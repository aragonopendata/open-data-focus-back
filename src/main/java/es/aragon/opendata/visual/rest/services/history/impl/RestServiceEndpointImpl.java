package es.aragon.opendata.visual.rest.services.history.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import es.aragon.opendata.visual.histories.HistoryData;
import es.aragon.opendata.visual.mail.MailModel;
import es.aragon.opendata.visual.mail.MailSender;
import es.aragon.opendata.visual.rest.services.history.RestServiceEndpoint;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class RestServiceEndpointImpl implements RestServiceEndpoint{
	
    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private Configuration freemarkerConfig;
	
    private static final Logger log = LogManager.getLogger();	
    
    public Response sendSaveAdminMailHistory(HistoryData historyData) {

        log.info("--- Peticion POST sendSaveAdminMailHistory ---");
        try{
        	this.sendSaveAdminHistoryMail(historyData);
        } catch (Exception e){
            log.warn("No se ha podido enviar mail de historia guardada al administrador.");
            return  Response.serverError().build();

        }
        Response responseDevolver=  Response.ok(historyData.getTitle()).build();
        log.info(responseDevolver);
        return responseDevolver;
    }


    public Response sendSaveUserMailHistory(HistoryData historyData) {
        log.info("--- Peticion POST sendSaveUserMailHistory ---");
        try{
        	this.sendSaveUserHistoryMail(historyData);
        } catch (Exception e){
            log.warn("No se ha podido enviar mail de historia guardada al usuario.");
            return  Response.serverError().build();

        }
        Response responseDevolver=  Response.ok(historyData.getTitle()).build();
        log.info(responseDevolver);
        return responseDevolver;
    }
    
    public Response sendPublicUserMailHistory(HistoryData historyData) {
        log.info("--- Peticion POST sendPublicUserMailHistory ---");
        try{
        	this.sendPublicUserHistoryMail(historyData);
        } catch (Exception e){
            log.warn("No se ha podido enviar mail de historia publicada al usuario.");
            return Response.serverError().build();
        }
        Response responseDevolver=  Response.ok(historyData.getTitle()).build();
        log.info(responseDevolver);
        return responseDevolver;
    }
    
    public Response sendReturnToBorradorMailHistory(HistoryData historyData) {
        log.info("--- Peticion POST sendReturnToBorradorMailHistory ---");
        try{
        	this.sendReturnToBorradorHistoryMail(historyData);
        } catch (Exception e){
            log.warn("No se ha podido enviar mail de el cambio de estado de la historia a en borrador.");
            return Response.serverError().build();
        }
        Response responseDevolver=  Response.ok(historyData.getTitle()).build();
        log.info(responseDevolver);
        return responseDevolver;
    }

    public void sendSaveAdminHistoryMail(HistoryData newHistory)  throws IOException, MessagingException, TemplateException  {
        log.info("--- Envio correo admin guardar nueva historia ---");
        MailModel mm = new MailModel();
        mm.setHistoryName(newHistory.getTitle());
        mm.setMailSubject("[Focus] Nueva Historia");
        mm.setMailTemplate("saveHistoryAdminEmail.ftl");
        MailSender.sendMail(emailSender, freemarkerConfig, mm);
    }

    public void sendSaveUserHistoryMail(HistoryData newHistory)  throws IOException, MessagingException, TemplateException  {
        log.info("--- Envio correo user guardar nueva historia---");
        MailModel mm = new MailModel();
        mm.setHistoryName(newHistory.getTitle());
        mm.setToken(newHistory.getToken());
        mm.setUserEmail(newHistory.getEmail());
        mm.setUrl(newHistory.getUrlEmail());
        mm.setMailSubject("[Focus] Nueva Historia");
        mm.setMailTemplate("saveHistoryUserEmail.ftl");
        MailSender.sendUserMail(emailSender, freemarkerConfig, mm);
    }
    
    public void sendPublicUserHistoryMail(HistoryData newHistory)  throws  IOException, MessagingException, TemplateException  {
        log.info("--- Envio correo user publicada su historia---");
        MailModel mm = new MailModel();
        mm.setHistoryName(newHistory.getTitle());
        mm.setToken(newHistory.getToken());
        mm.setUserEmail(newHistory.getEmail());
        mm.setUrl(newHistory.getUrlEmail());
        mm.setMailSubject("[Focus] Historia Publicada");
        mm.setMailTemplate("publicHistoryUserEmail.ftl");
        MailSender.sendUserMail(emailSender, freemarkerConfig, mm);
    }
    
    public void sendReturnToBorradorHistoryMail(HistoryData newHistory)  throws  IOException, MessagingException, TemplateException  {
        log.info("--- Envio correo historia cambiada a estado en borrador---");
        MailModel mm = new MailModel();
        mm.setHistoryName(newHistory.getTitle());
        mm.setUserEmail(newHistory.getEmail());
        mm.setUrl(newHistory.getUrlEmail());
        mm.setMailSubject("[Focus] Historia pendiente de cambios");
        mm.setMailTemplate("returnToBorradorHistoryUserEmail.ftl");
        MailSender.sendUserMail(emailSender, freemarkerConfig, mm);
    }
    
}
