package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.ProjectDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

//По сути не реализует паттерн фактори, а скорее является конвертером обычным
@Component
public class ProjectDTOFactory {
    //make

    //@ResponceBody()
    public ProjectDTO createProjectDto(ProjectEntity projectEntity){
        return ProjectDTO.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .createdAt(projectEntity.getCreatedAt())
                .users(projectEntity.getUsers())
                .build();
    }
}
