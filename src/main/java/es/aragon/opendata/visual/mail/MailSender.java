package es.aragon.opendata.visual.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import es.aragon.opendata.visual.histories.HistoryData;
import es.aragon.opendata.visual.mail.MailModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailSender {

    private static final ResourceBundle config = ResourceBundle.getBundle("application");

    private static final Logger log = LogManager.getLogger();

    private MailSender(){}

    public static void sendMail(JavaMailSender emailSender, Configuration freemarkerConfig, MailModel mm) throws IOException, MessagingException, TemplateException  {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate(mm.getMailTemplate());

        helper.setTo(config.getString("mail.to.notification"));
        helper.setFrom("opendata@aragon.es");
        helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(t, new ObjectMapper().convertValue(mm, Map.class)), true);
        helper.setSubject(mm.getMailSubject());
        
        emailSender.send(message);


    }
    
    
    public static void sendUserMail(JavaMailSender emailSender, Configuration freemarkerConfig, MailModel mm)  throws IOException, MessagingException, TemplateException  {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate(mm.getMailTemplate());

        helper.setTo(mm.getUserEmail());
        helper.setFrom("opendata@aragon.es");
        helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(t, new ObjectMapper().convertValue(mm, Map.class)), true);
        helper.setSubject(mm.getMailSubject());

        emailSender.send(message);

    }
}