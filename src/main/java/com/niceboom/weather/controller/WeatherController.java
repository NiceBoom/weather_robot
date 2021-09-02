package com.niceboom.weather.controller;

import com.niceboom.weather.enity.Result;
import com.niceboom.weather.enity.StatusCode;
import com.niceboom.weather.service.MsgService;
import com.niceboom.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/weather")
public class WeatherController {

    final static String CITY_KEY = "101010100";//北京城市码

    @Autowired
    private WeatherService weatherService;

    //更新redis天气数据
    //每天4点、9点、13点、19点更新天气缓存
    @Scheduled(cron = "0 0 4,9,13,19 1/1 * ? ")
    //@Scheduled(cron = "0/3 * * * * ?")
    void refreshAllWeather(){
        //将最新数据缓存到Redis中
        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        //判断是否获取成功

    }

    //运行项目后第0秒、1秒、3秒，缓存三次最新天气数据到Redis中
    //@Scheduled(cron = "0,1,3 * * * * ? ")
    void cacheAfterRunAllWeather(){
        //将最新数据缓存到Redis中
        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        //判断是否获取成功
        System.out.println("自动更新缓存成功");
    }

    //手动刷新天气
    @GetMapping("/manualRefreshAllWeather")
    void manualRefreshAllWeather(){
        //将最新数据缓存到Redis中
        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        //判断是否获取成功
        System.out.println("手动更新缓存成功");
    }

    //获取未来天气所有气温进行数据报告
    @GetMapping("/getAllTemperatureReport")
    Result getAllTemperatureReport(){

        //初始化返回数据
        Map<String, Integer> resultMap = new HashMap<>();
        //获取气温数据
        WeatherService.GetWeatherDescriptionOutputDto weatherDescriptionOutputDto =
                weatherService.getWeatherDescriptionOutputDto(WeatherController.CITY_KEY,
                        StatusCode.REPORT_ALL_TEMPERATURE_WEATHER);
        Map<String, String> allTemperature = weatherDescriptionOutputDto.getWeatherDescription();
        //处理气温数据，将String的气温全换成Integer类型
        //TODO for-each 当map为空时会抛异常，所以要先检查map不为空
        for (Map.Entry<String, String> entry : allTemperature.entrySet()) {
            try {
                int intTemperature = Integer.parseInt(entry.getValue());
                resultMap.put(entry.getKey(), intTemperature);
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return new Result(true, StatusCode.OK, "获取成功", resultMap);
    }

}
