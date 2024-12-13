package danila.sukhov.diary_tasks_tracker_api.api.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CutCommentsDTO {
    String topic;

    @NonNull
    String description;

    @NonNull
    Instant createdAt;
}
