package taskManager.controller;


import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;


@RestController
@RequestMapping("/tasks")
public class taskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public HttpStatus create(@RequestBody Task requestEntity) throws UnsupportedOperationException {
        try {
            taskRepository.save(requestEntity);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Something went wrong");
        }
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Task find(@PathVariable long id) throws NotFoundException {
        Task task = taskRepository.findOne(id);
        if (task != null) {
            return task;
        } else {
            throw new NotFoundException("Task not found");
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable long id) throws NotFoundException {
        try {
            taskRepository.delete(id);
        } catch (Exception e) {
            throw new NotFoundException("Task not found");
        }
        return HttpStatus.OK;

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
