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
@RequestMapping("/msg")
public class WeatherController {

    final static String CITYKEY = "101010100";//北京城市码

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MsgService msgService;

    //每天下午五点半发送明天天气提醒
    //@Scheduled(cron = "0 30 17 1/1 * ?")
    //@GetMapping("/sendMsg")
    @Scheduled(cron = "0/3 * * * * ?")
    void everyDaySendMsg(){
            //查询现在天气情况，并判断是否查询成功
            WeatherService.GetWeatherDescriptionOutputDto nowWeather = weatherService
                    .getWeatherDescriptionOutputDto(WeatherController.CITYKEY, StatusCode.NOW_WEATHER);
            //发送消息
            MsgService.GetMsgDescriptionOutputDto msgDescriptionOutputDto = msgService.getMsgDescriptionOutputDto(nowWeather, StatusCode.NOW_WEATHER);
            if (msgDescriptionOutputDto.getMsgDescription()!= null && msgDescriptionOutputDto.getMsgDescription().length() != 0)
                System.out.println("发送消息成功，日期是="+
                        msgDescriptionOutputDto.getDate()+
                        "城市是"+msgDescriptionOutputDto.getCityId());
            System.out.println("查询异常，请稍后再试123543");
        }
    }
