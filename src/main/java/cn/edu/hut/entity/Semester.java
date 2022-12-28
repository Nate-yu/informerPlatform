package cn.edu.hut.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-04-21
 */
@Getter
@Setter
@ApiModel(value = "Semester对象", description = "")
public class Semester implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学年")
    private String year;

    @ApiModelProperty("子学期")
    private String subSemester;

    @ApiModelProperty("开始日期")
    private String createDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("是否为当前学期")
    private String currentSemester;


}
