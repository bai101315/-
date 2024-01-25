package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("interview")
public class Interview extends Model<Interview> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 面试官 
      */
    private String name;

    /**
      * 面试人 
      */
    private String interviewee;

    /**
      * 面试编号 
      */
    private String interid;

    /**
      * 面试职位 
      */
    private String job;

    /**
      * 面试地点 
      */
    private String address;

    /**
      * 面试成绩 
      */
    private String score;

    /**
      * 评价 
      */
    private String avatar;

    /**
      * 面试时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date interviewtime;

}