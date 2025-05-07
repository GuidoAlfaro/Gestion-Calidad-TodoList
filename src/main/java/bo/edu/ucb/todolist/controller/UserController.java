package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.dto.AuthDto;
import bo.edu.ucb.todolist.dto.ResponseDto;
import bo.edu.ucb.todolist.dto.UserDto;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.repository.UserRepository;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<ResponseDto<AuthDto>> login(@RequestBody AuthDto authDto) {
        try {
            AuthDto authResponse = userService.login(authDto);
            ResponseDto<AuthDto> response = new ResponseDto<>("Login successful", "success", authResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDto<AuthDto> response = new ResponseDto<>("Login failed:"+e.getMessage(), "error", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    // POST /api/users - Create a new user
    @PostMapping("/new")
    public ResponseEntity<ResponseDto<UserDto>> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            ResponseDto<UserDto> response = new ResponseDto<>("User created successfully", "success", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseDto<UserDto> response = new ResponseDto<>("Error creating user: " + e.getMessage(), "error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
        Optional<Users> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /api/users/{id} - Update a user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUsername(userDetails.getUsername()); // Asumiendo que User tiene un campo username
            user.setEmail(userDetails.getEmail());       // Asumiendo que User tiene un campo email
            // Actualiza otros campos según la entidad User
            Users updatedUser = userRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE /api/users/{id} - Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/users/{id}/tasks - Retrieve all tasks assigned to a user
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Task> tasks = taskRepository.findByUserId(id); // Asumiendo que TaskRepository tiene este método
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}