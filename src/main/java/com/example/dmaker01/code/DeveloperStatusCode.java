package com.example.dmaker01.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeveloperStatusCode {
    EMPLOYED("고용"),
    RETIRED("퇴직");

    private final String description;
}
