package cn.edu.hut.mapper;

import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-17
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set password = #{newPassword} where stu_num = #{account} and password = #{password}")
    int updatePassword(PasswordDTO passwordDTO);

    @Update("update user set att_submit = att_submit + 1 where name = #{informer}")
    int updateAttSubmit(String informer);

    @Update("update user set info_submit = info_submit + 1 where name = #{informer}")
    int updateInfoSubmit(String informer);
}
