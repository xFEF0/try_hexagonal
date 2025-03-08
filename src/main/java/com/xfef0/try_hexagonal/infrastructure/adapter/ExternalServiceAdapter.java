package com.xfef0.try_hexagonal.infrastructure.adapter;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.port.out.ExternalServicePort;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class ExternalServiceAdapter implements ExternalServicePort {

    private final RestTemplate restTemplate;

    @Getter
    @Setter
    private static class JsonPlaceHolderTodo {
        private Long id;
        private Long userId;
    }

    @Getter
    @Setter
    private static class JsonPlaceHolderUser {
        private Long id;
        private String name;
        private String email;
    }

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        String todosUrl = "https://jsonplaceholder.typicode.com/todos/" + id;
        String usersUrl = "https://jsonplaceholder.typicode.com/users/";
        ResponseEntity<JsonPlaceHolderTodo> todoResponse = restTemplate.getForEntity(todosUrl, JsonPlaceHolderTodo.class);
        JsonPlaceHolderTodo todo = todoResponse.getBody();
        if (todo == null) {
            return null;
        }

        usersUrl = usersUrl + todo.getUserId();
        ResponseEntity<JsonPlaceHolderUser> userResponse = restTemplate.getForEntity(usersUrl, JsonPlaceHolderUser.class);
        JsonPlaceHolderUser user = userResponse.getBody();
        if (user == null) {
            return null;
        }

        return new AdditionalTaskInfo(user.getId(), user.getName(), user.getEmail());
    }
}
