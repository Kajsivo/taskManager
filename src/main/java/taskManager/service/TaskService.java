package taskManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskManager.entity.MultiChange;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;
import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void multiChange(List<String> tasksIds, String newValue, String fieldToChange) {
        switch (fieldToChange) {
            case MultiChange.STATUS:
                multiChangeStatusValue(tasksIds, newValue);
                break;
            case MultiChange.PRIORITY:
                multiChangePriorityValue(tasksIds, newValue);
                break;
            default:
                multiChangeStringValue(tasksIds, newValue, fieldToChange);
        }
    }

    public void multiChangeStatusValue(List<String> tasksIds, String newValue) {
        TaskStatus newStatus = TaskStatus.valueOf(newValue);
        tasksIds.forEach((taskId) -> {
            Task task = taskRepository.findOne(taskId);
            task.status = newStatus;
            taskRepository.save(task);
        });
    }

    public void multiChangePriorityValue(List<String> tasksIds, String newValue) {
        TaskPriority newPriority = TaskPriority.valueOf(newValue);
        tasksIds.forEach((taskId) -> {
            Task task = taskRepository.findOne(taskId);
            task.priority = newPriority;
            taskRepository.save(task);
        });
    }

    public void multiChangeStringValue(List<String> tasksIds, String newValue, String fieldToChange) {
        tasksIds.forEach((taskId) -> {
            try {
                Task task = taskRepository.findOne(taskId);
                Field field = Task.class.getField(fieldToChange);
                field.set(task, newValue);
                taskRepository.save(task);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            {

            }
        });
    }
}
