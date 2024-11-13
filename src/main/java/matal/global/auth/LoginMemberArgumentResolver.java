package matal.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import matal.global.exception.AuthException;
import matal.global.exception.ResponseCode;
import matal.member.dto.request.AuthMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String SESSION_KEY = "member";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class) &&
                AuthMember.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AuthException(ResponseCode.SESSION_VALUE_EXCEPTION);
        }

        Object authMember = session.getAttribute(SESSION_KEY);
        if (authMember == null) {
            throw new AuthException(ResponseCode.SESSION_AUTH_EXCEPTION);
        }
        return authMember;
    }
}
