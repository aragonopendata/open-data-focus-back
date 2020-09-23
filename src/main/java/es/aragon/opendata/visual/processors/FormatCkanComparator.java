package es.aragon.opendata.visual.processors;

import java.util.Comparator;

import es.aragon.opendata.visual.models.ckan.CkanResource;

public class FormatCkanComparator implements Comparator<CkanResource> {

  private static String[] strNames = {"PX","CSV"};

  @Override
  public int compare(CkanResource objX, CkanResource objY) {
    String x = objX.getFormat();
    String y = objY.getFormat();

    if (x.equals(y)) {
      return 0;
    }

    for (String strCurrentName : strNames) {
      if (strCurrentName.equals(x)) {
        return -1;
      }
      if (strCurrentName.equals(y)) {
        return 1;
      }
    }
    return x.compareTo(y);
  }

}