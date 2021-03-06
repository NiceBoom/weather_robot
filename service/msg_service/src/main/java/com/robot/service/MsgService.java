package com.robot.service;

import lombok.Data;

public interface MsgService {

    /**
     * 创建发送消息异常方便定位
     */
    public class MsgServiceException extends Exception{}
    public class RemoteServiceApiError extends MsgServiceException{}

    @Data
    class GetMsgDescriptionOutputDto{
        String cityId;
        //todo此处时间格式待定，应与天气Description天气格式一致
        String date;
        String MsgDescription;
    }

    public GetMsgDescriptionOutputDto getMsgDescriptionOutputDto(WeatherService.getWeatherDescriptionOutputDto weatherDescriptionOutputDto,
                                                                 Integer weatherCode);

}
