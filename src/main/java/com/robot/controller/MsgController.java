package com.robot.controller;

import com.robot.enity.Result;
import com.robot.enity.StatusCode;
import com.robot.service.MsgService;
import com.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/msg")
public class MsgController {

    final static String CITYKEY = "101010100";//北京城市码

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MsgService msgService;

    //每天下午五点半发送天气提醒
    @Scheduled(cron = "0 30 17 1/1 * ?")
    //@Scheduled(cron = "0/3 * * * * ?")
    void everyDaySendMsg(){
        //TODO 验证查询结果
        //查询现在天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto nowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.NOW_WEATHER);
        //发送现在天气消息
        MsgService.GetMsgDescriptionOutputDto msgNowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(nowWeather, StatusCode.NOW_WEATHER);
        if (msgNowDescriptionOutputDto.getMsgDescription()!= null && msgNowDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgNowDescriptionOutputDto.getDate()+
                    "城市是"+msgNowDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }

        //查询明天天气情况
        WeatherService.getWeatherDescriptionOutputDto tomorrowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.TOMORROW_WEATHER);
        //发送明天天气消息
        MsgService.GetMsgDescriptionOutputDto msgTomorrowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(tomorrowWeather, StatusCode.TOMORROW_WEATHER);
        if (msgTomorrowDescriptionOutputDto.getMsgDescription()!= null && msgTomorrowDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgTomorrowDescriptionOutputDto.getDate()+
                    "城市是"+msgTomorrowDescriptionOutputDto.getCityId());
        else {
            System.out.println("消息发送失败，请稍后再试");
        }
    }

    //每天早上八点半发送天气提醒
    @Scheduled(cron = "0 30 8 * * ?")
    //@Scheduled(cron = "0/3 * * * * ?")
    void sendTime830Weather(){
        //TODO 验证查询结果
        //查询现在天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto nowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.NOW_WEATHER);
        //发送现在天气消息
        MsgService.GetMsgDescriptionOutputDto msgNowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(nowWeather, StatusCode.NOW_WEATHER);
        if (msgNowDescriptionOutputDto.getMsgDescription()!= null && msgNowDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgNowDescriptionOutputDto.getDate()+
                    "城市是"+msgNowDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }

        //TODO 验证查询结果
        //查询今天天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto todayWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.TODAY_WEATHER);
        //发送今天天气消息
        MsgService.GetMsgDescriptionOutputDto msgTodayDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(todayWeather, StatusCode.TODAY_WEATHER);
        if (msgTodayDescriptionOutputDto.getMsgDescription()!= null && msgTodayDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgTodayDescriptionOutputDto.getDate()+
                    "城市是"+msgTodayDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }
    }

    //每天早上9.01发送天气提醒
    @Scheduled(cron = "0 1 9 * * ? ")
    //@Scheduled(cron = "0/3 * * * * ?")
    void sendTime900Weather(){
        //TODO 验证查询结果
        //查询现在天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto nowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.NOW_WEATHER);
        //发送现在天气消息
        MsgService.GetMsgDescriptionOutputDto msgNowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(nowWeather, StatusCode.NOW_WEATHER);
        if (msgNowDescriptionOutputDto.getMsgDescription()!= null && msgNowDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgNowDescriptionOutputDto.getDate()+
                    "城市是"+msgNowDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }

        //TODO 验证查询结果
        //查询今天天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto todayWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.TODAY_WEATHER);
        //发送今天天气消息
        MsgService.GetMsgDescriptionOutputDto msgTodayDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(todayWeather, StatusCode.TODAY_WEATHER);
        if (msgTodayDescriptionOutputDto.getMsgDescription()!= null && msgTodayDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgTodayDescriptionOutputDto.getDate()+
                    "城市是"+msgTodayDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }
    }

    //手动发送今天天气消息
    @GetMapping("/sendTodayWeatherMsg")
    Result<WeatherService.getWeatherDescriptionOutputDto> sendTodayWeatherMsg(){
        //查询今天天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto todayWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.TODAY_WEATHER);
        //发送今天天气消息
        MsgService.GetMsgDescriptionOutputDto msgTodayDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(todayWeather, StatusCode.TODAY_WEATHER);
        if (msgTodayDescriptionOutputDto.getMsgDescription()!= null && msgTodayDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgTodayDescriptionOutputDto.getDate()+
                    "城市是"+msgTodayDescriptionOutputDto.getCityId());
        else{
            System.out.println("天气获取失败，请稍后再试");
        }
        return new Result<>(true, StatusCode.OK, "发送成功", todayWeather);
    }

    //手动发送明天天气
    @GetMapping("/sendTomorrowWeatherMsg")
    Result<WeatherService.getWeatherDescriptionOutputDto> sendTomorrowWeatherMsg(){
        //查询明天天气情况
        WeatherService.getWeatherDescriptionOutputDto tomorrowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.TOMORROW_WEATHER);
        //发送明天天气消息
        MsgService.GetMsgDescriptionOutputDto msgTomorrowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(tomorrowWeather, StatusCode.TOMORROW_WEATHER);
        if (msgTomorrowDescriptionOutputDto.getMsgDescription()!= null && msgTomorrowDescriptionOutputDto.getMsgDescription().length() != 0)
            System.out.println("发送消息成功，日期是="+
                    msgTomorrowDescriptionOutputDto.getDate()+
                    "城市是"+msgTomorrowDescriptionOutputDto.getCityId());

        return new Result<>(true, StatusCode.OK, "发送成功", tomorrowWeather);
    }
    //手动发送现在天气
    @GetMapping("/sendNowWeatherMsg")
    Result<WeatherService.getWeatherDescriptionOutputDto> sendNowWeatherMsg(){
        //查询现在天气情况，并判断是否查询成功
        WeatherService.getWeatherDescriptionOutputDto nowWeather =
                weatherService.getWeatherDescriptionOutputDto(MsgController.CITYKEY, StatusCode.NOW_WEATHER);
        //发送现在天气消息
        MsgService.GetMsgDescriptionOutputDto msgNowDescriptionOutputDto =
                msgService.getMsgDescriptionOutputDto(nowWeather, StatusCode.NOW_WEATHER);

        return new Result<>(true, StatusCode.OK, "发送成功", nowWeather);
    }
}
