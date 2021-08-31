package com.niceboom.weather.service.Impl;


import com.niceboom.weather.enity.Forecast;
import com.niceboom.weather.enity.JsonData;
import com.niceboom.weather.enity.StatusCode;
import com.niceboom.weather.enity.WeatherResult;
import com.niceboom.weather.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    //请求URL
    final static String SOJSON_WEATHER_URL =
            "http://t.weather.itboy.net/api/weather/city/";

    @Override
    public GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto(String cityId, Integer dateCode)  {
        return null;
    }

    /**
     * 根据日期获取天气
     * @param city_id 城市id
     * @param weatherCode 日期代码
     * @return
     */
    public GetWeatherDescriptionOutputDto getWeather(String city_id, Integer weatherCode)  {
        //拼接请求URL
        String cityUrl = WeatherServiceImpl.SOJSON_WEATHER_URL + city_id;
        //创建返回结果集
        GetWeatherDescriptionOutputDto resultWeather = new GetWeatherDescriptionOutputDto();
        //获取当天时间yyyyMMddHH
        //TODO 此处时间格式待定
        String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        //结果集存入当前时间以及查询的城市
        resultWeather.setDate(timeStamp);
        resultWeather.setCityId(city_id);

        try {
            RestTemplate restTemplate = new RestTemplate();
            //获得查询的所有天气String数据
            String weatherJsonstr = restTemplate.getForObject(cityUrl, String.class);
            System.out.println("这里是查询后返回的天气数据:" + weatherJsonstr);
            //将String对象转换为DTO对象
            WeatherResult weatherResult = JSON.parseObject(weatherJsonstr, WeatherResult.class);
            System.out.println("转换后的数据结果：" + weatherResult);
            //获取天气数据列表
            List<JsonData> data1 = weatherResult.getData();
            //获取第二层嵌套数据
            JsonData jsonData = data1.get(0);

            // 获取当前天气具体情况
            String qualityNow = jsonData.getQuality();//当前空气质量
            String pm25Now = jsonData.getPm25();//当前PM25浓度
            String shiduNow = jsonData.getShidu();//当前空气湿度
            String wenduNow = jsonData.getWendu();//当前温度
            String noticeNow = jsonData.getGanmao();//提示
            //将当前天气封装到map中
            Map<String, String> nowWeatherMap = new HashMap<>();
            nowWeatherMap.put("qualityNow", qualityNow);
            nowWeatherMap.put("pm25Now", pm25Now);
            nowWeatherMap.put("shiduNow", shiduNow);
            nowWeatherMap.put("wenduNow", wenduNow);
            nowWeatherMap.put("noticeNow", noticeNow);
            //获取第三层嵌套列表
            List<Forecast> forecast = jsonData.getForecast();
            System.out.println("forecast=" + forecast);


            //获取当天天气详细
            Forecast todayWeather = forecast.get(0);
            Map<String, String> todayWeatherMap = new HashMap<>();
            String highToday = todayWeather.getHigh();//最高气温
            String lowToday = todayWeather.getLow();//最低气温
            String typeToday = todayWeather.getType();//天气情况
            String noticeToday = todayWeather.getNotice();//出行建议
            todayWeatherMap.put("highToday", highToday);
            todayWeatherMap.put("lowToday", lowToday);
            todayWeatherMap.put("typeToday", typeToday);
            todayWeatherMap.put("noticeToday", noticeToday);

            //获取明天天气详细
            Forecast tomorrowWeather = forecast.get(1);
            Map<String, String> tomorrowWeatherMap = new HashMap<>();
            String highTomorrow = tomorrowWeather.getHigh();//最高气温
            String lowTomorrow = tomorrowWeather.getLow();//最低气温
            String typeTomorrow = tomorrowWeather.getType();//天气情况
            String noticeTomorrow = tomorrowWeather.getNotice();//出行建议
            tomorrowWeatherMap.put("highTomorrow", highTomorrow);
            tomorrowWeatherMap.put("lowTomorrow", lowTomorrow);
            tomorrowWeatherMap.put("typeTomorrow", typeTomorrow);
            tomorrowWeatherMap.put("noticeTomorrow", noticeTomorrow);

            if (weatherCode == StatusCode.TODAY_WEATHER) {
                resultWeather.setWeatherDescription(todayWeatherMap);
                //返回当天天气
                return resultWeather;
            }
            if (weatherCode == StatusCode.TOMORROW_WEATHER) {
                resultWeather.setWeatherDescription(tomorrowWeatherMap);
                //返回明天天气
                return null;
            }
            if (weatherCode == StatusCode.NOW_WEATHER) {
                resultWeather.setWeatherDescription(nowWeatherMap);
                //返回当前天气
                return resultWeather;
            }
                return resultWeather;
        } catch (Exception e) {
            System.out.println(e);
            return resultWeather;
        }
    }
}
