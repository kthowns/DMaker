package com.example.dmaker01.service;

import com.example.dmaker01.dto.CreateDeveloper;
import com.example.dmaker01.dto.DeveloperDetailDto;
import com.example.dmaker01.dto.DeveloperDto;
import com.example.dmaker01.dto.EditDeveloper;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.repository.DeveloperRepository;
import com.example.dmaker01.type.DeveloperLevel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DmakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .memberId(request.getMemberId())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return new CreateDeveloper.Response().fromEntity(developer);
        //return DeveloperDto.fromEntity(developer);
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                //stream().map(dev -> DeveloperDto.fromEntity(dev))
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(()->{
                    throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
                });
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId)
                        .orElseThrow(()-> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setExperienceYears(request.getExperienceYears());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());

        return DeveloperDetailDto.fromEntity(developer);
    }

    @Transactional
    public DeveloperDto deleteDeveloper(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                        .orElseThrow(()-> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
        developerRepository.delete(developer);

        return DeveloperDto.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business logic validation
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
        developerRepository.findByMemberId(request.getMemberId())
                .orElseThrow(()-> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
    }

    private void validateEditDeveloperRequest(
            EditDeveloper.Request request,
            String memberId
    ) {
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
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