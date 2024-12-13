package danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers;

import danila.sukhov.diary_tasks_tracker_api.api.exceptions.NotFoundException;
import danila.sukhov.diary_tasks_tracker_api.store.entities.*;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskStateRepository taskStateRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentsRepository commentsRepository;
    public ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Name not found", projectId))
                );
    }

    public TaskStateEntity getTaskStateOrThrowException(Long taskStateId){
        return  taskStateRepository.
                findById(taskStateId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Task state not found", taskStateId))
                );
    }

    public TaskEntity getTaskOrThrowException(Long taskId){
        return  taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException(
                String.format("Task not found", taskId))
        );
    }

    public UserEntity getUserOrThrowExceptiom(String userName){
        return  userRepository.findByLogin(userName).findAny().orElseThrow(() -> new NotFoundException(
                String.format("User is not found", userName))
        );
    }

    public CommentEntity getCommentOrThrowException(Long commentId){
        return commentsRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Comment is not found", commentId))
        );
    }
}
