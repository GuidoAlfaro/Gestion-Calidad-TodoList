package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.dto.TagDto;
import bo.edu.ucb.todolist.entity.Tag;
import bo.edu.ucb.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}