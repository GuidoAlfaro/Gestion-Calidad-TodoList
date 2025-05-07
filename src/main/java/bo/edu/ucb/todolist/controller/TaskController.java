package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.dto.ResponseDto;
import bo.edu.ucb.todolist.dto.TaskDto;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.service.TaskService;
import bo.edu.ucb.todolist.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    // POST /api/tasks - Create a new task
    @PostMapping
    public ResponseEntity<ResponseDto<TaskDto>> createTask(@RequestBody TaskDto taskDto, @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Creating new task");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResourceNotFoundException("Token de autorización inválido o ausente");
            }
            TaskDto createdTask = taskService.createTask(taskDto, authHeader);
            ResponseDto<TaskDto> response = new ResponseDto<>("Task created successfully", "success", createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            log.error("Error creating task: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        }
    }

    // GET /api/tasks - Retrieve all tasks
    @GetMapping
    public ResponseEntity<ResponseDto<List<TaskDto>>> getUserTasks(@RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Retrieving tasks for user");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResourceNotFoundException("Token de autorización inválido o ausente");
            }
            ResponseDto<List<TaskDto>> response = new ResponseDto<>("Tasks retrieved successfully", "success", taskService.getAllTasks(authHeader));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.error("Error retrieving tasks: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
        } catch (Exception e) {
            log.error("Error retrieving tasks: {}", e.getMessage());
            throw e; // Re-lanzar para que GlobalExceptionHandler lo maneje
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
}