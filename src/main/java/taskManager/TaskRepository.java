package taskManager;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import taskManager.Entity.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, Long> {
}
