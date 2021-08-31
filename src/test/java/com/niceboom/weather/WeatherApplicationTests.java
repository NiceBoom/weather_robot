package com.niceboom.weather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SpringBootTest
class WeatherApplicationTests {

    @Test
    void contextLoads() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
    }

}
