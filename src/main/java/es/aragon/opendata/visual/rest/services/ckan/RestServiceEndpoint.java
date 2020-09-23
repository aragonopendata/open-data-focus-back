package es.aragon.opendata.visual.rest.services.ckan;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import org.springframework.stereotype.Service;

@Service
@Path("/ckan")
public abstract interface RestServiceEndpoint {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/packageInfo")
	public abstract Response packageInfo(JsonObject input);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/packageResource")
	public abstract Response packageResource(JsonObject input);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/packageList")
	public abstract Response packageList();
}