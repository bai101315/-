package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.JobType;
import com.example.service.JobTypeService;
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
@RequestMapping("/api/jobType")
public class JobTypeController {
    @Resource
    private JobTypeService jobTypeService;
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
    public Result<?> save(@RequestBody JobType jobType) {
        return Result.success(jobTypeService.save(jobType));
    }

    @PutMapping
    public Result<?> update(@RequestBody JobType jobType) {
        return Result.success(jobTypeService.updateById(jobType));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        jobTypeService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(jobTypeService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(jobTypeService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<JobType> query = Wrappers.<JobType>lambdaQuery().orderByDesc(JobType::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(JobType::getName, name);
        }
        return Result.success(jobTypeService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<JobType> all = jobTypeService.list();
        for (JobType obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("创建时间", obj.getCreatetime());
            row.put("学历", obj.getEducation());
            row.put("时薪", obj.getHourwage());
            row.put("", obj.getId());
            row.put("级别", obj.getLevel());
            row.put("工种", obj.getName());
            row.put("更新时间", obj.getUpdatetime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("基础数据信息", "UTF-8");
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
        List<JobType> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            JobType obj = new JobType();
            obj.setCreatetime(DateUtil.parseDateTime((String) row.get(1)));
            obj.setEducation((String) row.get(2));
            obj.setHourwage((String) row.get(3));
            obj.setLevel((String) row.get(4));
            obj.setName((String) row.get(5));
            obj.setUpdatetime(DateUtil.parseDateTime((String) row.get(6)));

            saveList.add(obj);
        }
        jobTypeService.saveBatch(saveList);
        return Result.success();
    }

}
