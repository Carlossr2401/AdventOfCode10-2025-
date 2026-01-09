package software.aoc.day10.a;

import software.aoc.day10.Solver;
import software.aoc.day10.SolverFactory;
import java.io.IOException;

public class Main {

    static void main() throws IOException {
        SolverFactory factory = new SolverFactory();
        Solver solver = factory.createSolver("src/main/resources/input");
        System.out.println("Answer: " + solver.solve());
    }
}