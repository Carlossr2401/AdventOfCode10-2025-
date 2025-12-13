package software.aoc.day10.a;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileInstructionReader reader = new FileInstructionReader("src/main/resources/input");
        ListOfMachines allMachines = reader.readAllData();
        Solver solver = new Solver(allMachines);
        System.out.println("Answer: " + solver.solveProblem());
    }
}
