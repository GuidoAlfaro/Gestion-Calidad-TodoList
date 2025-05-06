package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByUserId(Long userId);
    List<Task> findByTagsId(Long tagId);
}