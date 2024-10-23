package matal.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import matal.user.domain.Role;
import matal.user.domain.User;
import matal.user.domain.repository.UserRepository;
import matal.user.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class UserServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        User user1 = User.builder()
                .email("login@test.com")
                .pwd(passwordEncoder.encode("login"))
                .nickname("login")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user1);
    }

    @Test
    public void testSignUp() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("request@test.com", "test", "test", "request");

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequestDto);

        //then
        mockMvc.perform(post("/api/user/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testLogin() throws Exception {
        //given
        String testId = "login@test.com";
        String testPwd = "login";

        //when & then
        mockMvc.perform(post("/login")
                        .param("username", testId)
                        .param("password", testPwd))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());
    }
}
