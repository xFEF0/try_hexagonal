package com.xfef0.try_hexagonal.infrastructure.config;

import com.xfef0.try_hexagonal.application.service.TaskService;
import com.xfef0.try_hexagonal.application.usecase.*;
import com.xfef0.try_hexagonal.domain.port.in.GetAdditionalTaskInfoUseCase;
import com.xfef0.try_hexagonal.domain.port.out.ExternalServicePort;
import com.xfef0.try_hexagonal.domain.port.out.TaskRepositoryPort;
import com.xfef0.try_hexagonal.infrastructure.adapter.ExternalServiceAdapter;
import com.xfef0.try_hexagonal.infrastructure.adapter.JpaTaskRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public TaskService taskService(TaskRepositoryPort taskRepositoryPort,
                                   GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase) {
        return new TaskService(
                new CreateTaskUseCaseImpl(taskRepositoryPort),
                new DeleteTaskUseCaseImpl(taskRepositoryPort),
                new UpdateTaskUseCaseImpl(taskRepositoryPort),
                new RetrieveTaskUseCaseImpl(taskRepositoryPort),
                getAdditionalTaskInfoUseCase
        );
    }

    @Bean
    public TaskRepositoryPort taskRepositoryPort(JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter) {
        return jpaTaskRepositoryAdapter;
    }

    @Bean
    public GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase(ExternalServicePort externalServicePort) {
        return new GetAdditionalTaskInfoUseCaseImpl(externalServicePort);
    }

    @Bean
    public ExternalServicePort externalServicePort() {
        return new ExternalServiceAdapter(new RestTemplate());
    }
}
