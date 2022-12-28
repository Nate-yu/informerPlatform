package cn.edu.hut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
 * @since 2022-05-06
 */
@Getter
@Setter
@ApiModel(value = "Attendance对象", description = "")
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("上课日期")
    private LocalDate date;

    @ApiModelProperty("上课节次")
    private String lesson;

    @ApiModelProperty("教室")
    private String classroom;

    @ApiModelProperty("课程")
    private String course;

    @ApiModelProperty("教师")
    private String teacher;

    @ApiModelProperty("班级")
    private String classes;

    @ApiModelProperty("开课学院")
    private String college;

    @ApiModelProperty("应到人数")
    private Integer supposedNum;

    @ApiModelProperty("迟到人数")
    private Integer lateNum;

    @ApiModelProperty("早退人数")
    private Integer exitNum;

    @ApiModelProperty("实到人数")
    private Integer attendNum;

    @ApiModelProperty("提交人")
    private String informer;

    @ApiModelProperty("提交时间")
    private String createTime;


}
