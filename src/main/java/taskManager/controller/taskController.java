package taskManager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;


@RestController
@RequestMapping("/tasks")
public class taskController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value="/create", method = RequestMethod.POST, consumes = "application/json")
    public HttpStatus create(@RequestBody Task requestEntity) {
        taskRepository.save(requestEntity);
        return HttpStatus.OK;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET, produces = "application/json")
    public Task find(@PathVariable long id) {
        return taskRepository.findOne(id);
    }
}
