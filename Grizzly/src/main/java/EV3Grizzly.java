import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class EV3Grizzly {

    public static final String BASE_URI = "http://127.0.0.1:8080/";

    public static void main(String[] args) {
        final ResourceConfig rc = new ResourceConfig().packages("eu.jeroenweijers.ev3.jetty.rs");
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
}
