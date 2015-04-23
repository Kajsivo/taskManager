package taskManager.entity;

import taskManager.type.TaskPriority;
import taskManager.type.TaskStatus;

import javax.persistence.*;

@Table
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String assignee;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public TaskStatus status;

    @Column(nullable = false)
    public TaskPriority priority;
}
