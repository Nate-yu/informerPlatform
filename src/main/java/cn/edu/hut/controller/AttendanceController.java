package cn.edu.hut.controller;


import cn.edu.hut.entity.Information;
import cn.edu.hut.mapper.UserMapper;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.hut.common.Result;

import cn.edu.hut.service.IAttendanceService;
import cn.edu.hut.entity.Attendance;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    @Resource
    private UserMapper userMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Attendance attendance) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        attendance.setCreateTime(simpleDateFormat.format(new Date()));
        Integer id = attendance.getId();
        String informer = attendance.getInformer();
        if (id == null) {
            userMapper.updateAttSubmit(informer);
        }
        attendanceService.saveOrUpdate(attendance);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        attendanceService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        attendanceService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(attendanceService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(attendanceService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String course,
                           @RequestParam(defaultValue = "") String classroom,
                           @RequestParam(defaultValue = "") String teacher,
                           @RequestParam(defaultValue = "") String informer,
                           @RequestParam(defaultValue = "") String createTime) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        if (!"".equals(course)) {
            queryWrapper.like("course", course);
        }
        if (!"".equals(classroom)) {
            queryWrapper.like("classroom", classroom);
        }
        if (!"".equals(teacher)) {
            queryWrapper.like("teacher", teacher);
        }
        if (!"".equals(informer)) {
            queryWrapper.like("informer", informer);
        }
        if (!"".equals(createTime)) {
            queryWrapper.like("create_time", createTime);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(attendanceService.page(new Page<>(pageNum, pageSize), queryWrapper));
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
        List<Attendance> list = attendanceService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("date", "上课日期");
        writer.addHeaderAlias("lesson", "上课节次");
        writer.addHeaderAlias("classroom", "教室");
        writer.addHeaderAlias("course", "课程");
        writer.addHeaderAlias("teacher", "教师");
        writer.addHeaderAlias("classes", "班级");
        writer.addHeaderAlias("college", "开课学院");
        writer.addHeaderAlias("supposedNum", "应到人数");
        writer.addHeaderAlias("lateNum", "迟到人数");
        writer.addHeaderAlias("exitNum", "早退人数");
        writer.addHeaderAlias("attendNum", "实到人数");
        writer.addHeaderAlias("informer", "提交人");
        writer.addHeaderAlias("createTime", "提交时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("出勤反馈信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
}

