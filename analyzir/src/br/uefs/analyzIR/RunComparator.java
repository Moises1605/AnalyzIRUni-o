package br.uefs.analyzIR;

import br.uefs.analyzIR.graph.GraphItem;
import br.uefs.analyzIR.measure.data.MeasureSet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RunComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof String){
            String run1 =(String) o1;
            String run2 = (String) o2;
            return run1.compareToIgnoreCase(run2);
        }
        GraphItem item1 = (GraphItem) o1;
        GraphItem item2 = (GraphItem) o2;
        return item1.getName().compareToIgnoreCase(item2.getName());
    }
}
