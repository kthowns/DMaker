package com.example.dmaker01.dto;

import com.example.dmaker01.code.DeveloperStatusCode;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DeveloperDetailDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;
    private String memberId;
    private String name;
    private Integer age;
    private DeveloperStatusCode developerStatusCode;

    public static DeveloperDetailDto fromEntity(Developer developer) {
        return DeveloperDetailDto.builder()
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .developerStatusCode(developer.getDeveloperStatusCode())
                .developerLevel(developer.getDeveloperLevel())
                .age(developer.getAge())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .build();
    }
}
