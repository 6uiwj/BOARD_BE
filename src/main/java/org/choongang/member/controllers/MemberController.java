package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.exceptions.BadRequestException;
import org.choongang.commons.rests.JSONData;
import org.choongang.member.entities.Member;
import org.choongang.member.service.MemberInfo;
import org.choongang.member.service.MemberLoginService;
import org.choongang.member.service.MemberSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberLoginService loginService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public JSONData info(@AuthenticationPrincipal MemberInfo memberInfo) {
        Member member = memberInfo.getMember();

        return new JSONData(member);
    }

    @PostMapping
    public ResponseEntity join(@Valid @RequestBody RequestJoin form, Errors errors) {

        joinValidator.validate(form, errors);

        errorProcess(errors);

        saveService.join(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/token")
    public JSONData login(@Valid @RequestBody RequestLogin form, Errors errors) {

        errorProcess(errors);

        String token = loginService.login(form);

        return new JSONData(token);
    }

    @GetMapping("/member_only")
    //@PreAuthorize("authenticated()")
    public MemberInfo memberOnlyTest(@AuthenticationPrincipal MemberInfo memberInfo) {
        System.out.println("회원 전용 페이지 접근");

        return memberInfo;
    }

    @GetMapping("/admin_only")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void adminOnlyTest() {
        System.out.println("관리자 전용 페이지 접근");
    }

    private void errorProcess(Errors errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException(errors);
        }
    }
}
