package cn.edu.hut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2022-05-02
 */
@Getter
@Setter
@ApiModel(value = "Information对象", description = "")
public class Information implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("信息员")
    private String informer;

    @ApiModelProperty("信息反馈类型")
    private String type;

    @ApiModelProperty("主要内容")
    private String content;

    @ApiModelProperty("处理状态")
    private String state;

    @ApiModelProperty("创建时间")
    private String time;

    @ApiModelProperty("信息处理与回复")
    private String handleReply;


}
