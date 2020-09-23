package es.aragon.opendata.visual.histories;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "histories")
public class HistoryData {
	private String title;
	private String email;
	private String token;
	private String urlEmail;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrlEmail() {
		return urlEmail;
	}
	public void setUrlEmail(String urlEmail) {
		this.urlEmail = urlEmail;
	}
	
	
	

	
	
}
