package software.aoc.day10.b;

import software.aoc.day10.Solver;
import software.aoc.day10.SolverFactory;

public class Main {

    public static void main(String[] args) {
        SolverFactory factory = new SolverFactory();
        Solver solver = factory.createSolver("src/main/resources/input");
        System.out.println("Answer: " + solver.solve());
    }
}