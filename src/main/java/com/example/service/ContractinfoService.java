package com.example.service;

import com.example.entity.Contractinfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ContractinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContractinfoService extends ServiceImpl<ContractinfoMapper, Contractinfo> {

    @Resource
    private ContractinfoMapper contractinfoMapper;

}
