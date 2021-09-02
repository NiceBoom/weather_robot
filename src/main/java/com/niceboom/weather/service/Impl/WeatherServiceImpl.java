package com.niceboom.weather.service.Impl;


import com.niceboom.weather.enity.Forecast;
import com.niceboom.weather.enity.JsonData;
import com.niceboom.weather.enity.StatusCode;
import com.niceboom.weather.enity.WeatherResult;
import com.niceboom.weather.service.WeatherService;
import com.niceboom.weather.utils.JedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

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

    /**
     * 根据日期获取天气
     * @param cityId 城市id
     * @param dateCode 日期代码
     * @return
     */
    @Override
    public GetWeatherDescriptionOutputDto getWeatherDescriptionOutputDto(String cityId, Integer dateCode)  {
        //拼接请求URL
        String cityUrl = WeatherServiceImpl.SOJSON_WEATHER_URL + cityId;
        //创建返回结果集
        GetWeatherDescriptionOutputDto resultWeather = new GetWeatherDescriptionOutputDto();
        //获取当天时间yyyyMMddHH
        //TODO 此处时间格式待定
        String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        //结果集存入当前时间以及查询的城市
        resultWeather.setDate(timeStamp);
        resultWeather.setCityId(cityId);

        try {
            RestTemplate restTemplate = new RestTemplate();
//            //获得查询的所有天气String数据
//            String weatherJsonstr = restTemplate.getForObject(cityUrl, String.class);
//            System.out.println("这里是查询后返回的天气数据:" + weatherJsonstr);

            //直接到Redis中拿数据
            String weatherJsonstr = getWeatherFromRedis(cityId);
            //没拿到数据，就刷新缓存后再次获取天气数据
            if(weatherJsonstr.equals("")) {
                refreshAllWeather(cityId);
            }
            weatherJsonstr = getWeatherFromRedis(cityId);
            //获取不到则直接返回错误信息
            if(weatherJsonstr.equals("")) {
                return resultWeather;
            }
            //将获取的String对象转换为DTO对象
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

            if (dateCode == StatusCode.TODAY_WEATHER) {
                resultWeather.setWeatherDescription(todayWeatherMap);
                //返回当天天气
                return resultWeather;
            }
            if (dateCode == StatusCode.TOMORROW_WEATHER) {
                resultWeather.setWeatherDescription(tomorrowWeatherMap);
                //返回明天天气
                return resultWeather;
            }
            if (dateCode == StatusCode.NOW_WEATHER) {
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

    /**
     *  从redis中获取天气信息
     * @param cityId 城市代码
     * @return 天气信息json字符串
     */
    public String getWeatherFromRedis(String cityId){
        //获取当天时间yyyyMMddHH
        //TODO 此处时间格式待定
        String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
        //获取jedis连接
        Jedis jedisResource = JedisUtil.getResource();
        //截取当前小时数
        String nowHourStr = timeStamp.substring(timeStamp.length() - 2, timeStamp.length());
        int nowHourInt = Integer.parseInt(nowHourStr);
        //处理获取的时间，截掉小时数，以便统一存入Redis的key
        String redisKeyBefore = timeStamp.substring(0,timeStamp.length()-2);
        //初始化天气信息
        String weatherJsonstr = "";
        //0点-4点从redis获取天气
        if (nowHourInt >=0 && nowHourInt < 4) {
            weatherJsonstr = jedisResource.get("xxx_weather_" + redisKeyBefore + "00");
        }

        //4点-9点从redis获取天气
        if (nowHourInt >=4 && nowHourInt < 9) {
            weatherJsonstr = jedisResource.get("xxx_weather_" + redisKeyBefore + "04");
        }

        //9点-13点从redis获取天气
        if (nowHourInt >= 9 && nowHourInt <13) {
            weatherJsonstr = jedisResource.get("xxx_weather_" + redisKeyBefore + "09");
        }

        //13点-19点从redis获取天气
        if (nowHourInt >=13 && nowHourInt < 19) {
            weatherJsonstr = jedisResource.get("xxx_weather_" + redisKeyBefore + "13");
        }

        //19点-24点从redis获取天气
        if (nowHourInt >=19 && nowHourInt < 24) {
            weatherJsonstr = jedisResource.get("xxx_weather_" + redisKeyBefore + "19");
        }
        //关闭jedis连接池
        jedisResource.close();
        return weatherJsonstr;
    }

    public GetWeatherDescriptionOutputDto refreshAllWeather(String cityId){
        //拼接请求URL
        String cityUrl = WeatherServiceImpl.SOJSON_WEATHER_URL + cityId;
        //创建返回结果集
        GetWeatherDescriptionOutputDto resultWeather = new GetWeatherDescriptionOutputDto();
        //获取当天时间yyyyMMddHH
        //TODO 此处时间格式待定
        String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        //结果集存入当前时间以及查询的城市
        resultWeather.setDate(timeStamp);
        resultWeather.setCityId(cityId);
        Map<String, String> resultWeatherDescriptionMap = new HashMap();
        //获取最新查询的所有天气String数据
        RestTemplate restTemplate = new RestTemplate();
        String weatherJsonstr = restTemplate.getForObject(cityUrl, String.class);
        System.out.println("这里是查询后返回的天气数据:" + weatherJsonstr);
        //获取jedis连接
        Jedis jedisResource = JedisUtil.getResource();
        //截取当前小时数
        String nowHourStr = timeStamp.substring(timeStamp.length() - 2, timeStamp.length());
        int nowHourInt = Integer.parseInt(nowHourStr);
        //判断更新时间存入redis并设置其过期时间，过期时间延迟为一小时
        //处理获取的时间，截掉小时数，以便统一存入Redis的key
        String redisKeyBefore = timeStamp.substring(0,timeStamp.length()-2);
        //0点-4点更新
        if (nowHourInt >=0 && nowHourInt < 4) {
            jedisResource.set("xxx_weather_" + redisKeyBefore + "00", weatherJsonstr);
            jedisResource.expire("xxx_weather_" + redisKeyBefore + "00", 60 * 60 * (4-nowHourInt));
        }
        //4点-9点更新
        if (nowHourInt >=4 && nowHourInt < 9) {
            //存入redis并设置过期时间
            jedisResource.set("xxx_weather_" + redisKeyBefore + "04", weatherJsonstr);
            jedisResource.expire("xxx_weather_" + redisKeyBefore + "04", 60 * 60 * (9 - nowHourInt));
        }
        //9点-13点更新
        if (nowHourInt >= 9 && nowHourInt <13) {
            //存入redis并设置过期时间
            jedisResource.set("xxx_weather_" + redisKeyBefore + "09", weatherJsonstr);
            jedisResource.expire("xxx_weather_" + redisKeyBefore + "09", 60 * 60 * (13 - nowHourInt));
        }
        //13点-19点更新
        if (nowHourInt >=13 && nowHourInt < 19) {
            //存入redis并设置过期时间
            jedisResource.set("xxx_weather_" + redisKeyBefore + "13", weatherJsonstr);
            jedisResource.expire("xxx_weather_" + redisKeyBefore + "13", 60 * 60 * (19 - nowHourInt));
        }
        //19点-24点更新
        if (nowHourInt >=19 && nowHourInt < 24) {
            //存入redis并设置过期时间
            jedisResource.set("xxx_weather_" + redisKeyBefore + "19", weatherJsonstr);
            jedisResource.expire("xxx_weather_" + redisKeyBefore + "19", 60 * 60 * (19 - nowHourInt));
        }
        else
            return resultWeather;
        //关闭jedis连接池
        jedisResource.close();
        resultWeatherDescriptionMap.put("resultCode", "true");
        resultWeather.setWeatherDescription(resultWeatherDescriptionMap);
        return resultWeather;
    }
}
