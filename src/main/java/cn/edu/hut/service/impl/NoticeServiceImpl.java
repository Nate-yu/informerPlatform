package cn.edu.hut.service.impl;

import cn.edu.hut.entity.Notice;
import cn.edu.hut.mapper.NoticeMapper;
import cn.edu.hut.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-22
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

}
