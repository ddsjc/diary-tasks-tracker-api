package danila.sukhov.diary_tasks_tracker_api.store.repositories;

import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
