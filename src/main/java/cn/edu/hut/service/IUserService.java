package cn.edu.hut.service;

import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.controller.dto.UserDTO;
import cn.edu.hut.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-17
 */
public interface IUserService extends IService<User> {

    /**
     * 信息员登录方法
     * @param userDTO 信息员对象
     * @return 返回登陆状态
     */
    UserDTO login(UserDTO userDTO);

    void updatePassword(PasswordDTO passwordDTO);
}
