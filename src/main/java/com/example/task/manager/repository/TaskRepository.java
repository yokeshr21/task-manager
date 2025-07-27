package com.example.task.manager.repository;

import com.example.task.manager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAll(Pageable pageable);

    Page<Task> findByTitleContainingIgnoreCaseAndCompleted(String title, boolean completed, Pageable pageable);

}
