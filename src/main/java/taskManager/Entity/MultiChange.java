package taskManager.entity;

import java.util.List;

public class MultiChange {

    public static final String STATUS = "status";
    public static final String PRIORITY = "priority";

    private List<String> tasksIds;
    private String fieldToChange;
    private String newValue;

    public List<String> getTasksIds() {
        return tasksIds;
    }

    public String getFieldToChange() {
        return fieldToChange;
    }

    public String getNewValue() {
        return newValue;
    }
}
