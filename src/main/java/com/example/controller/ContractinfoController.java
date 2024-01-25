package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Contractinfo;
import com.example.service.ContractinfoService;
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
@RequestMapping("/api/contractinfo")
public class ContractinfoController {
    @Resource
    private ContractinfoService contractinfoService;
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
    public Result<?> save(@RequestBody Contractinfo contractinfo) {
        return Result.success(contractinfoService.save(contractinfo));
    }

    @PutMapping
    public Result<?> update(@RequestBody Contractinfo contractinfo) {
        return Result.success(contractinfoService.updateById(contractinfo));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        contractinfoService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(contractinfoService.getById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(contractinfoService.list());
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Contractinfo> query = Wrappers.<Contractinfo>lambdaQuery().orderByDesc(Contractinfo::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Contractinfo::getName, name);
        }
        return Result.success(contractinfoService.page(new Page<>(pageNum, pageSize), query));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Contractinfo> all = contractinfoService.list();
        for (Contractinfo obj : all) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("", obj.getId());
            row.put("被服务人姓名", obj.getName());
            row.put("服务人姓名", obj.getSername());
            row.put("服务地点", obj.getAddress());
            row.put("服务时间", obj.getSertime());
            row.put("服务时长", obj.getDuration());
            row.put("服务类型", obj.getType());
            row.put("费用", obj.getCost());
            row.put("支付状态", obj.getPaystatus());
            row.put("合同状态", obj.getCoststatus());
            row.put("创建时间", obj.getCreatetime());
            row.put("更新时间", obj.getUpdatetime());

            list.add(row);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("合同信息信息", "UTF-8");
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
        List<Contractinfo> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Contractinfo obj = new Contractinfo();
            obj.setName((String) row.get(1));
            obj.setSername((String) row.get(2));
            obj.setAddress((String) row.get(3));
            obj.setSertime(DateUtil.parseDateTime((String) row.get(4)));
            obj.setDuration((String) row.get(5));
            obj.setType((String) row.get(6));
            obj.setCost((String) row.get(7));
            obj.setPaystatus((String) row.get(8));
            obj.setCoststatus((String) row.get(9));
            obj.setCreatetime(DateUtil.parseDateTime((String) row.get(10)));
            obj.setUpdatetime(DateUtil.parseDateTime((String) row.get(11)));

            saveList.add(obj);
        }
        contractinfoService.saveBatch(saveList);
        return Result.success();
    }

}
