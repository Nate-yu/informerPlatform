package cn.edu.hut.controller;

import cn.edu.hut.common.Constants;
import cn.edu.hut.common.Result;
import cn.edu.hut.controller.dto.AdminDTO;
import cn.edu.hut.controller.dto.PersonDTO;
import cn.edu.hut.controller.dto.UserDTO;
import cn.edu.hut.service.IAdminService;
import cn.edu.hut.service.IUserService;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * @author Nate yu
 * @date 2022/4/22 - 下午 02:44
 * @Description TODO 登录控制器
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private IUserService userService;

    @Resource
    private IAdminService adminService;

    /**
     * 登录，输入账号和密码后，进行身份判断
     * @param personDTO 传入账号密码
     * @return 返回登陆状态
     */
    @PostMapping("/login")
    public Result login(@RequestBody PersonDTO personDTO) {
        String account = personDTO.getAccount();
        String password = personDTO.getPassword();

        if (StrUtil.isBlank(account) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"用户名或密码为空！");
        }
        // 身份判断，账号纯数字为信息员，否则为管理员
        if (isNumeric(account)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setStuNum(account);
            userDTO.setPassword(password);
            return Result.success(userService.login(userDTO));
        } else {
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setName(account);
            adminDTO.setPassword(password);
            return Result.success(adminService.login(adminDTO));
        }
    }

    /**
     * 判断字符串是否为纯数字
     * @param str 传入的字符串
     * @return 返回true或false
     */
    public static boolean isNumeric(String str) {
        String flag = "[0-9]*";
        Pattern pattern = Pattern.compile(flag);
        return pattern.matcher(str).matches();
    }
}
