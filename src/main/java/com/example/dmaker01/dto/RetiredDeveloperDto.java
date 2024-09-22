package com.example.dmaker01.dto;

import com.example.dmaker01.code.StatusCode;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RetiredDeveloperDto {
    private String name;
    private StatusCode statusCode;
    private String memberId;

    public static RetiredDeveloperDto fromEntity(Developer developer) {
        return RetiredDeveloperDto.builder()
                .name(developer.getName())
                .statusCode(developer.getStatusCode())
                .memberId(developer.getMemberId())
                .build();
    }
}
