package bo.edu.ucb.todolist.service;

import bo.edu.ucb.todolist.dto.TagDto;
import bo.edu.ucb.todolist.dto.TaskDto;
import bo.edu.ucb.todolist.entity.Tag;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.entity.TaskTag;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.repository.TaskRepository;
import bo.edu.ucb.todolist.repository.TaskTagRepository;
import bo.edu.ucb.todolist.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskTagRepository taskTagRepository;
    @Autowired
    private TagService tagService;

    public List<TaskDto> getAllTasks(String authHeader) {
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token inválido");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            Users user = userService.getUserByEmail(email);

            if (user == null) {
                throw new RuntimeException("Usuario no encontrado");
            }

            // Usamos la query con JOIN FETCH
            List<TaskTag> taskTags = taskTagRepository.findTaskTagsByUserId(user.getId());
            // Agrupamos por tarea
            Map<Long, TaskDto> taskMap = new LinkedHashMap<>();

            for (TaskTag tt : taskTags) {
                Task task = tt.getTask();
                Tag tag = tt.getTag();

                TaskDto dto = taskMap.get(task.getId());

                if (dto == null) {
                    dto = new TaskDto();
                    dto.setId(task.getId());
                    dto.setTitle(task.getTitle());
                    dto.setDescription(task.getDescription());
                    dto.setCreatedAt(task.getCreatedAt());
                    dto.setDueDate(task.getDueDate());
                    dto.setStatus(task.getStatus());
                    dto.setTags(new ArrayList<>()); // Ahora es lista de TagDto
                    taskMap.put(task.getId(), dto);
                }

                // Convertir Tag → TagDto
                TagDto tagDto = new TagDto(tag.getName());
                dto.getTags().add(tagDto);
            }

            return new ArrayList<>(taskMap.values());

        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar tareas con tags", e);
        }
    }

//    public TaskDto createTask(TaskDto taskDto, String authHeader) {
//        JwtUtil jwtUtil = new JwtUtil();
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                throw new RuntimeException("Token inválido");
//            }
//
//            String token = authHeader.substring(7);
//            String email = jwtUtil.extractEmail(token);
//            Users user = userService.getUserByEmail(email);
//
//            if (user == null) {
//                throw new RuntimeException("Usuario no encontrado");
//            }
//
//            Task task = new Task();
//            task.setTitle(taskDto.getTitle());
//            task.setDescription(taskDto.getDescription());
//            task.setDueDate(taskDto.getDueDate());
//            task.setStatus(taskDto.getStatus());
//            task.setUser(user);
//
//
//            //Task savedTask = taskRepository.save(task);
//
//            List<Tag> tags = taskDto.getTags().stream()
//                    .map(tagDto -> {
//                        Tag tag = tagService.findByName(tagDto.getName());
//                        if (tag == null) {
//                            tag = new Tag();
//                            tag.setName(tagDto.getName());
//                        }
//                        return tag;
//                    })
//                    .toList();
//
//
//
//
//
//            // Guardar las etiquetas
//            for (TagDto tagDto : taskDto.getTags()) {
//                Tag tag = new Tag();
//                tag.setName(tagDto.getName());
//                tag = taskTagRepository.save(tag); // Guardar la etiqueta
//
//                TaskTag taskTag = new TaskTag();
//                taskTag.setTask(savedTask);
//                taskTag.setTag(tag);
//                taskTagRepository.save(taskTag);
//            }
//
//            return new TaskDto(savedTask.getId(), savedTask.getTitle(), savedTask.getDescription(), savedTask.getDueDate(), savedTask.getCreatedAt(), savedTask.getStatus(), taskDto.getTags());
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error al crear tarea", e);
//        }
//    }

}
