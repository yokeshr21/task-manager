package com.example.task.manager.service;

import com.example.task.manager.model.Task;
import com.example.task.manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public String uploadTaskFile(Long taskId, MultipartFile file) throws IOException{
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with id"+ taskId));

        String savedPath = fileStorageService.storeFile(file);

        task.setFileName(file.getOriginalFilename());
        task.setFilePath(savedPath);

        taskRepository.save(task);

        return "File Saved Successfully "+ file.getOriginalFilename();
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

    public Page<Task> searchTasks(String title, boolean completed, Pageable pageable){
        return taskRepository.findByTitleContainingIgnoreCaseAndCompleted(title, completed, pageable);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
