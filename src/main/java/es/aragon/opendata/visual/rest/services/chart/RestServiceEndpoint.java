package es.aragon.opendata.visual.rest.services.chart;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import es.aragon.opendata.visual.models.chart.ChartDataGeneric;
import es.aragon.opendata.visual.models.chart.ChartGenerateProcess;

@Service
@Path("/charts")
public abstract interface RestServiceEndpoint {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public abstract Response getCharts(@DefaultValue("0") @QueryParam("page") Integer page, @DefaultValue("40") @QueryParam("size") Integer sizeOfPages);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/type_chart")
	public abstract Response getChartsType(@DefaultValue("0") @QueryParam("page") Integer page, @DefaultValue("40") @QueryParam("size") Integer sizeOfPages, @DefaultValue("number") @QueryParam("type") String type);

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{chart_id}")
	public abstract Response getChart(@PathParam("chart_id") String chartId);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save_chart")
	public abstract Response saveChart(ChartDataGeneric  input);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save_process")
	public abstract Response saveProcess(ChartGenerateProcess input);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/download_process/{chart_id}")
	public Response downloadProcess(@PathParam("chart_id") String chartId);


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/download_all_process")
	public Response downloadAllProcess();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remove_graph/{idProcess}")
	public Response removeGraph(@PathParam("idProcess") String idProcess);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveTitle/{idProcess}")
	public Response updateGraphTitle(@PathParam("idProcess") String idProcess, @QueryParam("title") String title);
}