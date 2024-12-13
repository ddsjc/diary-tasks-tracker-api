package danila.sukhov.diary_tasks_tracker_api.api.factories;

import danila.sukhov.diary_tasks_tracker_api.api.dtos.UserDTO;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDTOFactory {
    @Autowired
    CommentDTOFactory commentDTOFactory;
    public UserDTO createUserDTO(UserEntity user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .projects(user.getProjects())
                .comments(user.getComments()
                        .stream()
                        .map(commentDTOFactory::createCommentDTO)
                        .collect(Collectors.toSet()) )
                .build();
    }

    public UserDTO cutCreateUserDTO(UserEntity user){
        return UserDTO.builder()
                .name(user.getName())
                .login(user.getLogin())
                .cutComments(user.getComments()
                        .stream()
                        .map(commentDTOFactory::cutCreateCommentDTO)
                        .collect(Collectors.toSet()) )
                .build();
    }
}
