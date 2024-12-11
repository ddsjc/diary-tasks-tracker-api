package danila.sukhov.diary_tasks_tracker_api.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationDTO {

    Long id;

    String name;

    String surname;

    String login;

    String password;

    Instant createdAt = Instant.now();

    Set<String> roles;
}
