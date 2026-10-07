package com.internal.tasktracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t "
         + "WHERE t.archived = false "
         + "  AND (LOWER(t.title) LIKE :term OR LOWER(t.description) LIKE :term) "
         + "  AND (:status IS NULL OR t.status = :status) "
         + "ORDER BY t.id ASC")
    Page<Task> searchTasks(@Param("term") String term,
                           @Param("status") String status,
                           Pageable pageable);
}