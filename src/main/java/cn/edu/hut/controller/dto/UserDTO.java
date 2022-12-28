package cn.edu.hut.controller.dto;

import lombok.Data;

/**
 * @author Nate yu
 * @date 2022/5/2 - 下午 11:24
 * @Description TODO 信息员登录数据
 */
@Data
public class UserDTO {
    private String stuNum;
    private String name;
    private String password;
    private String role;
    private String token;
}
