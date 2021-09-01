package com.niceboom.weather.controller;

import com.niceboom.weather.enity.StatusCode;
import com.niceboom.weather.service.MsgService;
import com.niceboom.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if(getWeatherDescriptionOutputDto.getWeatherDescription().isEmpty())
            System.out.println("更新缓存失败，请重新缓存");
        System.out.println("更新缓存成功");
    }

    //运行项目后第0秒、1秒、3秒，缓存三次最新天气数据到Redis中
    @Scheduled(cron = "0,1,3 * * * * ? ")
    void cacheAfterRunAllWeather(){
        //将最新数据缓存到Redis中
        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        //判断是否获取成功
        if(getWeatherDescriptionOutputDto.getWeatherDescription().isEmpty())
            System.out.println("初始化天气缓存失败，请重新缓存");
        System.out.println("初始化天气缓存成功");
    }

    @GetMapping("/manualRefreshAllWeather")
    void manualRefreshAllWeather(){
        //将最新数据缓存到Redis中
        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        //判断是否获取成功
        if(getWeatherDescriptionOutputDto.getWeatherDescription().isEmpty())
            System.out.println("更新缓存失败，请重新缓存");
        System.out.println("更新缓存成功");
    }
}
