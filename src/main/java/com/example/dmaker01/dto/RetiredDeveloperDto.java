package com.example.dmaker01.dto;

import com.example.dmaker01.code.DeveloperStatusCode;
import com.example.dmaker01.entity.Developer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RetiredDeveloperDto {
    private String name;
    private DeveloperStatusCode developerStatusCode;
    private String memberId;

    public static RetiredDeveloperDto fromEntity(@NonNull Developer developer) {
        return RetiredDeveloperDto.builder()
                .name(developer.getName())
                .developerStatusCode(developer.getDeveloperStatusCode())
                .memberId(developer.getMemberId())
                .build();
    }
}
