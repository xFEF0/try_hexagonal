package com.xfef0.try_hexagonal.domain.port.out;

import com.xfef0.try_hexagonal.domain.model.AdditionalTaskInfo;

public interface ExternalServicePort {

    AdditionalTaskInfo getAdditionalTaskInfo(Long id);
}
