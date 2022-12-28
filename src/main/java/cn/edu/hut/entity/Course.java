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
 * @since 2022-04-20
 */
@Getter
@Setter
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("星期几")
    private String weekDay;

    @ApiModelProperty("课程节次")
    private String lesson;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("教师职称")
    private String teacherTitle;

    @ApiModelProperty("上课教室")
    private String classroom;

    @ApiModelProperty("应到人数")
    private Integer numbers;

    @ApiModelProperty("上课班级")
    private String classes;

    @ApiModelProperty("开课学院")
    private String college;


}
