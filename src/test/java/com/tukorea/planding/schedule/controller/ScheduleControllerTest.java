//package com.tukorea.planding.schedule.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tukorea.planding.domain.schedule.service.ScheduleService;
//import com.tukorea.planding.domain.userCodes.entity.User;
//import com.tukorea.planding.domain.userCodes.mapper.UserMapper;
//import com.tukorea.planding.global.config.security.SecurityConfig;
//import com.tukorea.planding.global.config.security.jwt.JwtAuthenticationFilter;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//@ExtendWith(MockitoExtension.class)
//@MockBean(JpaMetamodelMappingContext.class)
//@WebMvcTest(controllers = {ScheduleController.class}, excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})})
//class ScheduleControllerTest {
//
//    @MockBean
//    private ScheduleService scheduleService;
//
//    @InjectMocks
//    private ScheduleController scheduleController;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    void createSchedule() throws Exception{
//        User userCodes = User.builder()
//                .email("test")
//                .build();
//
//        ScheduleRequest schedule = ScheduleRequest.builder()
//                .title("test")
//                .build();
//
//        given(scheduleService.createSchedule(UserMapper.toUserInfo(userCodes),schedule)).willThrow(UsernameNotFoundException.class);
//
//
//        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedule")
//                .content(objectMapper.writeValueAsString(schedule))
//                .contentType(MediaType.APPLICATION_JSON).with(csrf()));
//
//        actions.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}