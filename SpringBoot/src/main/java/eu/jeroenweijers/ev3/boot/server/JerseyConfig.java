package eu.jeroenweijers.ev3.boot.server;

import eu.jeroenweijers.ev3.boot.rs.SimpleRestService;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(SimpleRestService.class);
    }

}
