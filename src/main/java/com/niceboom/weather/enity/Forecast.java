package com.niceboom.weather.enity;

import lombok.Data;

import java.util.Date;

/**
 * 返回天气数据第三层DTO
 */
@Data
public class Forecast {
    private String date;//当天日期
    private String high;//最高气温
    private String low;//最低气温
    private String type;//天气类型
    private Date ymd;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    private String notice;//提示信息
}
