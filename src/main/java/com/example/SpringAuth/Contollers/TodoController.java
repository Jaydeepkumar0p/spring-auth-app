package com.example.SpringAuth.controller;

import com.example.SpringAuth.Dto.TodoRequest;
import com.example.SpringAuth.Dto.TodoResponse;
import com.example.SpringAuth.Service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> create(
            @RequestBody TodoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.create(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.getAll(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.getById(id, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> update(
            @PathVariable Long id,
            @RequestBody TodoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.update(id, userDetails.getUsername(), request));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoResponse> complete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.complete(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        todoService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}