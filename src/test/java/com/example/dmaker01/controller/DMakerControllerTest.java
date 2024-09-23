package com.example.dmaker01.controller;

import com.example.dmaker01.dto.DeveloperDto;
import com.example.dmaker01.service.DMakerService;
import com.example.dmaker01.type.DeveloperLevel;
import com.example.dmaker01.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    @Test
    void getAllEmployedDevelopersTest() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1").build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId1").build();

        //given
        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        //when
        mockMvc.perform(get("/developers").contentType(contentType))
                //then
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                is(DeveloperSkillType.BACK_END.name()))
                ).andExpect(
                        jsonPath("$.[0].developerLevel",
                                is(DeveloperLevel.JUNIOR.name()))
                ).andExpect(
                        jsonPath("$.[1].developerSkillType",
                                is(DeveloperSkillType.FRONT_END.name()))
                ).andExpect(
                        jsonPath("$.[1].developerLevel",
                                is(DeveloperLevel.SENIOR.name()))
                );
    }
}