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
@ApiModel(value = "Campus对象", description = "")
public class Campus implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("校区ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("子校区")
    private String subCampus;


}
