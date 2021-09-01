package com.niceboom.weather.service;

import lombok.Data;

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
    class GetWeatherDescriptionOutputDto{
        String cityId;
        //此处时间格式待定
        String date;
        Map<String, String> weatherDescription;
    }

    /**
     * 查询指定日期的城市天气
     * @param cityId 城市id
     * @param dateCode 日期代码
     * @return 封装的返回结果dto
     */
    GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto(String cityId,
                                                                         Integer dateCode) ;

    /**
     *  刷新天气缓存
     * @param cityId 城市id
     * @return
     */
    GetWeatherDescriptionOutputDto refreshAllWeather(String cityId);


}
