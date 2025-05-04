package bo.edu.ucb.todolist.dao;

import bo.edu.ucb.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByUserId(Long userId);
}