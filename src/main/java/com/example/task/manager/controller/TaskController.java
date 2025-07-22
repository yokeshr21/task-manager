package com.example.task.manager.controller;

import com.example.task.manager.exception.ResourceNotFoundException;
import com.example.task.manager.model.Task;
import com.example.task.manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/getAllTasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
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
