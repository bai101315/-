package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("serviceStaff")
public class ServiceStaff extends Model<ServiceStaff> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * jobNumber 
      */
    private String jobnumber;

    /**
      * 密码 
      */
    private String password;

    /**
      * 员工姓名 
      */
    private String name;

    /**
      * 性别 
      */
    private String sex;

    /**
      * 年龄 
      */
    private String age;

    /**
      * 身份证号 
      */
    private String personalid;

    /**
      * 邮箱 
      */
    private String email;

    /**
      * 联系电话 
      */
    private String telephone;

    /**
      * 父亲姓名 
      */
    private String faname;

    /**
      * 父亲年龄 
      */
    private String faage;

    /**
      * 父亲身份证号 
      */
    private String fapersonid;

    /**
      * 父亲工作单位 
      */
    private String faworkunit;

    /**
      * 母亲姓名 
      */
    private String maname;

    /**
      * 母亲年龄 
      */
    private String maage;

    /**
      * 母亲身份证号 
      */
    private String mapersonid;

    /**
      * 母亲工作单位 
      */
    private String maworkunit;

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