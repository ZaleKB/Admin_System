package com.zale.security;

import cn.hutool.core.util.StrUtil;
import com.zale.entity.SysUser;
import com.zale.service.SysUserService;
import com.zale.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token exception");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token expired");
        }

        String username = claim.getSubject();

        SysUser sysUser = sysUserService.getByUsername(username);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }
}
