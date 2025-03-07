package com.xfef0.try_hexagonal.domain.port.in;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;
import com.xfef0.try_hexagonal.domain.model.Task;

public interface GetAdditionalTaskInfoUseCase {

    AdditionalTaskInfo getAdditionalTaskInfo(Long id);
}
