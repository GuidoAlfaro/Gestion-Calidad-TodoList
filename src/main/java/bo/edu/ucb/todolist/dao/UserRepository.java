package bo.edu.ucb.todolist.dao;

import bo.edu.ucb.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}