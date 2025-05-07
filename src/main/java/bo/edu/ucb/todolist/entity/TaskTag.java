package bo.edu.ucb.todolist.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_tags")
@Data
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // Opcional: podrías añadir fecha de asignación, etc.
    // private LocalDateTime assignedAt;
}
