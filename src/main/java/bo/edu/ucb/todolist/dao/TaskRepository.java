package bo.edu.ucb.todolist.dao;

import bo.edu.ucb.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, String status);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}