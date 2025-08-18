package com.learn.realworldscenarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RealWorldScenariosApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RealWorldScenariosApplication.class, args);
        System.out.println("Real World Scenarios Application Started... at: "+run.getEnvironment().getProperty("local.server.port"));
    }


}
