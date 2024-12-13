package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.CommentDTO;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.CutCommentsDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.CommentEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentDTOFactory {
    public CommentDTO createCommentDTO(CommentEntity comment){
        return CommentDTO.builder()
                .topic(comment.getTopic())
                .description(comment.getDescription())
                .createdAt(comment.getCreatedAt())
                .task(comment.getTask())
                .user(comment.getUser())
                .build();
    }

    public CutCommentsDTO cutCreateCommentDTO(CommentEntity comment){
        return CutCommentsDTO.builder()
                .topic(comment.getTopic())
                .description(comment.getDescription())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
