package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.ProjectDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

//По сути не реализует паттерн фактори, а скорее является конвертером обычным
@Component
public class ProjectDTOFactory {
    //make
    @Autowired
    UserDTOFactory userDTOFactory;
    //@ResponceBody()
    public ProjectDTO createProjectDto(ProjectEntity projectEntity){
        return ProjectDTO.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .createdAt(projectEntity.getCreatedAt())
                .users(projectEntity.getUsers()
                        .stream()
                        .map(userDTOFactory::cutCreateUserDTO)
                        .collect(Collectors.toSet()) )
                .build();
    }
}
