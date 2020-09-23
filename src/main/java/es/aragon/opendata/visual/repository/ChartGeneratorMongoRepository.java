package es.aragon.opendata.visual.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.aragon.opendata.visual.models.chart.ChartGenerateProcess;

import java.util.List;

public abstract interface ChartGeneratorMongoRepository extends MongoRepository<ChartGenerateProcess, String> {
    public ChartGenerateProcess findBychartDataId(String chartDataId);
    public ChartGenerateProcess findById(String Id);
    public List<ChartGenerateProcess> findByOrderByDateDesc();
    public Long deleteById(String id);
}