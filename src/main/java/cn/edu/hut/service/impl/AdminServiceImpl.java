package cn.edu.hut.service.impl;

import cn.edu.hut.common.Constants;
import cn.edu.hut.controller.dto.AdminDTO;
import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.entity.Admin;
import cn.edu.hut.entity.User;
import cn.edu.hut.exceptions.ServiceException;
import cn.edu.hut.mapper.AdminMapper;
import cn.edu.hut.service.IAdminService;
import cn.edu.hut.utils.TokenUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-18
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    private static final Log LOG = Log.get();

    @Override
    public AdminDTO login(AdminDTO adminDTO) {
        Admin one = getAdminInfo(adminDTO);
        if (one != null) {
            BeanUtil.copyProperties(one,adminDTO,true);
            // 生成token
            String token = TokenUtils.genAdminToken(one.getName(), one.getPassword());
            // 设置token
            adminDTO.setToken(token);
            // 从对象获取角色并设置
            adminDTO.setRole(one.getRole());
            return adminDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO) {
        int update = adminMapper.updatePassword(passwordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    private Admin getAdminInfo(AdminDTO adminDTO) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", adminDTO.getName());
        queryWrapper.eq("password", adminDTO.getPassword());
        Admin one;
        // 处理异常情况
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}
