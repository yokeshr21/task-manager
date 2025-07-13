package com.example.task.manager.service;

import com.example.task.manager.model.Task;
import com.example.task.manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateById(Task taskDetails, Long id) {
        Optional<Task> task = Optional.of(taskRepository.getById(id));

        if(task.isPresent()){
            Task existingTask = task.get();
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setCompleted(taskDetails.isCompleted());
            return taskRepository.save(existingTask);
        }else{
            return null;
        }
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
