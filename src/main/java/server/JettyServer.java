package server;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import domain.tasks.Task;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

public class JettyServer {
    public static EmbeddedPostgres embeddedPostgres = null;
    public static SessionFactory sessionFactory = null;

    private static Server createServer(int port) {
        final ResourceConfig config = new ResourceConfig()
                .packages("resources")
                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // Start Jetty Server
        return JettyHttpContainerFactory.createServer(
                        URI.create(String.format("http://localhost:%d/", port)), config);
    }

    private static void createDatabase() {
        try {
            embeddedPostgres = EmbeddedPostgres.builder().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sessionFactory = createSessionFactory();
    }

    private static void createTable() {
        if (embeddedPostgres != null) {
            try {
                embeddedPostgres.getPostgresDatabase().getConnection().prepareStatement("create table task(\n" +
                        "    id serial primary key,\n" +
                        "    title varchar(255),\n" +
                        "    description varchar(255),\n" +
                        "    finished boolean\n" +
                        ");").execute();
                embeddedPostgres.getPostgresDatabase().getConnection().prepareStatement(
                        "insert into task (title, description, finished) values ('my first task', 'yoopy', false)")
                        .execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static SessionFactory createSessionFactory() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", embeddedPostgres.getJdbcUrl("postgres"))
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "postgres")
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Task.class)
                .buildMetadata();

        System.out.printf("DB: Host: %s, port: %s", embeddedPostgres.getHost(), embeddedPostgres.getPort());

        return metadata.buildSessionFactory();
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        createDatabase();
        createTable();
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
