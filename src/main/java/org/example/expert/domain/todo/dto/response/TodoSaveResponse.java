package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;

@Getter
public class TodoSaveResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;

    private TodoSaveResponse(Long id, String title, String contents, String weather, UserResponse user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
    }

    public static TodoSaveResponse from(Todo todo) {
        return new TodoSaveResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                UserResponse.from(todo.getUser())
        );
    }
}
