package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.core.auth.enums.AdminFlag;
import com.xaaef.molly.core.auth.enums.StatusEnum;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.perms.mapper.PmsUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Slf4j
@Service
@AllArgsConstructor
public class PmsUserDetailsService implements UserDetailsService {


    private final PmsUserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var wrapper = new LambdaQueryWrapper<PmsUser>()
                .eq(PmsUser::getUsername, username);
        var dbUser = userMapper.selectOne(wrapper);
        if (dbUser == null) {
            throw new UsernameNotFoundException(
                    StrUtil.format("用户名 {} 不存在！", username)
            );
        }
        var result = new JwtLoginUser();
        BeanUtils.copyProperties(dbUser, result);
        result.setStatus(StatusEnum.get(dbUser.getStatus()));
        result.setAdminFlag(AdminFlag.get(dbUser.getAdminFlag()));
        result.setAuthorities(Set.of());
        return result;
    }


}