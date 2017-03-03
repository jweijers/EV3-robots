package eu.jeroenweijers.ev3.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class EV3Boot extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new EV3Boot()
                .configure(new SpringApplicationBuilder(EV3Boot.class))
                .run(args);
    }
}
