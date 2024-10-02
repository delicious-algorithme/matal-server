package matal.member.service;

import matal.member.dto.LoginResponse;
import matal.member.dto.MemberLoginDto;
import matal.member.dto.MemberSignUpRequestDto;


public interface MemberService {
    public LoginResponse login(MemberLoginDto requestDto) throws Exception;

    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception;

    public void logout(String token) throws Exception;

}
