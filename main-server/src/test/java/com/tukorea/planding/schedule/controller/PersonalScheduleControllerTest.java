package com.tukorea.planding.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.domain.schedule.controller.PersonalScheduleController;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.global.config.security.SecurityConfig;
import com.tukorea.planding.global.config.security.jwt.JwtAuthenticationFilter;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.service.PersonalScheduleService;
import com.tukorea.planding.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {PersonalScheduleController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})})
class PersonalScheduleControllerTest {

    @MockBean
    private PersonalScheduleService personalScheduleService;

    @InjectMocks
    private PersonalScheduleController personalScheduleController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createSchedule() throws Exception{
        User user = User.builder()
                .email("test")
                .build();

        PersonalScheduleRequest schedule = PersonalScheduleRequest.builder()
                .title("test")
                .build();

        given(personalScheduleService.createSchedule(UserMapper.toUserInfo(user),schedule)).willThrow(UsernameNotFoundException.class);


        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedule")
                .content(objectMapper.writeValueAsString(schedule))
                .contentType(MediaType.APPLICATION_JSON).with(csrf()));

        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}