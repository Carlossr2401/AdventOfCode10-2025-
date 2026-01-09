package software.aoc.day10;

import software.aoc.day10.a.Day10PartASolver;
import software.aoc.day10.a.MachinesA;
import software.aoc.day10.b.Day10PartBSolver;
import software.aoc.day10.b.MachinesB;

public class SolverFactory {
    @SuppressWarnings("unchecked")
    public Solver createSolver(String type, String inputPath) {
        InstructionReader<?> reader = new ReaderFactory().createFileReader(type, inputPath);
        
        if ("A".equalsIgnoreCase(type)) {
            return new Day10PartASolver((InstructionReader<MachinesA>) reader);
        } else if ("B".equalsIgnoreCase(type)) {
            return new Day10PartBSolver((InstructionReader<MachinesB>) reader);
        }
        
        throw new IllegalArgumentException("Unknown solver type: " + type);
    }
}
