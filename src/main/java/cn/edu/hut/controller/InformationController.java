package cn.edu.hut.controller;


import cn.edu.hut.entity.User;
import cn.edu.hut.mapper.UserMapper;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.hut.common.Result;

import cn.edu.hut.service.IInformationService;
import cn.edu.hut.entity.Information;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-05-02
 */
@RestController
@RequestMapping("/information")
public class InformationController {

    @Resource
    private IInformationService informationService;

    @Resource
    private UserMapper userMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Information information) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        information.setTime(simpleDateFormat.format(new Date()));
        Integer id = information.getId();
        String informer = information.getInformer();
        if (id == null) {
            userMapper.updateInfoSubmit(informer);
        }
        informationService.saveOrUpdate(information);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        informationService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        informationService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(informationService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(informationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String type,
                           @RequestParam(defaultValue = "") String informer,
                           @RequestParam(defaultValue = "") String time) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        if (!"".equals(type)) {
            queryWrapper.like("type", type);
        }
        if (!"".equals(informer)) {
            queryWrapper.like("informer", informer);
        }
        if (!"".equals(time)) {
            queryWrapper.like("time", time);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(informationService.page(new Page<>(pageNum, pageSize), queryWrapper));
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
        List<Information> list = informationService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("informer", "信息员");
        writer.addHeaderAlias("type", "反馈类型");
        writer.addHeaderAlias("content", "反馈内容");
        writer.addHeaderAlias("state", "处理状态");
        writer.addHeaderAlias("time", "信息提交时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("反馈信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
}

