package cn.edu.hut.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.hut.common.Result;

import cn.edu.hut.service.ISemesterService;
import cn.edu.hut.entity.Semester;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-21
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    @Resource
    private ISemesterService semesterService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Semester semester) {
        semesterService.saveOrUpdate(semester);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        semesterService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        semesterService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(semesterService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(semesterService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Semester> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(semesterService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

