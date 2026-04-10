package com.example.SpringAuth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
}