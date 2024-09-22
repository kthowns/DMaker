package com.example.dmaker01.dto;

import com.example.dmaker01.exception.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DMakerErrorResponse {
    private DMakerErrorCode dMakerErrorCode;
    private String detailMessage;
}
