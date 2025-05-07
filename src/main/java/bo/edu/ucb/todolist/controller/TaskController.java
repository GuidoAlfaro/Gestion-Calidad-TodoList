package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.dto.ResponseDto;
import bo.edu.ucb.todolist.dto.TaskDto;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.repository.UserRepository;
import bo.edu.ucb.todolist.security.JwtUtil;
import bo.edu.ucb.todolist.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // POST /api/tasks - Create a new task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // GET /api/tasks - Retrieve all tasks

    @GetMapping
    public ResponseEntity<List<TaskDto>> getUserTasks(@RequestHeader("Authorization") String authHeader) {
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7); // quitar "Bearer "
            String email = jwtUtil.extractEmail(token);

            Users user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<Task> tasks = taskRepository.findByUserId(user.getId());
            log.info("Tasks retrieved for user: {}", user.getId());
            List<TaskDto> taskDtos = new ArrayList<>();
            for (Task task : tasks) {
                TaskDto taskDto = new TaskDto();
                taskDto.setId(task.getId());
                taskDto.setTitle(task.getTitle());
                taskDto.setDescription(task.getDescription());
                taskDto.setStatus(task.getStatus());
                // Add other fields as needed
                taskDtos.add(taskDto);
            }
            return ResponseEntity.ok(taskDtos);

        } catch (Exception e) {
            log.error("Error retrieving tasks: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // GET /api/tasks/{id} - Retrieve a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /api/tasks/{id} - Update a task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            // Add other fields as needed
            Task updatedTask = taskRepository.save(task);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE /api/tasks/{id} - Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/tasks/status/{status} - Retrieve tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskRepository.findByStatus(status); // Asumiendo que TaskRepository tiene este método
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET /api/tasks/tag/{tagId} - Retrieve tasks associated with a specific tag
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<Task>> getTasksByTagId(@PathVariable Long tagId) {
        List<Task> tasks = taskRepository.findByTagsId(tagId); // Asumiendo que TaskRepository tiene este método
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}