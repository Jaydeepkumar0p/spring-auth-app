package com.example.SpringAuth.Service;

import com.example.SpringAuth.Repository.TodoRepository;
import com.example.SpringAuth.Repository.UserRepository;
import com.example.SpringAuth.Dto.TodoRequest;
import com.example.SpringAuth.Dto.TodoResponse;
import com.example.SpringAuth.entity.Todo;
import com.example.SpringAuth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // ── Helper ────────────────────────────────────────────────────────────
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getCreatedAt()
        );
    }

    // ── Create ────────────────────────────────────────────────────────────
    public TodoResponse create(String email, TodoRequest request) {
        User user = getUser(email);

        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setUser(user);

        Todo saved = todoRepository.save(todo);

        // Send email async — won't block the HTTP response
        emailService.sendTodoCreated(
                user.getEmail(), user.getName(),
                saved.getTitle(), saved.getDescription()
        );

        return toResponse(saved);
    }

    // ── Get All ───────────────────────────────────────────────────────────
    public List<TodoResponse> getAll(String email) {
        User user = getUser(email);
        return todoRepository.findByUser(user)
                .stream().map(this::toResponse).toList();
    }

    // ── Get by ID ─────────────────────────────────────────────────────────
    public TodoResponse getById(Long id, String email) {
        User user = getUser(email);
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        return toResponse(todo);
    }

    // ── Update ────────────────────────────────────────────────────────────
    public TodoResponse update(Long id, String email, TodoRequest request) {
        User user = getUser(email);
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());

        Todo saved = todoRepository.save(todo);
        emailService.sendTodoUpdated(user.getEmail(), user.getName(), saved.getTitle());

        return toResponse(saved);
    }

    // ── Complete ──────────────────────────────────────────────────────────
    public TodoResponse complete(Long id, String email) {
        User user = getUser(email);
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todo.setCompleted(true);
        Todo saved = todoRepository.save(todo);
        emailService.sendTodoCompleted(user.getEmail(), user.getName(), saved.getTitle());

        return toResponse(saved);
    }

    // ── Delete ────────────────────────────────────────────────────────────
    public void delete(Long id, String email) {
        User user = getUser(email);
        Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        String title = todo.getTitle();
        todoRepository.delete(todo);
        emailService.sendTodoDeleted(user.getEmail(), user.getName(), title);
    }
}