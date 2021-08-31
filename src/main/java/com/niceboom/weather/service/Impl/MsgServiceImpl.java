package com.niceboom.weather.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.niceboom.weather.enity.StatusCode;
import com.niceboom.weather.service.MsgService;
import com.niceboom.weather.service.WeatherService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsgServiceImpl implements MsgService {

    //消息URL
    final static String DINGDINGTALK_ROBOT_URL =
            "https://oapi.dingtalk.com/robot/send?";
    //验证TOKEN
    final static String DINGDINGTALK_ROBOT_URL_TOKEN =
            "access_token=ffaabe93a835ff732b8053c0cd54c1e8315a8f906ddc0cc722dad5e833ff281c";


    @Override
    public GetMsgDescriptionOutputDto getMsgDescriptionOutputDto(WeatherService.GetWeatherDescriptionOutputDto weatherDescriptionOutputDto,
                                                                 Integer weatherCode) {
        //初始化变量
        String requestBody = "";
        //初始化返回值
        GetMsgDescriptionOutputDto msgDescriptionOutputDto = new GetMsgDescriptionOutputDto();
        msgDescriptionOutputDto.setCityId(weatherDescriptionOutputDto.getCityId());
        msgDescriptionOutputDto.setDate(weatherDescriptionOutputDto.getDate());
        //取出封装的天气数据
        Map<String, String> weatherDescription = weatherDescriptionOutputDto.getWeatherDescription();
        //如果没有天气数据，则直接返回
        if(weatherDescription.isEmpty()){
            return msgDescriptionOutputDto;
        }

        //使用RestTemplate发送http请求
        RestTemplate restTemplate = new RestTemplate();
        //请求路径拼接
        String url = MsgServiceImpl.DINGDINGTALK_ROBOT_URL +
                MsgServiceImpl.DINGDINGTALK_ROBOT_URL_TOKEN;

        //今天天气消息模板
        if(weatherCode == StatusCode.TODAY_WEATHER) {
            //今天天气消息请求体
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京今天天气" + weatherDescription.get("typeToday")
                            +"，"+weatherDescription.get("highToday")+"，"+ weatherDescription.get("lowToday") + "。" + weatherDescription.get("noticeToday")+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }

        //明天天气消息模板
        if(weatherCode == StatusCode.TOMORROW_WEATHER) {
            //明天天气消息请求体
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京明天天气" + weatherDescription.get("typeTomorrow")
                            +"，"+weatherDescription.get("highTomorrow")+"，"+ weatherDescription.get("lowTomorrow") + "。" + weatherDescription.get("noticeTomorrow")+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }
        //现在天气消息模板
        if(weatherCode == StatusCode.NOW_WEATHER){
            //现在天气消息请求模板
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京当前空气质量" + weatherDescription.get("qualityNow")
                            +"，PM2.5浓度为"+weatherDescription.get("pm25Now")+"，当前湿度为"+ weatherDescription.get("shiduNow") + "，室外温度为"
                            + weatherDescription.get("wenduNow")+"度。"+weatherDescription.get("noticeNow")+"。"+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }
        //json对象
        JSONObject jsonObject = new JSONObject();
        //设置请求头为APPLICATION_JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);//Content-Type

        //请求体，包括请求body与请求头
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        try{
            //发送请求，以String接收返回的数据
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            //解析返回数据
            JSONObject jsTemp = JSONObject.parseObject(responseEntity.getBody());
            System.out.println("这里是发送信息请求返回的消息"+jsTemp.toString());
            msgDescriptionOutputDto.setMsgDescription("发送成功");
            return msgDescriptionOutputDto;
        }catch (Exception e){
            System.out.println(e);
            return msgDescriptionOutputDto;
        }
    }
}
