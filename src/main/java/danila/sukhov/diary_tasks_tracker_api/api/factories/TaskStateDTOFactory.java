package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.TaskStateDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskStateEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TaskStateDTOFactory {

    TaskDTOFactory taskDTOFactory;
    public TaskStateDTO createTaskStateDTO(TaskStateEntity taskStateEntity){
        return TaskStateDTO.builder()
                .id(taskStateEntity.getId())
                .createdAt(taskStateEntity.getCreatedAt())
                .name(taskStateEntity.getName())
                .leftTaskStateId(taskStateEntity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(taskStateEntity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .tasks(taskStateEntity
                        .getTasks()
                        .stream()
                        .map(taskDTOFactory::createTaskDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
