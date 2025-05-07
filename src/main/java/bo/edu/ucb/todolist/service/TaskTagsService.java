package bo.edu.ucb.todolist.service;

import bo.edu.ucb.todolist.entity.Tag;
import bo.edu.ucb.todolist.entity.Task;
import bo.edu.ucb.todolist.entity.TaskTag;
import bo.edu.ucb.todolist.repository.TaskTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskTagsService {
    @Autowired
    private TaskTagRepository taskTagRepository;

    public TaskTag createTaskTag(TaskTag taskTag) {
        try {
            return taskTagRepository.save(taskTag);
        } catch (Exception e) {
            throw new RuntimeException("Error creating TaskTag", e);
        }
    }

    public TaskTag createTaskTag(Long taskId, Long tagId) {
        try {
            Task task = new Task();
            task.setId(taskId);
            Tag tag = new Tag();
            tag.setId(tagId);
            TaskTag taskTag = new TaskTag();
            taskTag.setTask(task);
            taskTag.setTag(tag);
            return taskTagRepository.save(taskTag);
        } catch (Exception e) {
            throw new RuntimeException("Error creating TaskTag", e);
        }
    }
}
