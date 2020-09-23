package es.aragon.opendata.visual.rest.services.history;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import es.aragon.opendata.visual.histories.HistoryData;


@Service
@Path("/histories") 
public interface RestServiceEndpoint {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send_save_admin_mail")
	public abstract Response sendSaveAdminMailHistory(HistoryData historyData);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send_save_user_mail")
	public abstract Response sendSaveUserMailHistory(HistoryData historyData);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send_public_user_mail")
	public abstract Response sendPublicUserMailHistory(HistoryData historyData);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send_return_borrador_user_mail")
	public abstract Response sendReturnToBorradorMailHistory(HistoryData historyData);

}
