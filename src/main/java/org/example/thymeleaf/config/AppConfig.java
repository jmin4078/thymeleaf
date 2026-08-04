/*package org.example.thymeleaf.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {
    public static class MyBean {
        MyBean(String name) {
            System.out.println("MyBean : %s가 등록되었습니다".formatted(name));
        }
    }

    @Bean
    @Profile("dev")
    @Primary
    public MyBean devMyBean() {
        return new MyBean("dev");
    }

    @Bean
    @Profile("dev")
    public MyBean devMyBean2() {
        return new MyBean("dev");
    }

    @Bean
//    public String beanTest(MyBean myBean) {
    // primary가 없다고 가정할 시 prod, dev 모두가 잡힘 -> profile은 중복 활성화가 가능
    // -> 하나를 골르던가(Qualifier) 혹은 기본값(Primary)을 골라야함
    public String beanTest(@Qualifier("devMyBean2") MyBean myBean) {
        return "beanTest";
    }

    @Bean
    @Profile("prod")
    public MyBean prodMyBean() {
        return new MyBean("prod");
    }
}*/