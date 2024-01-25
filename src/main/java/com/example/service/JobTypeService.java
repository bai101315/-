package com.example.service;

import com.example.entity.JobType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.JobTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobTypeService extends ServiceImpl<JobTypeMapper, JobType> {

    @Resource
    private JobTypeMapper jobTypeMapper;

}
