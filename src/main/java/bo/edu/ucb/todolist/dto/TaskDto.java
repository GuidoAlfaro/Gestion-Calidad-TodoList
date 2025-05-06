package bo.edu.ucb.todolist.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Timestamp dueDate;
    private Timestamp createdAt;
    private String status;
    private Set<TagDto> tags;
}
