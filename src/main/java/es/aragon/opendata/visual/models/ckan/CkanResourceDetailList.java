package es.aragon.opendata.visual.models.ckan;

import java.util.ArrayList;
import java.util.List;

public class CkanResourceDetailList {

    private List<CkanResourceDetail> result;

    public CkanResourceDetailList() {
        this.result = new ArrayList<>();
    }

    public List<CkanResourceDetail> getResult() {
        return result;
    }

    public void setResult(List<CkanResourceDetail> result) {
        this.result = result;
    }

    public void add(CkanResourceDetail detail) {
        this.result.add(detail);
    }

}
