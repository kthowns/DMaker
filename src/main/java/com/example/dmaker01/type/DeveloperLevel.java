package com.example.dmaker01.type;

import com.example.dmaker01.constant.DMakerConstant;
import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.example.dmaker01.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEAR;
import static com.example.dmaker01.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEAR;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years <= MAX_JUNIOR_EXPERIENCE_YEAR),
    JUNGNIOR("중니어 개발자", years -> years > MAX_JUNIOR_EXPERIENCE_YEAR
            && years < MIN_SENIOR_EXPERIENCE_YEAR),
    SENIOR("시니어 개발자", years -> years >= MIN_SENIOR_EXPERIENCE_YEAR);

    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years){
        if(!validateFunction.apply(years))
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
}
