package taskManager.dto;

import java.util.List;

public class changeAssigneeDto {

    private List<Long> tasksIds;
    private String assignee;

    public List<Long> getTasksIds() {
        return tasksIds;
    }

    public String getAssignee() {
        return assignee;
    }
}
