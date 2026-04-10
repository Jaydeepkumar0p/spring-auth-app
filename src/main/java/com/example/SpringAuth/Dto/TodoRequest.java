package com.example.SpringAuth.Dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
}
