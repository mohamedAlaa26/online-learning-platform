package org.microservicetwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


//@ComponentScan(basePackages = "org.instructormicro.microservicetwo")
//(scanBasePackages = "org.microservicetwo")
@SpringBootApplication
public class MicroservicetwoApplication{

    public static void main(String[] args) {
        SpringApplication.run(MicroservicetwoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
