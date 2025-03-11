package com.xfef0.try_hexagonal.application.usecase;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.port.in.GetAdditionalTaskInfoUseCase;
import com.xfef0.try_hexagonal.domain.port.out.ExternalServicePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAdditionalTaskInfoUseCaseImpl implements GetAdditionalTaskInfoUseCase {

    private final ExternalServicePort externalServicePort;

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        return externalServicePort.getAdditionalTaskInfo(id);
    }
}
