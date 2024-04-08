package org.board.boardproject.dto.security;

import lombok.Getter;
import org.board.boardproject.dto.UserAccountDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo
) implements UserDetails {

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        // GrantedAuthority의 기본 구현체로 타입을 맞춰 반환하기에 map 메소드를 활용
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo
        );
    }

    // principal -> dto로 변경
    public UserAccountDTO toDto() {
        return UserAccountDTO.of(
                username,
                password,
                email,
                nickname,
                memo
        );
    }

    // dto -> 객체로 변환하는 static 메소드
    public static BoardPrincipal from(UserAccountDTO dto) {
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }

    @Override public String getPassword() { return username; }
    @Override public String getUsername() { return password; }

    // 인증이 아닌 권한을 관리하는 메소드
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    // 4가지 기능은 true로 변환해서 사용해도 된다.
    // 유저의 권한 만료인가?
    @Override public boolean isAccountNonExpired() { return true; }
    // 유저의 기능이 잠겼는가 ?
    @Override public boolean isAccountNonLocked() { return true; }
    // 크레덴셜이 만료되었는가 ?
    @Override public boolean isCredentialsNonExpired() { return true; }
    // 활성하된 유저인가?
    @Override public boolean isEnabled() { return true; }


    // Enum 구현
    public enum RoleType {
        USER("ROLE_USER");

        @Getter
        private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}
