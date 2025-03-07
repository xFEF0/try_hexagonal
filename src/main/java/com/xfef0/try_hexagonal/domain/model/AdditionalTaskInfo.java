package com.xfef0.try_hexagonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdditionalTaskInfo {

    private final Long userId;
    private final String userName;
    private final String userEmail;

}
