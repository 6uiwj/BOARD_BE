package org.choongang.commons;

import org.choongang.member.service.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        String email = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof MemberInfo) {
            MemberInfo memberInfo = (MemberInfo)authentication.getPrincipal();
            email = memberInfo.getEmail();
        }

        return Optional.ofNullable(email);
    }
}
