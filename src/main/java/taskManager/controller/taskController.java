package taskManager.controller;


import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.entity.MultiChange;
import taskManager.dto.TaskDto;
import taskManager.repository.TaskRepository;
import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity save(@RequestBody TaskDto requestEntity) throws UnsupportedOperationException
    {
        try {
            TaskDto taskDto = taskRepository.save(requestEntity);
            return ResponseEntity.ok().body(taskDto.id);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Something went wrong");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public TaskDto find(@PathVariable @NotNull long id) throws NotFoundException
    {
        TaskDto taskDto = taskRepository.findOne(id);
        if (taskDto != null) {
            return taskDto;
        } else {
            throw new NotFoundException("TaskDto not found");
        }
    }

    @RequestMapping(value = "assignee/{assigneeId}", method = RequestMethod.GET, produces = "application/json")
    public List findListForAssignee(@PathVariable @NotNull String assigneeId) throws NotFoundException
    {
        List<Long> tasksList = taskRepository.findIdsByAssignee(assigneeId);
        if (tasksList != null) {
            return tasksList;
        } else {
            throw new NotFoundException("TaskDto not found");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable @NotNull long id) throws NotFoundException
    {
        try {
            taskRepository.delete(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("TaskDto not found");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpStatus update(@PathVariable @NotNull long id, @RequestBody TaskDto requestEntity) throws NotFoundException
    {
        try {
            requestEntity.id = id;
            taskRepository.save(requestEntity);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("TaskDto not found");
        }

    }

    @RequestMapping(value="/multiChange/", method = RequestMethod.POST)
    public HttpStatus multiChange(@RequestBody MultiChange requestEntity) throws NotFoundException
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
            throw new NotFoundException("TaskDto not found");
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
            TaskDto taskDto = taskRepository.findOne(taskId);
            taskDto.status = newStatus;
            taskRepository.save(taskDto);
        });
    }

    public void multiChangePriorityValue(List<Long> tasksIds, String newValue) {
        TaskPriority newPriority = TaskPriority.valueOf(newValue);
        tasksIds.forEach((taskId) -> {
            TaskDto taskDto = taskRepository.findOne(taskId);
            taskDto.priority = newPriority;
            taskRepository.save(taskDto);
        });
    }

    public void multiChangeStringValue(List<Long> tasksIds, String newValue, String fieldToChange) {
        tasksIds.forEach((taskId) -> {
            try {
                TaskDto taskDto = taskRepository.findOne(taskId);
                Field field = TaskDto.class.getField(fieldToChange);
                field.set(taskDto, newValue);
                taskRepository.save(taskDto);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            {

            }
        });
    }
}
