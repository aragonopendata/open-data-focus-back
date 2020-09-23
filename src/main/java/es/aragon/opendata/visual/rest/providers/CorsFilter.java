package es.aragon.opendata.visual.rest.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.springframework.http.HttpHeaders;

@Provider
public class CorsFilter implements ContainerResponseFilter {

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres)
			throws IOException {
		cres.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		cres.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type, accept, authorization");
		cres.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		cres.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		cres.getHeaders().add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1209600");
	}
}