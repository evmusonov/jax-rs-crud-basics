package domain.tasks;

import dto.TaskCreateDTO;
import dto.TaskUpdateDTO;
import jakarta.transaction.Transactional;
import repository.TaskDao;
import server.JettyServer;

import java.util.Set;

public class TaskService {
    private final TaskDao dao;

    public TaskService() {
        dao = new TaskDao(JettyServer.sessionFactory);
    }

    public Set<Task> getAll() {
        return dao.getAll();
    }

    public Task findOne(Integer id) {
        Task task = dao.findOne(id);
        if (task == null) {
            throw new RuntimeException("Запись не найдена");
        }

        return task;
    }

    public void create(TaskCreateDTO taskCreateDTO) {
        if (dao.findByTitle(taskCreateDTO.title) != null) {
            throw new RuntimeException("Запись уже существует");
        }

        Task task = new Task();
        task.setTitle(taskCreateDTO.title);
        task.setDescription(taskCreateDTO.description);
        task.setFinished(taskCreateDTO.finished);
        dao.create(task);
    }

    public void update(Integer id, TaskUpdateDTO taskUpdateDTO) {
        Task task = dao.findOne(id);
        if (task == null) {
            throw new RuntimeException("Запись не найдена");
        }

        if (taskUpdateDTO.title != null) {
            task.setTitle(taskUpdateDTO.title);
        }
        if (taskUpdateDTO.description != null) {
            task.setDescription(taskUpdateDTO.description);
        }
        if (taskUpdateDTO.finished != null) {
            task.setFinished(taskUpdateDTO.finished);
        }
        dao.update(task);
    }

    public void remove(Integer id) {
        Task task = dao.findOne(id);
        if (task == null) {
            throw new RuntimeException("Запись не найдена");
        }

        dao.delete(task);
    }
}
