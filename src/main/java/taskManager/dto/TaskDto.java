package taskManager.dto;

import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

public class TaskDto {

    public long id;

    public String title;

    public String assignee;

    public String description;

    public TaskStatus status;

    public TaskPriority priority;
}
