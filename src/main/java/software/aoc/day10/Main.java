package software.aoc.day10;

import java.io.IOException;

public class Main {

    static void main() throws IOException {
        String part = "B"; // Change to "B" for Second Part
        SolverFactory factory = new SolverFactory();
        Solver solver = factory.createSolver(part, "src/main/resources/input");
        System.out.println("Answer " + part + ": " + solver.solve());
    }
}