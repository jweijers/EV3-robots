package eu.jeroenweijers.ev3.jetty.rs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Consumes("application/json")
@Produces("application/json")
@Path("/rest")
public interface SimpleRestService {

    @GET
    @Path("/display/{text}")
    void display(@PathParam("text") String text);
}
