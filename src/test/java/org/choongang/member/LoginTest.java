package org.choongang.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.choongang.commons.rests.JSONData;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.service.MemberSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(properties = "spring.profiles.active=dev")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberSaveService joinService;

    @Autowired
    private ObjectMapper om;

    private RequestJoin form;

    @BeforeEach
    void init() {
        form = new RequestJoin();
        form.setEmail("user02@test.org");
        form.setPassword("_aA123456");
        form.setConfirmPassword(form.getConfirmPassword());
        form.setName("사용자02");
        form.setAgree(true);

        joinService.join(form);

    }
    
    @Test
    @DisplayName("[통합테스트]로그인 테스트")
    void loginTest() throws Exception {
        RequestLogin loginForm = new RequestLogin();
        loginForm.setEmail(form.getEmail());
        loginForm.setPassword(form.getPassword());

        String params = om.writeValueAsString(loginForm);

        String body = mockMvc.perform(post("/api/v1/member/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(params))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF-8"));

        JSONData data = om.readValue(body, JSONData.class);
        String token = (String)data.getData();

        HttpHeaders headers = new HttpHeaders();
        //token = "";
        headers.add("Authorization", "Bearer " + token);
        mockMvc.perform(get("/api/v1/member/admin_only")
                .headers(headers))
                .andDo(print());
    }
}


