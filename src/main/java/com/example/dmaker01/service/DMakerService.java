package com.example.dmaker01.service;

import com.example.dmaker01.code.StatusCode;
import com.example.dmaker01.dto.*;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.entity.RetiredDeveloper;
import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.repository.DeveloperRepository;
import com.example.dmaker01.repository.RetiredDeveloperRepository;
import com.example.dmaker01.type.DeveloperLevel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return new CreateDeveloper.Response().fromEntity(developer);
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                //stream().map(dev -> DeveloperDto.fromEntity(dev))
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> {
                    throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
                });
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setExperienceYears(request.getExperienceYears());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());

        return DeveloperDetailDto.fromEntity(developer);
    }

    @Transactional
    public RetiredDeveloperDto deleteDeveloper(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

        if (developer.getStatusCode().equals(StatusCode.RETIRED))
            throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);

        developer.setStatusCode(StatusCode.RETIRED);
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .statusCode(developer.getStatusCode())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);

        return RetiredDeveloperDto.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business logic validation
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent(developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }

    private void validateEditDeveloperRequest(
            EditDeveloper.Request request,
            String memberId
    ) {
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
        developerRepository.findByMemberId(memberId)
                .ifPresent(developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }

    private static void validateDeveloperLevel(
            DeveloperLevel developerLevel,
            Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR
                && experienceYears > 4) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}