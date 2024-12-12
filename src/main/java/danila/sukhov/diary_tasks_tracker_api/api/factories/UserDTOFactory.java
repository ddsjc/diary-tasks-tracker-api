package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.UserDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDTOFactory {
    public UserDTO createUserDTO(UserEntity user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .projects(user.getProjects())
                .build();
    }
}
