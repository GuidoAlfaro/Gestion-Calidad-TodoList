package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}