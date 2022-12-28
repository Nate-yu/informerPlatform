package cn.edu.hut.controller;


import cn.edu.hut.entity.User;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.hut.common.Result;

import cn.edu.hut.service.ICourseService;
import cn.edu.hut.entity.Course;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        courseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(courseService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String weekDay,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String classroom,
                           @RequestParam(defaultValue = "") String lesson) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (!"".equals(weekDay)) {
            queryWrapper.like("week_day", weekDay);
        }
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(classroom)) {
            queryWrapper.like("classroom", classroom);
        }
        if (!"".equals(lesson)) {
            queryWrapper.like("lesson", lesson);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(courseService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Course> courses = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Course course = new Course();
            course.setWeekDay(row.get(1).toString());
            course.setLesson(row.get(2).toString());
            course.setName(row.get(3).toString());
            course.setTeacherName(row.get(4).toString());
            course.setTeacherTitle(row.get(5).toString());
            course.setClassroom(row.get(6).toString());
            course.setNumbers(Integer.valueOf(row.get(7).toString()));
            course.setClasses(row.get(8).toString());
            course.setCollege(row.get(9).toString());
            courses.add(course);
        }
        courseService.saveBatch(courses);
        return true;
    }

    /**
     * 导出接口
     *
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Course> list = courseService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("weekDay", "上课日期");
        writer.addHeaderAlias("lesson", "课程节次");
        writer.addHeaderAlias("name", "课程名称");
        writer.addHeaderAlias("teacherName", "教师姓名");
        writer.addHeaderAlias("teacherTitle", "教师职称");
        writer.addHeaderAlias("classroom", "上课教室");
        writer.addHeaderAlias("numbers", "应到人数");
        writer.addHeaderAlias("classes", "上课班级");
        writer.addHeaderAlias("college", "开课学院");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("课程信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
}

