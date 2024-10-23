package matal.user.service;

import lombok.RequiredArgsConstructor;
import matal.user.domain.User;
import matal.user.domain.Role;
import matal.user.domain.repository.UserRepository;
import matal.user.dto.request.SignUpRequestDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean signUp(SignUpRequestDto signUpRequestDto) {

        boolean isExisted = userRepository.existsByEmail(signUpRequestDto.email());
        boolean isPwdCheck = signUpRequestDto.pwd().equals(signUpRequestDto.pwdChek());

        if(!isPwdCheck) {
            throw new IllegalArgumentException("The Passwords do not match");
        }

        if(isExisted) {
            throw new IllegalArgumentException("This email already registered");
        }

        User user = User.builder()
                .email(signUpRequestDto.email())
                .pwd(bCryptPasswordEncoder.encode(signUpRequestDto.pwd()))
                .nickname(signUpRequestDto.nickname())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        return userRepository.existsByEmail(user.getEmail());
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("not found email"));

        return new CustomUserDetails(user);
    }


}