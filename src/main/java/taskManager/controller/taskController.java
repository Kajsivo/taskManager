package taskManager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
}
