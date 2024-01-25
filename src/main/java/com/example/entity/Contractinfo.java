package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("contractinfo")
public class Contractinfo extends Model<Contractinfo> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 被服务人姓名 
      */
    private String name;

    /**
      * 服务人姓名 
      */
    private String sername;

    /**
      * 服务地点 
      */
    private String address;

    /**
      * 服务时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date sertime;

    /**
      * 服务时长 
      */
    private String duration;

    /**
      * 服务类型 
      */
    private String type;

    /**
      * 费用 
      */
    private String cost;

    /**
      * 支付状态 
      */
    private String paystatus;

    /**
      * 合同状态 
      */
    private String coststatus;

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