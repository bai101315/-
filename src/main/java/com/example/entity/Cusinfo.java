package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("cusinfo")
public class Cusinfo extends Model<Cusinfo> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 用户名 
      */
    private String name;

    /**
      * 性别 
      */
    private String sex;

    /**
      * 年龄 
      */
    private Integer age;

    /**
      * 地址 
      */
    private String address;

    /**
      * 邮箱 
      */
    private String email;

    /**
      * 手机号 
      */
    private String phone;

    /**
      * 创建时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createtime;

    /**
      * 更新时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date updatetime;

}