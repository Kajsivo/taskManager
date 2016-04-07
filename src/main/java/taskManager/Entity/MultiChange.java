package taskManager.entity;

import java.util.List;

public class MultiChange {

    private List<Long> tasksIds;
    private String fieldToChange;
    private String newValue;

    public List<Long> getTasksIds() {
        return tasksIds;
    }

    public String getFieldToChange() {
        return fieldToChange;
    }

    public String getNewValue() {
        return newValue;
    }
}
