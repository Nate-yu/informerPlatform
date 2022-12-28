package cn.edu.hut.service.impl;

import cn.edu.hut.common.Constants;
import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.controller.dto.UserDTO;
import cn.edu.hut.entity.User;
import cn.edu.hut.exceptions.ServiceException;
import cn.edu.hut.mapper.UserMapper;
import cn.edu.hut.service.IUserService;
import cn.edu.hut.utils.TokenUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    private static final Log LOG = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one,userDTO,true);
            // 生成token
            String token = TokenUtils.genUserToken(one.getStuNum(), one.getPassword());
            // 设置token
            userDTO.setToken(token);
            // 从对象获取并设置角色与姓名
            userDTO.setRole(one.getRole());
            userDTO.setName(one.getName());
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO) {
        int update = userMapper.updatePassword(passwordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_num", userDTO.getStuNum());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            // 从数据库查询用户信息
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}
