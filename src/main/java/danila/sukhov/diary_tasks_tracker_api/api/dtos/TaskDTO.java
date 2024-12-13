package danila.sukhov.diary_tasks_tracker_api.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import danila.sukhov.diary_tasks_tracker_api.store.entities.CommentEntity;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDTO {
    Long id;

    @NonNull
    String name;

    @NonNull
    String priority;

    @Builder.Default
    Instant createdAt = Instant.now();

    @NonNull
    String description;

    Set<CommentEntity> comments;
}
