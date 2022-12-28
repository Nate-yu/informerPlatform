package cn.edu.hut.controller;

import cn.edu.hut.common.Result;
import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.service.IAdminService;
import cn.edu.hut.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.edu.hut.controller.LoginController.isNumeric;


/**
 * @author Nate yu
 * @date 2022/5/3 - 下午 09:28
 * @Description TODO 更改密码控制器
 */
@RestController
@RequestMapping("/password")
public class PasswordController {

    @Resource
    private IUserService userService;

    @Resource
    private IAdminService adminService;

    @PostMapping("/updatePassword")
    public Result password(@RequestBody PasswordDTO passwordDTO) {
        String account = passwordDTO.getAccount();
        // 判读用户身份，进行数据分发
        if (isNumeric(account)) {
            userService.updatePassword(passwordDTO);
        } else {
            adminService.updatePassword(passwordDTO);
        }
        return Result.success();
    }

}
