package taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import taskManager.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query(value = "SELECT id FROM task WHERE assignee = :assignee", nativeQuery = true)
    List<String> findIdsByAssignee(@Param("assignee") String assignee);
}
