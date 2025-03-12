package com.xfef0.try_hexagonal.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xfef0.try_hexagonal.application.service.TaskService;
import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    private static final String BASE_URL = "/api/v1/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldGetAllTasks() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(getTask());
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(tasks.size()))
                .andExpect(jsonPath("$[0].id").value(tasks.getFirst().getId()));
    }


    @Test
    void shouldGetZeroTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = getTask();
        when(taskService.getTask(eq(task.getId()))).thenReturn(Optional.of(task));

        mockMvc.perform(get(BASE_URL + "/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.creationDate").value(getStringDate(task.getCreationDate())))
                .andExpect(jsonPath("$.complete").value(task.isComplete()));
    }

    @Test
    void shouldNotGetTaskById() throws Exception {
        when(taskService.getTask(eq(2L))).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + 2))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTaskAdditionalInfo() throws Exception {
        AdditionalTaskInfo additionalTaskInfo = new AdditionalTaskInfo(3L, "userName", "email@test.com");
        long taskId = 1L;
        when(taskService.getAdditionalTaskInfo(eq(taskId))).thenReturn(additionalTaskInfo);

        mockMvc.perform(get(BASE_URL + "/" + taskId + "/additionalInfo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(additionalTaskInfo.getUserId()))
                .andExpect(jsonPath("$.userName").value(additionalTaskInfo.getUserName()))
                .andExpect(jsonPath("$.userEmail").value(additionalTaskInfo.getUserEmail()));
    }

    @Test
    void shouldNotGetTaskAdditionalInfo() throws Exception {
        long taskId = 99L;
        when(taskService.getAdditionalTaskInfo(eq(taskId))).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/" + taskId + "/additionalInfo"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        Task returnedTask = getTask();
        when(taskService.createTask(any(Task.class))).thenReturn(returnedTask);
        Task task = getTask();
        task.setId(null);
        task.setCreationDate(null);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(returnedTask.getId()))
                .andExpect(jsonPath("$.title").value(returnedTask.getTitle()))
                .andExpect(jsonPath("$.creationDate")
                        .value(getStringDate(returnedTask.getCreationDate())));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = getTask();
        Task updatedTask = getTask();
        updatedTask.setDescription("awesome");
        updatedTask.setComplete(true);
        when(taskService.updateTask(eq(task.getId()), any(Task.class))).thenReturn(Optional.of(updatedTask));

        mockMvc.perform(put(BASE_URL + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedTask.getId()))
                .andExpect(jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedTask.getDescription()))
                .andExpect(jsonPath("$.creationDate").value(getStringDate(updatedTask.getCreationDate())))
                .andExpect(jsonPath("$.complete").value(updatedTask.isComplete()));
    }

    @Test
    void shouldNotUpdateTask() throws Exception {
        long taskId = 99L;
        Task updatedTask = getTask();
        updatedTask.setId(taskId);
        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(Optional.empty());

        mockMvc.perform(put(BASE_URL + "/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        long taskId = 1L;
        when(taskService.deleteTask(eq(taskId))).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/" + taskId))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("Task id = 1 deleted."))
                .andDo(print());
    }

    @Test
    void shouldNotDeleteTask() throws Exception {
        long taskId = 1L;
        when(taskService.deleteTask(eq(taskId))).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/" + taskId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Task id = 1 not found."))
                .andDo(print());
    }

    private static Task getTask() {
        return new Task(
                1L,
                "title",
                "description",
                LocalDateTime.now(),
                false);
    }

    private String getStringDate(LocalDateTime dateTime) {
        String creationDate = dateTime.toString();
        if (creationDate.length() > "2025-03-12T17:22:59.640273".length()) {
            creationDate = creationDate.substring(0, creationDate.length() - 2);
        }
        return creationDate;
    }
}
