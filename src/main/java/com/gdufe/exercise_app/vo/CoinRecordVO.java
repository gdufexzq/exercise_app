package com.gdufe.exercise_app.vo;

import lombok.Data;

@Data
public class CoinRecordVO {

    //获得时间
    public String date;
    //获得金币的途径
    public String beizhu;
    //所得的运动币数量
    public long coin;


}
