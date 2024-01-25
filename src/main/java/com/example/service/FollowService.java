package com.example.service;

import com.example.entity.Follow;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.FollowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FollowService extends ServiceImpl<FollowMapper, Follow> {

    @Resource
    private FollowMapper followMapper;

}
