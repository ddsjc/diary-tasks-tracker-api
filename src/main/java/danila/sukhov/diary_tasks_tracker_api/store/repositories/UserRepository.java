package danila.sukhov.diary_tasks_tracker_api.store.repositories;

import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Stream<UserEntity> findByLogin(String login);
    UserEntity findUserByLogin(String login);
    boolean existsByLogin(String login);
}
