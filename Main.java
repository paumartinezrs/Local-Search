import IA.Bicing.Estaciones;
import IA.ProbIA5.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        int nbicis = 1250;
        int nestaciones = 25;
        int demanda = 1;
        int seed = 1234;
        int nfurgos = 5;
        int heuristic = 1;
        int estadoIni = 1;


        Estaciones e = new Estaciones(nestaciones, nbicis, demanda, seed);
        BicingBoard board = new BicingBoard(e, nbicis, nfurgos, estadoIni, heuristic);

        if (heuristic == 0) {
            BicingHillClimbingSearch(board);
            //BicingSimulatedAnnealingSearch(board);
        } else if (heuristic == 1) {
            BicingHillClimbingSearchDistance(board);
            //BicingSimulatedAnnealingSearchDistance(board);
        }
    }

    private static void BicingHillClimbingSearch(BicingBoard board) {
        System.out.println("\nBicing HillClimbing  -->");
        try {
            long time = System.currentTimeMillis();
            Problem problem =  new Problem(board,
                    new BicingSuccesorFunction(),
                    new BicingGoalTest(),
                    new BicingHeuristicFunction());
            System.out.println("Problem created");
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            System.out.println();
            printActions(agent.getActions());
            BicingBoard newBoard = (BicingBoard) search.getGoalState();
            time = System.currentTimeMillis() - time;
            System.out.println(newBoard.getCoste());
            printInstrumentation(agent.getInstrumentation());
            System.out.println(time + " ms");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void BicingHillClimbingSearchDistance(BicingBoard board) {
        System.out.println("\nBicing HillClimbingDistance  -->");
        try {
            long time = System.currentTimeMillis();
            Problem problem =  new Problem(board,
                    new BicingSuccesorFunction(),
                    new BicingGoalTest(),
                    new BicingHeuristicFunctionDistance());
            System.out.println("Problem created.");
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            System.out.println();
            printActions(agent.getActions());
            BicingBoard newBoard = (BicingBoard) search.getGoalState();
            time = System.currentTimeMillis() - time;
            System.out.println(newBoard.getCoste());
            printInstrumentation(agent.getInstrumentation());
            System.out.println("Tiempo de ejecución: " + time + " milisegundos");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void BicingSimulatedAnnealingSearch(BicingBoard board) {
        System.out.println("\nBicing SimulatedAnnealing  -->");
        try {
            long time = System.currentTimeMillis();
            Problem problem =  new Problem(board,
                    new BicingSuccesorFunctionSA(),
                    new BicingGoalTest(),
                    new BicingHeuristicFunction());
            System.out.println("Problem created");
            Search search =  new SimulatedAnnealingSearch(1000,100,25,0.0001);
            SearchAgent agent = new SearchAgent(problem,search);
            System.out.println();
            printActions(agent.getActions());
            BicingBoard newBoard = (BicingBoard) search.getGoalState();
            time = System.currentTimeMillis() - time;
            System.out.println(newBoard.getCoste());
            printInstrumentation(agent.getInstrumentation());
            System.out.println(time + " ms");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void BicingSimulatedAnnealingSearchDistance(BicingBoard board) {
        System.out.println("\nBicing SimulatedAnnealingDistance  -->");
        try {
            long time = System.currentTimeMillis();
            Problem problem =  new Problem(board,
                    new BicingSuccesorFunctionSA(),
                    new BicingGoalTest(),
                    new BicingHeuristicFunctionDistance());
            System.out.println("Problem created");
            Search search =  new SimulatedAnnealingSearch(25000,100,25,0.01);
            SearchAgent agent = new SearchAgent(problem,search);
            System.out.println();
            printActions(agent.getActions());
            BicingBoard newBoard = (BicingBoard) search.getGoalState();
            time = System.currentTimeMillis() - time;
            System.out.println(newBoard.getCoste());
            printInstrumentation(agent.getInstrumentation());
            System.out.println(time + " ms");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
