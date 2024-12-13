package danila.sukhov.diary_tasks_tracker_api.store.repositories;

import danila.sukhov.diary_tasks_tracker_api.store.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface CommentsRepository extends JpaRepository<CommentEntity, Long> {

}
