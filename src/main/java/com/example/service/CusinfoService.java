package com.example.service;

import com.example.entity.Cusinfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CusinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CusinfoService extends ServiceImpl<CusinfoMapper, Cusinfo> {

    @Resource
    private CusinfoMapper cusinfoMapper;

}
