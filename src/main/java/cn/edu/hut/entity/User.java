package cn.edu.hut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-17
 */
@Getter
@Setter
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学号")
    private String stuNum;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("学院")
    private String college;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("班级")
    private String classes;

    @ApiModelProperty("学历")
    private String proof;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("QQ号")
    private String qq;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("信息反馈次数")
    private Integer infoSubmit;

    @ApiModelProperty("出勤反馈次数")
    private Integer attSubmit;

}
