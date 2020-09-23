package es.aragon.opendata.visual.rest.services.virtuoso;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
@Path("/virtuoso")
public abstract interface RestServiceEndpoint {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/packageInfo")
	public abstract Response packageInfo(JsonObject input);
}