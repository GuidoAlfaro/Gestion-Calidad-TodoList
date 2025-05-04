package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}