package com.example.SpringAuth.Repository;

import com.example.SpringAuth.entity.Todo;
import com.example.SpringAuth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    List<Todo> findByUserAndCompleted(User user, boolean completed);
    Optional<Todo> findByIdAndUser(Long id, User user);
}