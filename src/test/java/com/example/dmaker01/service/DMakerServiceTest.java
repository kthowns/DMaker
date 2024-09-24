package com.example.dmaker01.service;

import com.example.dmaker01.code.DeveloperStatusCode;
import com.example.dmaker01.dto.CreateDeveloper;
import com.example.dmaker01.dto.DeveloperDetailDto;
import com.example.dmaker01.dto.EditDeveloper;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.repository.DeveloperRepository;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.dmaker01.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.example.dmaker01.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(3)
            .age(25)
            .name("name")
            .developerStatusCode(DeveloperStatusCode.EMPLOYED)
            .memberId("memberId01")
            .build();

    @Test
    void editDeveloperTest_success() {
        EditDeveloper.Request request = getEditDeveloperRequest(defaultDeveloper);
        request.setDeveloperLevel(DeveloperLevel.SENIOR);
        request.setDeveloperSkillType(DeveloperSkillType.FULL_STACK);
        request.setExperienceYears(10);

        DeveloperDetailDto target = DeveloperDetailDto.fromEntity(defaultDeveloper);
        target.setDeveloperLevel(DeveloperLevel.SENIOR);
        target.setDeveloperSkillType(DeveloperSkillType.FULL_STACK);
        target.setExperienceYears(10);
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        //when
        DeveloperDetailDto response = dMakerService.editDeveloper("memberId01", request);
        //then
        assertEquals(target, response);
    }

    @Test
    void getDeveloperDetailTest() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(defaultDeveloper.getDeveloperLevel(), developerDetail.getDeveloperLevel());
        assertEquals(defaultDeveloper.getDeveloperSkillType(), developerDetail.getDeveloperSkillType());
        assertEquals(defaultDeveloper.getExperienceYears(), developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captorDeveloper =
                ArgumentCaptor.forClass(Developer.class);

        //when
        dMakerService.createDeveloper(
                getCreateDeveloperRequest(defaultDeveloper)
        );

        //then
        verify(developerRepository, times(1))
                .save(captorDeveloper.capture());
        Developer savedDeveloper = captorDeveloper.getValue();
        assertEquals(defaultDeveloper, savedDeveloper);
    }

    @Test
    void createDeveloperTest_fail_low_senior() {
        CreateDeveloper.Request request = getCreateDeveloperRequest(defaultDeveloper);
        request.setExperienceYears(8);
        request.setDeveloperLevel(DeveloperLevel.SENIOR);

        //findByMemberId Method is Called before request validation, so we don't need this code.

        //when
        DMakerException e = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(request));
        //then
        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, e.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        CreateDeveloper.Request request = getCreateDeveloperRequest(defaultDeveloper);
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(request));

        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

    private CreateDeveloper.Request getCreateDeveloperRequest(Developer developer) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .age(developer.getAge())
                .build();
    }

    private EditDeveloper.Request getEditDeveloperRequest(Developer developer){
        return EditDeveloper.Request.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .build();
    }
}