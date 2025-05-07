package bo.edu.ucb.todolist.controller;

import bo.edu.ucb.todolist.entity.Tag;
import bo.edu.ucb.todolist.repository.TagRepository;
import bo.edu.ucb.todolist.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    // POST /api/tags - Create a new tag
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagRepository.save(tag);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
    }

    // GET /api/tags - Retrieve all tags
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    // GET /api/tags/{id} - Retrieve a tag by ID
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etiqueta", "id", id));
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    // PUT /api/tags/{id} - Update a tag
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etiqueta", "id", id));
        tag.setName(tagDetails.getName());
        Tag updatedTag = tagRepository.save(tag);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    // DELETE /api/tags/{id} - Delete a tag
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etiqueta", "id", id));
        tagRepository.delete(tag);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}