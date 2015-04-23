package taskManager.controller;


import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;

import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/tasks")
public class taskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity save(@RequestBody Task requestEntity) throws UnsupportedOperationException {
        try {
            Task task = taskRepository.save(requestEntity);
            return ResponseEntity.ok().body(task.id);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Something went wrong");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Task find(@PathVariable @NotNull long id) throws NotFoundException {
        Task task = taskRepository.findOne(id);
        if (task != null) {
            return task;
        } else {
            throw new NotFoundException("Task not found");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable @NotNull long id) throws NotFoundException {
        try {
            taskRepository.delete(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpStatus update(@PathVariable @NotNull long id, @RequestBody Task requestEntity) throws NotFoundException {
        try {
            Task task = taskRepository.findOne(id);
            task.title = requestEntity.title;
            task.description = requestEntity.description;
            taskRepository.save(task);
            return HttpStatus.OK;
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
