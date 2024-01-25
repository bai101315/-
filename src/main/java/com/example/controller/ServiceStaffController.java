package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.ServiceStaff;
import com.example.service.ServiceStaffService;
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
@RequestMapping("/api/serviceStaff")
public class ServiceStaffController {
    @Resource
    private ServiceStaffService serviceStaffService;
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
    public Result<?> save(@RequestBody ServiceStaff serviceStaff) {
        return Result.success(serviceStaffService.save(serviceStaff));
    }

    @PutMapping
    public Result<?> update(@RequestBody ServiceStaff serviceStaff) {
        return Result.success(serviceStaffService.updateById(serviceStaff));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        serviceStaffService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(serviceStaffService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(serviceStaffService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ServiceStaff> query = Wrappers.<ServiceStaff>lambdaQuery().orderByDesc(ServiceStaff::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(ServiceStaff::getName, name);
        }
        return Result.success(serviceStaffService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<ServiceStaff> all = serviceStaffService.list();
        for (ServiceStaff obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("jobNumber", obj.getJobnumber());
            row.put("密码", obj.getPassword());
            row.put("员工姓名", obj.getName());
            row.put("性别", obj.getSex());
            row.put("年龄", obj.getAge());
            row.put("身份证号", obj.getPersonalid());
            row.put("邮箱", obj.getEmail());
            row.put("联系电话", obj.getTelephone());
            row.put("父亲姓名", obj.getFaname());
            row.put("父亲年龄", obj.getFaage());
            row.put("父亲身份证号", obj.getFapersonid());
            row.put("父亲工作单位", obj.getFaworkunit());
            row.put("母亲姓名", obj.getManame());
            row.put("母亲年龄", obj.getMaage());
            row.put("母亲身份证号", obj.getMapersonid());
            row.put("母亲工作单位", obj.getMaworkunit());
            row.put("创建时间", obj.getCreatetime());
            row.put("更新时间", obj.getUpdatetime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("服务人员信息", "UTF-8");
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
        List<ServiceStaff> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            ServiceStaff obj = new ServiceStaff();
            obj.setJobnumber((String) row.get(1));
            obj.setPassword((String) row.get(2));
            obj.setName((String) row.get(3));
            obj.setSex((String) row.get(4));
            obj.setAge((String) row.get(5));
            obj.setPersonalid((String) row.get(6));
            obj.setEmail((String) row.get(7));
            obj.setTelephone((String) row.get(8));
            obj.setFaname((String) row.get(9));
            obj.setFaage((String) row.get(10));
            obj.setFapersonid((String) row.get(11));
            obj.setFaworkunit((String) row.get(12));
            obj.setManame((String) row.get(13));
            obj.setMaage((String) row.get(14));
            obj.setMapersonid((String) row.get(15));
            obj.setMaworkunit((String) row.get(16));
            obj.setCreatetime(DateUtil.parseDateTime((String) row.get(17)));
            obj.setUpdatetime(DateUtil.parseDateTime((String) row.get(18)));

            saveList.add(obj);
        }
        serviceStaffService.saveBatch(saveList);
        return Result.success();
    }

}
