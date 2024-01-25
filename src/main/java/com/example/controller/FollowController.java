package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Follow;
import com.example.service.FollowService;
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
@RequestMapping("/api/follow")
public class FollowController {
    @Resource
    private FollowService followService;
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
    public Result<?> save(@RequestBody Follow follow) {
        return Result.success(followService.save(follow));
    }

    @PutMapping
    public Result<?> update(@RequestBody Follow follow) {
        return Result.success(followService.updateById(follow));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        followService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(followService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(followService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Follow> query = Wrappers.<Follow>lambdaQuery().orderByDesc(Follow::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Follow::getName, name);
        }
        return Result.success(followService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Follow> all = followService.list();
        for (Follow obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("客户姓名", obj.getName());
            row.put("回访人", obj.getFolper());
            row.put("回访地点", obj.getFolloc());
            row.put("回访时间", obj.getFoltime());
            row.put("回访次数", obj.getFolnum());
            row.put("客户评价", obj.getEvaluate());
            row.put("满意程度", obj.getDegree());
            row.put("创建时间", obj.getCreatetime());
            row.put("更新时间", obj.getUpdatetime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("回访信息信息", "UTF-8");
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
        List<Follow> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Follow obj = new Follow();
            obj.setName((String) row.get(1));
            obj.setFolper((String) row.get(2));
            obj.setFolloc((String) row.get(3));
            obj.setFoltime(DateUtil.parseDateTime((String) row.get(4)));
            obj.setFolnum((String) row.get(5));
            obj.setEvaluate((String) row.get(6));
            obj.setDegree((String) row.get(7));
            obj.setCreatetime(DateUtil.parseDateTime((String) row.get(8)));
            obj.setUpdatetime(DateUtil.parseDateTime((String) row.get(9)));

            saveList.add(obj);
        }
        followService.saveBatch(saveList);
        return Result.success();
    }

}
