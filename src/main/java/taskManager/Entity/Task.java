package taskManager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Table
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @JsonProperty("title")
    public String title;

    @Column(nullable = false)
    @JsonProperty("assignee")
    public String assignee;

    @Column(nullable = false)
    @JsonProperty("description")
    public String description;
}
