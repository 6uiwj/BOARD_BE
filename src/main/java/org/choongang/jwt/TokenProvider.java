package org.choongang.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.choongang.member.constants.Authority;
import org.choongang.member.service.MemberInfo;
import org.choongang.member.service.MemberInfoService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {

    private MemberInfoService memberInfoService;

    private JwtProperties props;

    private Key key;

    public TokenProvider(MemberInfoService memberInfoService, JwtProperties props) {
        this.memberInfoService = memberInfoService;
        this.props = props;

        byte[] keyBytes = Decoders.BASE64.decode(props.getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // EpochTime
        Date validity = new Date(now + props.getAccessTokenValidityInSeconds() * 1000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        String _authorities = (String)claims.get("auth");
        _authorities = StringUtils.hasText(_authorities) ? _authorities : Authority.USER.name();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(_authorities.split(",")).map(SimpleGrantedAuthority::new).toList();

        MemberInfo memberInfo = (MemberInfo)memberInfoService.loadUserByUsername(claims.getSubject());
        memberInfo.setAuthorities(authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberInfo, token, authorities);

        return authentication;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
