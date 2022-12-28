package cn.edu.hut.controller.dto;

import lombok.Data;

/**
 * @author Nate yu
 * @date 2022/5/3 - 上午 12:01
 * @Description TODO 管理员登录数据
 */
@Data
public class AdminDTO {
    private String name;
    private String password;
    private String role;
    private String token;
}
