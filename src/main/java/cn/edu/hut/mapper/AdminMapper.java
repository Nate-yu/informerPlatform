package cn.edu.hut.mapper;

import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-18
 */
public interface AdminMapper extends BaseMapper<Admin> {

    @Update("update admin set password = #{newPassword} where name = #{account} and password = #{password}")
    int updatePassword(PasswordDTO passwordDTO);
}
