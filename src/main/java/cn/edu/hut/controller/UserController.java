package cn.edu.hut.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import cn.edu.hut.service.IUserService;
import cn.edu.hut.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nate-yu
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        if (user.getId() == null) {
            if (user.getPassword() == null) {
                user.setPassword("123456");
            }
            user.setRole("INFORMER");
            String stuNum = user.getStuNum();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stu_num",stuNum);
            User one = userService.getOne(queryWrapper);
            if (one != null) {
                return Result.error("400","该信息员已存在");
            } else {
                userService.save(user);
                return Result.success();
            }
        } else {
            userService.updateById(user);
            return Result.success();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/name/{name}")
    public Result findByUsername(@PathVariable String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String stuNum,
                           @RequestParam(defaultValue = "") String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(stuNum)) {
            queryWrapper.like("stu_num", stuNum);
        }
        if (!"".equals(phone)) {
            queryWrapper.like("phone", phone);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/statistics")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String informer) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(informer)) {
            queryWrapper.like("name", informer);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
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
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setStuNum(row.get(0).toString());
            user.setName(row.get(1).toString());
            user.setPassword(row.get(2).toString());
            user.setSex(row.get(3).toString());
            user.setCollege(row.get(4).toString());
            user.setMajor(row.get(5).toString());
            user.setClasses(row.get(6).toString());
            user.setProof(row.get(7).toString());
            user.setPhone(row.get(8).toString());
            user.setEmail(row.get(9).toString());
            user.setQq(row.get(10).toString());
            user.setAvatarUrl(row.get(11).toString());
            user.setRole(row.get(12).toString());
            users.add(user);
        }

        userService.saveBatch(users);
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
        List<User> list = userService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("stuNum", "学号");
        writer.addHeaderAlias("name", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("college", "学院");
        writer.addHeaderAlias("major", "专业");
        writer.addHeaderAlias("classes", "班级");
        writer.addHeaderAlias("proof", "学历");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("qq", "QQ号");
        writer.addHeaderAlias("avatarUrl", "头像");
        writer.addHeaderAlias("role", "角色");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("信息员信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
}

