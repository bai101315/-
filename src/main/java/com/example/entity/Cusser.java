package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("cusser")
public class Cusser extends Model<Cusser> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 客户姓名 
      */
    private String name;

    /**
      * 预约客户姓名 
      */
    private String reseruser;

    /**
      * 联系电话 
      */
    private String telephone;

    /**
      * 服务地点 
      */
    private String address;

    /**
      * 家政公司 
      */
    private String company;

    /**
      * 上门时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date doortime;

    /**
      * 服务时长 
      */
    private String serhour;

    /**
      * 共计金额 
      */
    private String cost;

    /**
      * 备注 
      */
    private String remark;

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