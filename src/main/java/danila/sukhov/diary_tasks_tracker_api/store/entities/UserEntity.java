package danila.sukhov.diary_tasks_tracker_api.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String surname;

    @Column(unique = true, nullable = false)
    String login;

    //@JsonIgnore
    //@Column(nullable = false)
    String password;

    @Builder.Default
    Instant createdAt = Instant.now();

    String role;

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "user_entity_id", referencedColumnName = "id")
    List<TaskEntity> tasks = new ArrayList<>();



}
