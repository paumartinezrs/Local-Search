package IA.ProbIA5;

import aima.search.framework.HeuristicFunction;

public class BicingHeuristicFunction implements HeuristicFunction {
    public boolean equals(Object obj) {
        boolean retValue;
        retValue = super.equals(obj);
        return retValue;
    }
    
    public double getHeuristicValue(Object n) {
        BicingBoard estado = (BicingBoard) n;
        return estado.getCoste();
    }
}
