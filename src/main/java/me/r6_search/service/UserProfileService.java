package me.r6_search.service;

import lombok.RequiredArgsConstructor;
import me.r6_search.domain.userrole.UserRole;
import me.r6_search.exception.user.UserProfileNotFound;
import me.r6_search.web.dto.SignUpRequestDto;
import me.r6_search.domain.userprofile.UserProfile;
import me.r6_search.domain.userprofile.UserProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final MailService mailService;
    private static final String RANDOM_CODE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Transactional
    public Long saveUser(SignUpRequestDto requestDto) throws IllegalArgumentException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//        String emailCode = generateEmailCode(18);
        String emailCode = "123";
        UserProfile newUser = UserProfile.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .emailCode(emailCode)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        UserRole userRole = new UserRole();
        userRole.setRoleName("USER");
        newUser.setRoles(Arrays.asList(userRole));

        mailService.sendEmailAuthenticationCode(requestDto.getUsername(), requestDto.getEmail(), emailCode);

        return userProfileRepository.save(newUser).getId();
    }

    @Transactional
    public boolean authenticateEmail(String username, String code) {
        UserProfile userProfile = userProfileRepository.findByUsernameAndEmailAuthenticateCode(username, code);
        if(userProfile != null) {
            userProfile.setEmailAuthenticated(true);
            userProfile.getRoles().add(new UserRole("AUTHENTICATED_USED"));
            return true;
        }
        return false;
    }

    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new UserProfileNotFound("유저를 찾을 수 없습니다."));
    }

    private String generateEmailCode(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append( RANDOM_CODE.charAt( rnd.nextInt(RANDOM_CODE.length()) ) );
        return sb.toString();
    }
}