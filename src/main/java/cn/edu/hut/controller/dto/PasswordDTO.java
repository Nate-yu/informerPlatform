package cn.edu.hut.controller.dto;

import lombok.Data;

/**
 * @author Nate yu
 * @date 2022/5/3 - 下午 09:26
 * @Description TODO 修改密码提交的数据
 */
@Data
public class PasswordDTO {
    private String account;
    private String password;
    private String newPassword;
}
