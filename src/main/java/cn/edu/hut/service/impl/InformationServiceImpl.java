package cn.edu.hut.service.impl;

import cn.edu.hut.entity.Information;
import cn.edu.hut.mapper.InformationMapper;
import cn.edu.hut.service.IInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-05-02
 */
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements IInformationService {

}
