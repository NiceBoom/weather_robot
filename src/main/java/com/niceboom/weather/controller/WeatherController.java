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
    //@Scheduled(cron = "0 0 4,9,13,19 1/1 * ? ")
    @Scheduled(cron = "0/3 * * * * ?")
    void refreshAllWeather(){

        WeatherService.GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto =
                weatherService.refreshAllWeather(WeatherController.CITY_KEY);

    }

}
