package com.tukorea.planding.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.global.config.security.SecurityConfig;
import com.tukorea.planding.global.config.security.jwt.JwtAuthenticationFilter;
import com.tukorea.planding.domain.schedule.controller.PersonalScheduleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {PersonalScheduleController.class, GlobalExceptionController.class},excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})})
class GlobalExceptionControllerTest {



    @InjectMocks
    private GlobalExceptionController globalExceptionController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(globalExceptionController)
                .build();
    }

    @Test
    @DisplayName("존재하지 않는 유저를 찾을시 controlleradvice에서 에러를 감지하는지 테스트")
    @WithMockUser
    void handleIllegalArgumentException() throws Exception {
        // mock 설정
        UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("User not found");
        MyService myService = mock(MyService.class);
        // 예외 던지기
        doThrow(usernameNotFoundException).when(myService).doSomething();

        myService.doSomething();

        mockMvc.perform(post("/api/v1/schedule"))
                .andExpect(status().isBadRequest()) // 400 상태 코드 검증
                .andExpect((ResultMatcher) jsonPath("$.message").value("Username not found")) // 에러 메시지 검증
                .andExpect((ResultMatcher) jsonPath("$.code").value("400"));


    }


    interface MyService {
        void doSomething();
    }
}