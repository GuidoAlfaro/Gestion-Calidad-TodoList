package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    @Query("""
    SELECT tt FROM TaskTag tt
    JOIN FETCH tt.task t
    JOIN FETCH tt.tag
    WHERE t.user.id = :userId
""")
    List<TaskTag> findTaskTagsByUserId(@Param("userId") Long userId);

}
