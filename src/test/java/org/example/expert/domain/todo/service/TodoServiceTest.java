package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private TodoService todoService;

    @Test
    public void todo를_정상적으로_등록한다() {
        // Given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user =  User.fromAuthUser(authUser);
        TodoSaveRequest request = new TodoSaveRequest("Test Title", "Test Contents");

        long todoId = 1L;
        Todo todo = new Todo(request.getTitle(), request.getContents(), "Sunny", user);

        given(todoRepository.save(any())).willReturn(todo);

        // When
        TodoSaveResponse result = todoService.saveTodo(authUser, request);

        // Then
        assertNotNull(result);
    }

    @Test
    public void todo를_정상적으로_조회한다() {
        // Given
        long todoId = 1L;
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo("Test Title", "Test Contents", "Sunny", user);

        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.of(todo));

        // When
        TodoResponse result = todoService.getTodo(todoId);

        // Then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Contents", result.getContents());
    }

    @Test
    public void todo_조회_중_todo가_존재하지_않아_에러가_발생한다() {
        // Given
        long todoId = 1L;
        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidRequestException.class, () -> todoService.getTodo(todoId));
    }
}