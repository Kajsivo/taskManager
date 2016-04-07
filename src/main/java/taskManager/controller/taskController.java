package taskManager.controller;


import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.entity.MultiChange;
import taskManager.dto.TaskDto;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;
import taskManager.service.TaskService;

import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepository taskRepository, ModelMapper modelMapper, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.taskService = taskService;

    }

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
    public TaskDto find(@PathVariable @NotNull long id) throws NotFoundException
    {
        Task task = taskRepository.findOne(id);
        if (task != null) {
            return modelMapper.map(task, TaskDto.class);
        } else {
            throw new NotFoundException("Task not found");
        }
    }

    @RequestMapping(value = "assignee/{assigneeId}", method = RequestMethod.GET, produces = "application/json")
    public List findListForAssignee(@PathVariable @NotNull String assigneeId) throws NotFoundException
    {
        List<Long> tasksList = taskRepository.findIdsByAssignee(assigneeId);
        if (tasksList != null) {
            return tasksList;
        } else {
            throw new NotFoundException("No tasks found");
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
    public HttpStatus multiChange(@RequestBody MultiChange requestEntity) throws NotFoundException
    {
        try {
            List<Long> tasksIds = requestEntity.getTasksIds();
            String fieldToChange = requestEntity.getFieldToChange();
            String newValue = requestEntity.getNewValue();

            taskService.multiChange(tasksIds, fieldToChange, newValue);

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
}
