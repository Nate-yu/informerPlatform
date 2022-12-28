package cn.edu.hut.config.interceptor;

import cn.edu.hut.common.Constants;
import cn.edu.hut.entity.Admin;
import cn.edu.hut.entity.User;
import cn.edu.hut.exceptions.ServiceException;
import cn.edu.hut.service.IAdminService;
import cn.edu.hut.service.IUserService;
import cn.edu.hut.service.impl.AdminServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import static cn.edu.hut.controller.LoginController.isNumeric;

/**
 * @author Nate yu
 * @date 2022/5/2 - 下午 11:43
 * @Description TODO
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private IUserService userService;

    @Resource
    private IAdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的用户账户
        String account;
        try {
            account = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        // 根据token中的account查询数据库
        if (isNumeric(account)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("stu_num", account);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
            }

            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            try {
                // 验证token
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
            }
            return true;
        } else {
            QueryWrapper<Admin> queryWrapper = new QueryWrapper();
            queryWrapper.eq("name", account);
            Admin admin = adminService.getOne(queryWrapper);
            if (admin == null) {
                throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
            }

            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPassword())).build();
            try {
                // 验证token
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
            }
            return true;
        }
    }
}


