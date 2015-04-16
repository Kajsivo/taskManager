package taskManager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import taskManager.entity.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, Long> {
}
