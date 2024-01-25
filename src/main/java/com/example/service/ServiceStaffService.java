package com.example.service;

import com.example.entity.ServiceStaff;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ServiceStaffMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ServiceStaffService extends ServiceImpl<ServiceStaffMapper, ServiceStaff> {

    @Resource
    private ServiceStaffMapper serviceStaffMapper;

}
