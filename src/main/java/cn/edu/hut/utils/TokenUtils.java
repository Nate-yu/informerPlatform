package cn.edu.hut.utils;

import cn.edu.hut.entity.Admin;
import cn.edu.hut.entity.User;
import cn.edu.hut.service.IAdminService;
import cn.edu.hut.service.IUserService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Nate yu
 * @date 2022/5/2 - 下午 08:03
 * @Description TODO jwt登录拦截
 */
@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    private static IAdminService staticAdminService;

    @Resource
    private IAdminService adminService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    @PostConstruct
    public void setAdminService() {
        staticAdminService = adminService;
    }

    /**
     * 生成信息员token
     * @param stuNum 学号
     * @param sign 密码
     * @return
     */
    public static String genUserToken(String stuNum, String sign) {
        // 将用户学号保存到 token 里面,作为载荷
        return JWT.create().withAudience(stuNum)
                // 2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                // 以 password 作为 token 的密钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 生成管理员token
     * @param name 用户名
     * @param sign 密码
     * @return
     */
    public static String genAdminToken(String name, String sign) {
        // 将 用户 id 保存到 token 里面,作为载荷
        return JWT.create().withAudience(name)
                // 2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                // 以 password 作为 token 的密钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return admin对象
     */
    public static Admin getCurrentAdmin() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String adminId = JWT.decode(token).getAudience().get(0);
                return staticAdminService.getById(Integer.valueOf(adminId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
