package org.choongang.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class InfoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user01@test.org", password = "_aA123456")
    @DisplayName("로그인시 GET /api/v1/member로 접근시 회원정보 조회")
    public void memberInfoTest() throws Exception {
        mockMvc.perform(get("/api/v1/member"))
    }

}
