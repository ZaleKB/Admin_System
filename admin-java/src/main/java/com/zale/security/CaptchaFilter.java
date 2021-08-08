package com.zale.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zale.common.exception.CaptchaException;
import com.zale.common.lang.Const;
import com.zale.security.LoginFailureHandler;
import com.zale.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();

        if("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
            try{
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // when fail handle to
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {

        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("captcha incorrect!");
        }

        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("captcha incorrect!");
        }

        // delete captcha, one time use
        redisUtil.hdel(Const.CAPTCHA_KEY, key);
    }

}
