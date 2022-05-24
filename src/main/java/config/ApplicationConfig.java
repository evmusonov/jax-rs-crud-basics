package config;

import domain.tasks.TaskService;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.TaskDao;
import resources.TaskResource;
import server.JettyServer;

@Configuration
public class ApplicationConfig {
    @Bean
    public SessionFactory sessionFactory() {
        return JettyServer.sessionFactory;
    }

    @Bean
    public TaskDao taskDao(SessionFactory sessionFactory) {
        return new TaskDao(sessionFactory);
    }

    @Bean
    public TaskService taskService(TaskDao taskDao) {
        return new TaskService(taskDao);
    }

    @Bean
    public TaskResource taskResource(TaskService taskService) {
        return new TaskResource(taskService);
    }
}
