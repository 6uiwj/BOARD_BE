package org.choongang.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final HttpServletRequest request;


    /**
     * Authorization: Bearer 토큰값
     * @return
     */
    public String getToken() {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization)) {
            return authorization.substring(7);
        }

        return null;
    }
}
