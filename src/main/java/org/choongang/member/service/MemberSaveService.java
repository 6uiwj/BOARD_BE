package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.member.constants.Authority;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSaveService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public void join(RequestJoin form) {

        String hash = encoder.encode(form.getPassword());

        Member member = Member.builder()
                .email(form.getEmail())
                .name(form.getName())
                .password(hash)
                .authority(Authority.USER)
                .lock(false)
                .enable(true)
                .build();

        save(member);
    }

    public void save(Member member) {

        memberRepository.saveAndFlush(member);
    }

}
