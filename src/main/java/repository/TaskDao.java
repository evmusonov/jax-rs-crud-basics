package repository;

import domain.tasks.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class TaskDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public TaskDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Set<Task> getAll() {
        return new HashSet<>(getSession()
                .createQuery("from Task", Task.class)
                .getResultList()
        );
    }

    public Optional<Task> findOne(Integer id) {
        Task task = getSession().get(Task.class, id);
        return Optional.ofNullable(task);
    }

    public Optional<Task> findByTitle(String title) {
        Task task = getSession().createQuery("from Task where title = :title", Task.class)
                .setParameter("title", title)
                .getSingleResultOrNull();

        return Optional.ofNullable(task);
    }

    public void create(Task task) {
        sessionFactory.inTransaction(session -> session.persist(task));
    }

    public void update(Task task) {
        sessionFactory.inTransaction(session -> session.update(task));
    }

    public void delete(Task task) {
        sessionFactory.inTransaction(session -> session.remove(task));
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }
}
