package bo.edu.ucb.todolist.service;

import bo.edu.ucb.todolist.dto.TagDto;
import bo.edu.ucb.todolist.entity.Tag;
import bo.edu.ucb.todolist.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<TagDto> listAllTags() {
        try {
            List<Tag> tags = tagRepository.findAll();
            return tags.stream()
                    .map(tag -> new TagDto(tag.getId(), tag.getName()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching tags", e);
        }
    }

    public TagDto createTag(TagDto tagDto) {
        try {
            Tag tag = new Tag();
            tag.setName(tagDto.getName());
            Tag savedTag = tagRepository.save(tag);
            return new TagDto(savedTag.getId(), savedTag.getName());
        } catch (Exception e) {
            throw new RuntimeException("Error creating tag", e);
        }
    }

    public TagDto updateTag(Long id, TagDto tagDto) {
        try {
            Tag tag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
            tag.setName(tagDto.getName());
            Tag updatedTag = tagRepository.save(tag);
            return new TagDto(updatedTag.getId(), updatedTag.getName());
        } catch (Exception e) {
            throw new RuntimeException("Error updating tag", e);
        }
    }

    public void deleteTag(Long id) {
        try {
            Tag tag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
            tagRepository.delete(tag);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting tag", e);
        }
    }
}
