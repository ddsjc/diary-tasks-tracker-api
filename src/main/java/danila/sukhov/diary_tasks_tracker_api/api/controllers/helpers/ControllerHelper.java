package danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers;

import danila.sukhov.diary_tasks_tracker_api.api.exceptions.NotFoundException;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskStateEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.ProjectRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskStateRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {
    ProjectRepository projectRepository;
    TaskStateRepository taskStateRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
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
}
