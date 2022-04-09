package com.jiutian.config.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiutian.pojo.vo.User;
import com.jiutian.service.impl.UserServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2022/4/2 20:55
 * @Author: jiutian
 * @Description:
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userService;

    public SecurityUserDetailsService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(
                new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                new BCryptPasswordEncoder().encode(user.getPwd()),
                getGrantedAuthority(user.getRole()));
    }

    public List<GrantedAuthority> getGrantedAuthority(String role) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }
}
