package taskManager.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskManager.api.exception.NotFoundException;
import taskManager.entity.MultiChange;
import taskManager.api.dto.TaskDto;
import taskManager.entity.Task;
import taskManager.repository.TaskRepository;
import taskManager.service.TaskService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


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
    public ResponseEntity save(@RequestBody Task requestEntity) {
        requestEntity.id = UUID.randomUUID().toString();
        Task task = taskRepository.save(requestEntity);
        return ResponseEntity.ok().body(task.id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public TaskDto find(@PathVariable @NotNull String id) {
        Task task = taskRepository.findOne(id);
        return modelMapper.map(task, TaskDto.class);

    }

    @RequestMapping(value = "assignee/{assigneeId}", method = RequestMethod.GET, produces = "application/json")
    public List findListForAssignee(@PathVariable @NotNull String assigneeId) {
        return taskRepository.findIdsByAssignee(assigneeId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable @NotNull String id) {
        taskRepository.delete(id);
        return HttpStatus.OK;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpStatus update(@PathVariable @NotNull String id, @RequestBody Task requestEntity) {
        requestEntity.id = id;
        taskRepository.save(requestEntity);
        return HttpStatus.OK;

    }

    @RequestMapping(value = "/multiChange/", method = RequestMethod.POST)
    public HttpStatus multiChange(@RequestBody MultiChange requestEntity) {
        List<String> tasksIds = requestEntity.getTasksIds();
        String fieldToChange = requestEntity.getFieldToChange();
        String newValue = requestEntity.getNewValue();

        taskService.multiChange(tasksIds, fieldToChange, newValue);

        return HttpStatus.OK;

    }
}
