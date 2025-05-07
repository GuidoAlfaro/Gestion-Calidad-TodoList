package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.dto.AuthDto;
import bo.edu.ucb.todolist.dto.ResponseDto;
import bo.edu.ucb.todolist.dto.UserDto;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.repository.UserRepository;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.service.UserService;
import bo.edu.ucb.todolist.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    // POST /api/users/auth - User login
    @PostMapping("/auth")
    public ResponseEntity<ResponseDto<AuthDto>> login(@RequestBody AuthDto authDto) {
        try {
            AuthDto authResponse = userService.login(authDto);
            ResponseDto<AuthDto> response = new ResponseDto<>("Login successful", "success", authResponse);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.error("Login failed: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        }
    }

    // POST /api/users/new - Create a new user
    @PostMapping("/new")
    public ResponseEntity<ResponseDto<UserDto>> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            ResponseDto<UserDto> response = new ResponseDto<>("User created successfully", "success", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        }
    }

    // GET /api/users - Retrieve all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // GET /api/users/{id} - Retrieve a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // PUT /api/users/{id} - Update a user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        Users updatedUser = userRepository.save(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // DELETE /api/users/{id} - Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /api/users/{id}/tasks - Retrieve all tasks assigned to a user
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        List<Task> tasks = taskRepository.findByUserId(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}