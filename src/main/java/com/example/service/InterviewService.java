package com.example.service;

import com.example.entity.Interview;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.InterviewMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InterviewService extends ServiceImpl<InterviewMapper, Interview> {

    @Resource
    private InterviewMapper interviewMapper;

}
