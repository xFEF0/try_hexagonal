package com.xfef0.try_hexagonal.infrastructure.adapter;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@ActiveProfiles("test")
class ExternalServiceAdapterTest {

    private static final String TODOS_URL = "https://jsonplaceholder.typicode.com/todos/";
    private static final String USERS_URL = "https://jsonplaceholder.typicode.com/users/";
    private static final String TODO_JSON = "{\"id\":3,\"userId\":5}";
    private static final String USER_JSON = "{\"id\":5,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";

    @Autowired
    private ExternalServiceAdapter externalServiceAdapter;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        externalServiceAdapter = new ExternalServiceAdapter(restTemplate);
    }

    @Test
    void shouldGetAdditionalTaskInfo() {
        long taskId = 3L;
        mockServer.expect(requestTo(TODOS_URL + taskId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(TODO_JSON, MediaType.APPLICATION_JSON));

        long userId = 5L;
        mockServer.expect(requestTo(USERS_URL + userId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(USER_JSON, MediaType.APPLICATION_JSON));

        AdditionalTaskInfo taskInfo = externalServiceAdapter.getAdditionalTaskInfo(taskId);

        String userName = "John Doe";
        String userEmail = "john.doe@example.com";
        assertThat(taskInfo)
                .isNotNull()
                .extracting(AdditionalTaskInfo::getUserId, AdditionalTaskInfo::getUserName, AdditionalTaskInfo::getUserEmail)
                .containsExactly(userId, userName, userEmail);
    }

    @Test
    void shouldNotGetAdditionalInfoWhenTaskNotFound() {
        long taskId = 99L;
        mockServer.expect(requestTo(TODOS_URL + taskId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        AdditionalTaskInfo additionalTaskInfo = externalServiceAdapter.getAdditionalTaskInfo(taskId);

        assertThat(additionalTaskInfo).isNull();
    }

    @Test
    void shouldNotGetAdditionalInfoWhenUserNotFound() {
        long taskId = 99L;
        mockServer.expect(requestTo(TODOS_URL + taskId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(TODO_JSON, MediaType.APPLICATION_JSON));

        int userId = 5;
        mockServer.expect(requestTo(USERS_URL + userId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        AdditionalTaskInfo additionalTaskInfo = externalServiceAdapter.getAdditionalTaskInfo(taskId);

        assertThat(additionalTaskInfo).isNull();
    }


}