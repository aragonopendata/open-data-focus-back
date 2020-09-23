package es.aragon.opendata.visual.mail;

public class MailModel {

    private String historyName;	
    private String chartName;
    private String chartUrl;
    private String chartOrigin;
    private String mailSubject;
    private String mailTemplate;
    private String userEmail;
    private String token;
    private String url;

    
    public String getHistoryName() {
        return historyName;
    }

    public String getChartName() {
        return chartName;
    }

    public String getChartUrl() {
        return chartUrl;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public String getMailTemplate() {
        return mailTemplate;
    }
    
    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public void setMailTemplate(String mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public void setChartOrigin(String chartOrigin) {
        this.chartOrigin = chartOrigin;
    }

    public String getChartOrigin() {
        return chartOrigin;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
    

}