package es.aragon.opendata.visual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import es.aragon.opendata.visual.models.chart.ChartDataGeneric;

public abstract interface ChartMongoRepository extends MongoRepository<ChartDataGeneric, String> {
    List<ChartDataGeneric> findByOrderByDateDesc(Pageable pageable);
    
    List<ChartDataGeneric> findByTypeOrderByDateDesc(String type, Pageable pageable);
    
    public ChartDataGeneric findById(String id);

    public Long deleteById(String id);
}