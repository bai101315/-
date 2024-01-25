package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Interview;
import com.example.service.InterviewService;
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
@RequestMapping("/api/interview")
public class InterviewController {
    @Resource
    private InterviewService interviewService;
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
    public Result<?> save(@RequestBody Interview interview) {
        return Result.success(interviewService.save(interview));
    }

    @PutMapping
    public Result<?> update(@RequestBody Interview interview) {
        return Result.success(interviewService.updateById(interview));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        interviewService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(interviewService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(interviewService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Interview> query = Wrappers.<Interview>lambdaQuery().orderByDesc(Interview::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Interview::getName, name);
        }
        return Result.success(interviewService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Interview> all = interviewService.list();
        for (Interview obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("面试官", obj.getName());
            row.put("面试人", obj.getInterviewee());
            row.put("面试编号", obj.getInterid());
            row.put("面试职位", obj.getJob());
            row.put("面试地点", obj.getAddress());
            row.put("面试成绩", obj.getScore());
            row.put("评价", obj.getAvatar());
            row.put("面试时间", obj.getInterviewtime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("面试信息信息", "UTF-8");
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
        List<Interview> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Interview obj = new Interview();
            obj.setName((String) row.get(1));
            obj.setInterviewee((String) row.get(2));
            obj.setInterid((String) row.get(3));
            obj.setJob((String) row.get(4));
            obj.setAddress((String) row.get(5));
            obj.setScore((String) row.get(6));
            obj.setAvatar((String) row.get(7));
            obj.setInterviewtime(DateUtil.parseDateTime((String) row.get(8)));

            saveList.add(obj);
        }
        interviewService.saveBatch(saveList);
        return Result.success();
    }

}
