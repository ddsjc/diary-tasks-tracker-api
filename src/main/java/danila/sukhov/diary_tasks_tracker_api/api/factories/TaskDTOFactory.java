package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.TaskDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDTOFactory {

    public TaskDTO createTaskDTO(TaskEntity taskEntity){
        return TaskDTO.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .createdAt(taskEntity.getCreatedAt())
                .description(taskEntity.getDescription())
                .priority(taskEntity.getPriority())
                .comments(taskEntity.getComments())
                .build();
    }
}
