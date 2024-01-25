package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Cusinfo;
import com.example.service.CusinfoService;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.example.exception.CustomException;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cusinfo")
public class CusinfoController {
    @Resource
    private CusinfoService cusinfoService;
    @Resource
    private HttpServletRequest request;

    public User getUser() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomException("-1", "请登录");
        }
        return user;
    }

    @PostMapping
    public Result<?> save(@RequestBody Cusinfo cusinfo) {
        return Result.success(cusinfoService.save(cusinfo));
    }

    @PutMapping
    public Result<?> update(@RequestBody Cusinfo cusinfo) {
        return Result.success(cusinfoService.updateById(cusinfo));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        cusinfoService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(cusinfoService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(cusinfoService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Cusinfo> query = Wrappers.<Cusinfo>lambdaQuery().orderByDesc(Cusinfo::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Cusinfo::getName, name);
        }
        return Result.success(cusinfoService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Cusinfo> all = cusinfoService.list();
        for (Cusinfo obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("用户名", obj.getName());
            row.put("性别", obj.getSex());
            row.put("年龄", obj.getAge());
            row.put("地址", obj.getAddress());
            row.put("邮箱", obj.getEmail());
            row.put("手机号", obj.getPhone());
            row.put("创建时间", obj.getCreatetime());
            row.put("更新时间", obj.getUpdatetime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("客户信息信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/upload/{fileId}")
    public Result<?> upload(@PathVariable String fileId) {
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String file = fileNames.stream().filter(name -> name.contains(fileId)).findAny().orElse("");
        List<List<Object>> lists = ExcelUtil.getReader(basePath + file).read(1);
        List<Cusinfo> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Cusinfo obj = new Cusinfo();
            obj.setName((String) row.get(1));
            obj.setSex((String) row.get(2));
            obj.setAge(Integer.valueOf((String) row.get(3)));
            obj.setAddress((String) row.get(4));
            obj.setEmail((String) row.get(5));
            obj.setPhone((String) row.get(6));
            obj.setCreatetime(DateUtil.parseDateTime((String) row.get(7)));
            obj.setUpdatetime(DateUtil.parseDateTime((String) row.get(8)));

            saveList.add(obj);
        }
        cusinfoService.saveBatch(saveList);
        return Result.success();
    }

}
