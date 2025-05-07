package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.dto.TaskDto;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.repository.UserRepository;
import bo.edu.ucb.todolist.security.JwtUtil;
import bo.edu.ucb.todolist.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    // GET /api/tasks - Retrieve all tasks for the authenticated user
    @GetMapping
    public ResponseEntity<List<TaskDto>> getUserTasks(@RequestHeader("Authorization") String authHeader) {
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResourceNotFoundException("Token de autorización inválido o ausente");
            }

            String token = authHeader.substring(7); // quitar "Bearer "
            String email = jwtUtil.extractEmail(token);

            Users user = userRepository.findByEmail(email);
            if (user == null) {
                throw new ResourceNotFoundException("Usuario no encontrado con email: " + email);
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
                taskDtos.add(taskDto);
            }
            return ResponseEntity.ok(taskDtos);

        } catch (Exception e) {
            log.error("Error retrieving tasks: {}", e.getMessage());
            throw e; // Dejamos que GlobalExceptionHandler maneje la excepción
        }
    }

    // GET /api/tasks/{id} - Retrieve a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", "id", id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // PUT /api/tasks/{id} - Update a task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", "id", id));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        Task updatedTask = taskRepository.save(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    // DELETE /api/tasks/{id} - Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", "id", id));
        taskRepository.delete(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /api/tasks/status/{status} - Retrieve tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET /api/tasks/tag/{tagId} - Retrieve tasks associated with a specific tag
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<Task>> getTasksByTagId(@PathVariable Long tagId) {
        List<Task> tasks = taskRepository.findByTagsId(tagId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}