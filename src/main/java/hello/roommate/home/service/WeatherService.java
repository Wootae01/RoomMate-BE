package hello.roommate.home.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    @Value("${weather.serviceKey}")
    private String serviceKey;

}
