package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("follow")
public class Follow extends Model<Follow> {
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
      * 回访人 
      */
    private String folper;

    /**
      * 回访地点 
      */
    private String folloc;

    /**
      * 回访时间 
      */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date foltime;

    /**
      * 回访次数 
      */
    private String folnum;

    /**
      * 客户评价 
      */
    private String evaluate;

    /**
      * 满意程度 
      */
    private String degree;

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