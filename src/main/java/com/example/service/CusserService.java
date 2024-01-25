package com.example.service;

import com.example.entity.Cusser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CusserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CusserService extends ServiceImpl<CusserMapper, Cusser> {

    @Resource
    private CusserMapper cusserMapper;

}
