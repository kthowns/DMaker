package com.example.dmaker01.dto;

import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateDeveloper {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;

        @NotNull
        @Size(min = 3, max = 50, message="memberId size must 3 ~ 50")
        private String memberId;
        @NotNull
        @Size(min = 3, max = 50, message="Name size must be at least 1 letter")
        private String name;
        @Min(value=18, message = "Only 18+ is available")
        private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class Response{
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;
        private String memberId;
        private String name;
        private Integer age;
        public static Response fromEntity(Developer developer){
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .memberId(developer.getMemberId())
                    .name(developer.getName())
                    .age(developer.getAge())
                    .experienceYears(developer.getExperienceYears())
                    .build();
        }

    }
}
