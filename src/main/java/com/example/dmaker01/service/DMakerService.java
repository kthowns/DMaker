package com.example.dmaker01.service;

import com.example.dmaker01.code.DeveloperStatusCode;
import com.example.dmaker01.dto.*;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.entity.RetiredDeveloper;
import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.repository.DeveloperRepository;
import com.example.dmaker01.repository.RetiredDeveloperRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        final Developer developer = createDeveloperFromRequest(request);

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findByDeveloperStatusCodeEquals(DeveloperStatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());
        Developer developer = getDeveloperByMemberId(memberId);

        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    @Transactional
    public RetiredDeveloperDto deleteDeveloper(String memberId) {
        Developer developer = validateDeleteDeveloper(memberId);
        retiredDeveloperRepository.save(
                RetiredDeveloper.builder()
                        .developerLevel(developer.getDeveloperLevel())
                        .developerSkillType(developer.getDeveloperSkillType())
                        .name(developer.getName())
                        .memberId(developer.getMemberId())
                        .build()
        );

        developer.setDeveloperStatusCode(DeveloperStatusCode.RETIRED);
        return RetiredDeveloperDto.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business logic validation
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent(developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }

    private Developer getDeveloperByMemberId(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
        if(developer.getDeveloperStatusCode() == DeveloperStatusCode.RETIRED)
            throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
        return developer;
    }

    private Developer validateDeleteDeveloper(String memberId){
        Developer developer = getDeveloperByMemberId(memberId);
        if(developer.getDeveloperStatusCode() != DeveloperStatusCode.EMPLOYED)
            throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);

        return developer;
    }

    private Developer createDeveloperFromRequest(CreateDeveloper.Request request){
        return Developer.builder()
                .age(request.getAge())
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .developerStatusCode(DeveloperStatusCode.EMPLOYED)
                .name(request.getName())
                .memberId(request.getMemberId())
                .experienceYears(request.getExperienceYears())
                .build();
    }
}
