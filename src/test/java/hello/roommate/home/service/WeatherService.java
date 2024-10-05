package hello.roommate.home.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStreamReader;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class WeatherService {
    //@Value("${weather.serviceKey}")
    String serviceKey = "ULN30lwfyYIZpVSBxraansSav9cNjXnqN0nqrIYwmbqv8UdwXUSXL3wk74OkRAKFJMP0k5xhuPCbix9lBWwhhQ==";

    @Test
    void request() throws IOException, URISyntaxException {
        WeatherRequest request = new WeatherRequest(serviceKey, 1, 300, "json",
                "20241005", "1700", 68, 106);

        String uriString = UriComponentsBuilder.fromUriString(request.getURI())
                .queryParam("serviceKey", request.getServiceKey())
                .queryParam("numOfRows", request.getNumOfRows())
                .queryParam("pageNo", request.getPageNo())
                .queryParam("base_date", request.getBase_date())
                .queryParam("base_time", request.getBase_time())
                .queryParam("nx", request.getNx())
                .queryParam("ny", request.getNy())
                .queryParam("dataType", request.getDataType())
                .encode()
                .toUriString();
        log.info(uriString);
        URI uri = new URI(uriString);
        RestClient restClient = RestClient.create();
        String response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(String.class);
        log.info(response);
    }
}

