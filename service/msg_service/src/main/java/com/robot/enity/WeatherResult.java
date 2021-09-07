package com.robot.enity;


import java.util.List;

/**
 * 返回天气数据第一层DTO
 */
public class WeatherResult {
    private String date;//日期
    private String status;//状态码
    private List<JsonData> data;//天气数据

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JsonData> getData() {
        return data;
    }

    public void setData(List<JsonData> data) {
        this.data = data;
    }
    public void addData(JsonData jsonData){
        data.add(jsonData);
    }
}
