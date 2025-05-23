package bo.edu.ucb.todolist.repository;

import bo.edu.ucb.todolist.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByUsername(String username);
}