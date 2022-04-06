package com.myschool.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class SimpleWebClientConfiguration  {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    private static final String BASE_URL = "http://localhost:8081/api/v1/scholarshipsystem";
   // private static final Logger logger = LoggerFactory.getLogger(SimpleApiClient.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event){
        //Impresión de los messageConverters registrados
        handlerAdapter.getMessageConverters().stream().forEach(System.out::println);
    }
    @Bean
    public WebClient webClientFromScratch() {

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }
}
