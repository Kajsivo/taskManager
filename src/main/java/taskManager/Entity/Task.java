package taskManager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
