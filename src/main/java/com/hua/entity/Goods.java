package com.hua.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: SeniorTong丶
 * @Date: 2020/11/5 14:18
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
public class Goods implements Serializable {

    private String goodsId;

    private String goodsName;

    private String goodsPrice;

    private String goodsUrl;

    private String goodsImgUrl;

    private String goodsShop;


}
