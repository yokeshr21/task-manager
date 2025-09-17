package com.example.task.manager.controller;

import com.example.task.manager.exception.ResourceNotFoundException;
import com.example.task.manager.model.Task;
import com.example.task.manager.repository.TaskRepository;
import com.example.task.manager.service.FileStorageService;
import com.example.task.manager.service.TaskService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/getAllTasks")
    public Page<Task> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Task>> searchTasks(
        @RequestParam String title,
        @RequestParam boolean completed,
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ){
        Page<Task> result = taskService.searchTasks(title, completed, pageable);
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/getById/{id}")
//    public ResponseEntity<Task> getById(@PathVariable Long id){
//        Optional<Task> task = taskService.getTaskById(id);
//
//        if(task.isPresent()){
//            return new ResponseEntity<>(task.get(), HttpStatus.OK);
//        }
//        else{
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping("/{taskId}/upload")
    public ResponseEntity<String> saveFile(@PathVariable long taskId, @RequestParam("file")MultipartFile file){

        try{
            String result = taskService.uploadTaskFile(taskId,file);
            return ResponseEntity.ok(result);
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body("Upload failed : "+ex.getMessage());
        }
    }

    @GetMapping("/{taskId}/download")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long taskId){
        try{
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with id"+ taskId));

            UrlResource resource = fileStorageService.loadFileAsResource(task.getFilePath());

            String fileName = task.getFileName();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/saveTask")
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveTask(@Valid @RequestBody Task task){
        return taskService.createTask(task);
    }

    @PutMapping("/updateTask/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails){
        Task updatedTask = taskService.updateById(taskDetails, id);

        if(updatedTask != null){
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
