package cn.edu.hut.service;

import cn.edu.hut.controller.dto.AdminDTO;
import cn.edu.hut.controller.dto.PasswordDTO;
import cn.edu.hut.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-18
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 管理员登录方法
     * @param adminDTO 管理员对象
     * @return 返回登录状态
     */
    AdminDTO login(AdminDTO adminDTO);

    void updatePassword(PasswordDTO passwordDTO);
}
