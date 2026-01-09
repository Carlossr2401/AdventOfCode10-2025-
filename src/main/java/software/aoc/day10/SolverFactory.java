package software.aoc.day10;

import software.aoc.day10.b.Day10Solver;

public class SolverFactory {
    public Solver createSolver(String inputPath) {
        InstructionReader reader = new ReaderFactory().createFileReader(inputPath);
        return new Day10Solver(reader);
    }
}
