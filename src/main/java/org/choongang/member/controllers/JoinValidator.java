package org.choongang.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator {

    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        /**
         * 1. 이메일 중복 여부
         * 2. 비밀번호 복잡성 - 대소문자 각각 1자 이상 + 숫자 + 특수문자
         * 3. 비밀번호, 비밀번호 확인 일치여부
         */

        RequestJoin form = (RequestJoin)target;
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        // 1. 이메일 중복 여부
        if (StringUtils.hasText(email) && repository.exists(email)) {
            errors.rejectValue("email", "Duplicated");
        }

        // 2. 비밀번호 복잡성 - 대소문자 각각 1자 이상 + 숫자 + 특수문자
        if (StringUtils.hasText(password) && (!alphaCheck(password, false)
            || !numberCheck(password) || !specialCharsCheck(password))) {
            errors.rejectValue("password", "Complexity");
        }

        // 3. 비밀번호, 비밀번호 확인 일치여부
        if (StringUtils.hasText(password) && StringUtils.hasText(confirmPassword)
            && !password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "Mismatch");
        }
    }
}
