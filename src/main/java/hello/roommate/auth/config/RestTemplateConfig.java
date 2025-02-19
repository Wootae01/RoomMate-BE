package hello.roommate.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


//RestTemplate 빈으로 등록 : RestTemplate : 서버로 Header&Body 보내고 응답받을 때 사용
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

