package com.robot.service;

import lombok.Data;

import java.util.List;
import java.util.Map;

public interface WeatherService {

    /**
     * 创建异常方便定位
     */
    public class WeatherServiceException extends Exception{}
    public class RemoteWeatherApiError extends WeatherServiceException{}

    /**
     * 创建查询天气返回dto
     */
    @Data
    class getWeatherDescriptionOutputDto {
        String cityId;
        //此处时间格式待定
        String date;
        //具体的天气情况
        Map<String , String> weatherDescription;
        //气温变化报告
        List<TemperatureResultNode> weatherTemperature;

    }

    /**
     * 封装的返回前端数据
     * 封装的气温日期
     */
    @Data
    class TemperatureResultNode {
        String temperatureDate;
        Integer temperature;
    }

    /**
     * 查询指定日期的城市天气
     * @param cityId 城市id
     * @param dateCode 日期代码
     * @return 封装的返回结果dto
     */
    getWeatherDescriptionOutputDto getWeatherDescriptionOutputDto(String cityId,
                                                                  Integer dateCode) ;

    /**
     *  刷新天气缓存
     * @param cityId 城市id
     * @return
     */
    getWeatherDescriptionOutputDto refreshAllWeather(String cityId);

}
