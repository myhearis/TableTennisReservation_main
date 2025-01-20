package com.atsu.tabletennisreservation.dto;

import com.atsu.tabletennisreservation.pojo.BallTable;

import java.io.Serializable;

//包装类
public class BallTableDto extends BallTable   {
    private String readImgPath;

    public String getReadImgPath() {
        return readImgPath;
    }

    public void setReadImgPath(String readImgPath) {
        this.readImgPath = readImgPath;
    }
    public BallTableDto(){

    }
    public BallTableDto(BallTable ballTable){
        //初始化信息

    }

}
