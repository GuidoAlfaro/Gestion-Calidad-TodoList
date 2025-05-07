package bo.edu.ucb.todolist.dto;

import bo.edu.ucb.todolist.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Timestamp dueDate;
    private Timestamp createdAt;
    private String status;
    private List<TagDto> tags;


}
