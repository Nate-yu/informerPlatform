package cn.edu.hut.service.impl;

import cn.edu.hut.entity.Course;
import cn.edu.hut.mapper.CourseMapper;
import cn.edu.hut.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-20
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
