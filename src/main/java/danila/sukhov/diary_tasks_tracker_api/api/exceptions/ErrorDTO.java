package danila.sukhov.diary_tasks_tracker_api.api.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ErrorDTO {

    String error;

    @JsonProperty("error_description")
    String errorDescription;
}
