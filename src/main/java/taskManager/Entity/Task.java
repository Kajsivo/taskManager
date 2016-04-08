package taskManager.entity;

import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

import javax.persistence.*;

@Table
@Entity
public class Task {

    @Id
    @Column(nullable = false, insertable = true, updatable = false)
    public String id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String assignee;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TaskStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TaskPriority priority;
}
