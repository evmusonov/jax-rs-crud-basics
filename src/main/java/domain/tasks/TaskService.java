package domain.tasks;

import dto.TaskCreateDTO;
import dto.TaskUpdateDTO;
import exceptions.InvalidArgumentsException;
import exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TaskDao;

import java.util.Set;

@Service
public class TaskService {
    private final TaskDao dao;

    @Autowired
    public TaskService(TaskDao taskDao) {
        this.dao = taskDao;
    }

    public Set<Task> getAll() {
        return dao.getAll();
    }

    public Task findOne(Integer id) {
        return dao.findOne(id).orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }

    public void create(TaskCreateDTO taskCreateDTO) {
        if (dao.findByTitle(taskCreateDTO.title).isPresent()) {
            throw new InvalidArgumentsException("Запись уже существует");
        }

        Task task = new Task();
        task.setTitle(taskCreateDTO.title);
        task.setDescription(taskCreateDTO.description);
        task.setFinished(taskCreateDTO.finished);
        dao.create(task);
    }

    public void update(Integer id, TaskUpdateDTO taskUpdateDTO) {
        Task task = dao.findOne(id).orElseThrow(() -> new NotFoundException("Запись не найдена"));

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
        Task task = dao.findOne(id).orElseThrow(() -> new NotFoundException("Запись не найдена"));

        dao.delete(task);
    }
}
