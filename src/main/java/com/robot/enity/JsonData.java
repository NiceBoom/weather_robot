package com.robot.enity;

import java.util.List;

/**
 * 返回天气数据第二层DTO
 */

public class JsonData {
    private String shidu;//湿度
    private String pm25; //pm25
    private String quality;//空气质量
    private String wendu;//当前气温
    private List<Forecast> forecast;//预报列表
    private String ganmao;

    public String getShidu() {
        return shidu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public void addForecast(Forecast forecasts){
        forecast.add(forecasts);
    }
}
