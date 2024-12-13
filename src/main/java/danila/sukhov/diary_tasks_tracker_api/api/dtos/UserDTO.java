package danila.sukhov.diary_tasks_tracker_api.api.dtos;

import danila.sukhov.diary_tasks_tracker_api.store.entities.CommentEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank
    Long id;

    String name;

    String surname;

    @NotBlank(message = "Login cannot be blank")
    @Size(max = 50, message = "Login must not exceed 50 characters")
    String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 255, message = "Password must not exceed 255 characters")
    String password;

    Instant createdAt = Instant.now();

    String role;

    Set<ProjectEntity> projects;

    Set<CommentDTO> comments;

    Set<CutCommentsDTO> cutComments;
}
