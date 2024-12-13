package danila.sukhov.diary_tasks_tracker_api.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "task")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String name;

    String priority;

    @Builder.Default
    Instant createdAt = Instant.now();

    @ManyToOne
    TaskStateEntity taskStateEntity;

    @ManyToOne
    UserEntity userEntity;

    String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<CommentEntity> comments;
}
