package com.zale.security;


import com.zale.entity.SysUser;
import com.zale.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("username or password incorrect!");
        }
        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
    }

    public List<GrantedAuthority> getUserAuthority(Long userId){

        // (ROLE_admin),  menu (sys:user:list)
        String authority = sysUserService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
