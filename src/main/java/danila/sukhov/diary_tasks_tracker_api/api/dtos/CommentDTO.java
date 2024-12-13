package danila.sukhov.diary_tasks_tracker_api.api.dtos;

import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {


    String topic;

    @NonNull
    String description;

    @NonNull
    Instant createdAt;

    @NonNull
    UserEntity user;

    @NonNull
    TaskEntity task;
}
