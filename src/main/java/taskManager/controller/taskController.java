package taskManager.controller;


import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.dto.multiChangeDto;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;
import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class taskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity save(@RequestBody Task requestEntity) throws UnsupportedOperationException
    {
        try {
            Task task = taskRepository.save(requestEntity);
            return ResponseEntity.ok().body(task.id);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Something went wrong");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Task find(@PathVariable @NotNull long id) throws NotFoundException
    {
        Task task = taskRepository.findOne(id);
        if (task != null) {
            return task;
        } else {
            throw new NotFoundException("Task not found");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable @NotNull long id) throws NotFoundException
    {
        try {
            taskRepository.delete(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpStatus update(@PathVariable @NotNull long id, @RequestBody Task requestEntity) throws NotFoundException
    {
        try {
            requestEntity.id = id;
            taskRepository.save(requestEntity);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }

    }

    @RequestMapping(value="/multiChange/", method = RequestMethod.POST)
    public HttpStatus multiChange(@RequestBody multiChangeDto requestEntity) throws NotFoundException
    {
        try {
            List<Long> tasksIds = requestEntity.getTasksIds();
            String fieldToChange = requestEntity.getFieldToChange();
            String newValue = requestEntity.getNewValue();

            switch (fieldToChange){
                case "status":
                    multiChangeStatusValue(tasksIds, newValue);
                    break;
                case "priority":
                    multiChangePriorityValue(tasksIds, newValue);
                    break;
                default:
                    multiChangeStringValue(tasksIds, newValue, fieldToChange);
            }

            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException ex)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    public void multiChangeStatusValue(List<Long> tasksIds, String newValue) {
        TaskStatus newStatus = TaskStatus.valueOf(newValue);
        tasksIds.forEach((taskId) -> {
            Task task = taskRepository.findOne(taskId);
            task.status = newStatus;
            taskRepository.save(task);
        });
    }

    public void multiChangePriorityValue(List<Long> tasksIds, String newValue) {
        TaskPriority newPriority = TaskPriority.valueOf(newValue);
        tasksIds.forEach((taskId) -> {
            Task task = taskRepository.findOne(taskId);
            task.priority = newPriority;
            taskRepository.save(task);
        });
    }

    public void multiChangeStringValue(List<Long> tasksIds, String newValue, String fieldToChange) {
        tasksIds.forEach((taskId) -> {
            try {
                Task task = taskRepository.findOne(taskId);
                Field field = Task.class.getField(fieldToChange);
                field.set(task, newValue);
                taskRepository.save(task);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } {

            }
        });
    }
}
