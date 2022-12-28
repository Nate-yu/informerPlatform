package cn.edu.hut.controller.dto;

import lombok.Data;

/**
 * @author Nate yu
 * @date 2022/4/22 - 下午 02:46
 * @Description TODO 前端提交的数据
 */
@Data
public class PersonDTO {

    private String account;
    private String password;
    private String role;
    private String token;
}
