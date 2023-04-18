package es.aragon.opendata.visual.rest.services.chart.impl;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;

import es.aragon.opendata.visual.mail.MailModel;
import es.aragon.opendata.visual.mail.MailSender;
import es.aragon.opendata.visual.models.chart.ChartDataGeneric;
import es.aragon.opendata.visual.models.chart.ChartGenerateProcess;
import es.aragon.opendata.visual.models.chart.ChartList;
import es.aragon.opendata.visual.repository.ChartGeneratorMongoRepository;
import es.aragon.opendata.visual.repository.ChartMongoRepository;
import es.aragon.opendata.visual.rest.services.chart.RestServiceEndpoint;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import static org.apache.coyote.http11.Constants.a;

public class RestServiceEndpointImpl implements RestServiceEndpoint {

    @Autowired
    private ChartMongoRepository chartMongoRepository;

    @Autowired
    private ChartGeneratorMongoRepository chartGeneratorMongoRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    private static final Logger log = LogManager.getLogger();
    private static final ResourceBundle config = ResourceBundle.getBundle("application");

    @Override
    public Response getCharts(Integer page, Integer sizeOfPages) {
        log.info("--- Peticion GET Chats ---");
        Pageable pageable = new PageRequest(page, sizeOfPages);

        List<ChartDataGeneric> data = chartMongoRepository.findByOrderByDateDesc(pageable);
        ChartList charts = new ChartList();

        charts.setCharts(data);

        return Response.ok(charts).build();
    }
    
    
    @Override
    public Response getChartsType(Integer page, Integer sizeOfPages, String type) {
        log.info("--- Peticion GET Chats By Type---");
        Pageable pageable = new PageRequest(page, sizeOfPages);

        List<ChartDataGeneric> data = chartMongoRepository.findByTypeOrderByDateDesc(type, pageable);
        ChartList charts = new ChartList();

        charts.setCharts(data);

        return Response.ok(charts).build();
    }

    @Override
    public Response getChart(String chartId) {
        log.info("--- Peticion GET Chart ---");

        return Response.ok(chartMongoRepository.findById(chartId)).build();
    }

    @Override
    public Response saveChart(ChartDataGeneric input) {
        log.info("--- Peticion POST Save Chart ---");

        String id = input.getId();
        chartMongoRepository.save(input);
        if(id == null) {
            try{
            this.sendNewChartMail(input);
            } catch (Exception e){
                log.warn("No se ha podido enviar Mail.");
            }
        }
        return Response.ok(chartMongoRepository.findById(input.getId())).build();
    }

    @Override
    public Response saveProcess(ChartGenerateProcess input) {
        log.info("--- Peticion POST Save Process ---");
        chartGeneratorMongoRepository.save(input);

        return Response.ok("{\"response\": \"saved\"}").build();
    }

    @Override
    public Response downloadProcess(String chartId) {
        log.info("--- Peticion GET Download Process ---");

        return Response.ok(chartGeneratorMongoRepository.findBychartDataId(chartId)).build();
    }

    @Override
    public Response downloadAllProcess() {
        log.info("--- Peticion GET Download Process ---");

        return Response.ok(chartGeneratorMongoRepository.findByOrderByDateDesc()).build();
    }

    @Override
    public Response removeGraph(String idProcess) {
        log.info("--- Peticion DELETE Remove Graph ---");
        ChartGenerateProcess p = chartGeneratorMongoRepository.findById(idProcess);

        try {
            if (p != null) {
                chartMongoRepository.deleteById(p.getChartDataId());
                chartGeneratorMongoRepository.deleteById(p.getId());
            } else {
                p = new ChartGenerateProcess();
            }
        }catch (Exception e){
            p = new ChartGenerateProcess();
        }
        return  Response.ok(p).build();
    }

    @Override
    public Response updateGraphTitle(String idProcess, String title) {
        ChartGenerateProcess p = chartGeneratorMongoRepository.findById(idProcess);
        if(p != null ) {
            ChartDataGeneric d = chartMongoRepository.findById(p.getChartDataId());

            try {
                if (p != null && d != null) {
                    p.setTitle(title);
                    d.setTitle(title);
                    chartGeneratorMongoRepository.save(p);
                    chartMongoRepository.save(d);
                } else {
                    p = new ChartGenerateProcess();
                }
            } catch (Exception e) {
                p = new ChartGenerateProcess();
            }
        }else{
            p = new ChartGenerateProcess();
        }
        return  Response.ok(p).build();
    }

    public void sendNewChartMail(ChartDataGeneric input)  throws IOException, MessagingException, TemplateException {
        log.info("--- Envio correo nueva grafica ---");
        MailModel mm = new MailModel();
        mm.setChartName(input.getTitle());
        mm.setChartUrl(config.getString("visual.data.front.url") + "/charts/embed/" + input.getId());
        mm.setMailSubject("[VisualData] Nueva Gráfica");
        mm.setMailTemplate("mailNewChartTemplate.ftl");
        MailSender.sendMail(emailSender, freemarkerConfig, mm);
    }
}
