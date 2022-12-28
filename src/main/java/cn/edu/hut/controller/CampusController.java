package cn.edu.hut.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.hut.common.Result;

import cn.edu.hut.service.ICampusService;
import cn.edu.hut.entity.Campus;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/campus")
public class CampusController {

    @Resource
    private ICampusService campusService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Campus campus) {
        campusService.saveOrUpdate(campus);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        campusService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        campusService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(campusService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(campusService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String subCampus) {
        QueryWrapper<Campus> queryWrapper = new QueryWrapper<>();
        if (!"".equals(subCampus)) {
            queryWrapper.like("sub_campus", subCampus);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(campusService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

