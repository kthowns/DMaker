package com.example.dmaker01.service;

import com.example.dmaker01.code.StatusCode;
import com.example.dmaker01.dto.CreateDeveloper;
import com.example.dmaker01.dto.DeveloperDetailDto;
import com.example.dmaker01.entity.Developer;
import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.repository.DeveloperRepository;
import com.example.dmaker01.repository.RetiredDeveloperRepository;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
                        .experienceYears(2)
                        .memberId("memberId12")
                        .name("name")
                        .age(20)
                        .statusCode(StatusCode.EMPLOYED)
                        .build();

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(2)
            .memberId("memberId12")
            .name("name")
            .age(20)
            .build();

    @Test
    public void getDeveloperDetailTest() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(DeveloperLevel.JUNIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, developerDetail.getDeveloperSkillType());
        assertEquals(2, developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captorDeveloper =
                ArgumentCaptor.forClass(Developer.class);
        ArgumentCaptor<String> captorString =
                ArgumentCaptor.forClass(String.class);

        //when
        dMakerService.createDeveloper(defaultCreateRequest);

        //then
        verify(developerRepository, times(1))
                .save(captorDeveloper.capture());
        Developer savedDeveloper = captorDeveloper.getValue();
        assertEquals(DeveloperLevel.JUNIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, savedDeveloper.getDeveloperSkillType());
        assertEquals("memberId12", savedDeveloper.getMemberId());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(defaultCreateRequest));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}