package software.aoc.day10.a;

import java.util.List;

public record Solver(ListOfMachines allMachines) {
    public int solveProblem() {
        int totalPresses = 0;
        for (Machine machine : allMachines.list()) {
            int minPresses = solveMachine(machine);
            totalPresses += minPresses;
        }
        return totalPresses;
    }

    private int solveMachine(Machine machine) {
        int numButtons = machine.button().size();
        int numLights = machine.configuration().configuration().size();
        List<Boolean> target = machine.configuration().configuration();

        int minPresses = Integer.MAX_VALUE;

        // Iterate all subsets of buttons ( 1 << numButtons )
        // Using int for mask since numButtons is small (<= ~20)
        int combinations = 1 << numButtons;

        for (int i = 0; i < combinations; i++) {
            int currentPresses = Integer.bitCount(i);
            
            // Optimization: if we already found a better or equal solution, 
            // no need to simulate this one if strict inequality check, 
            // but we want ANY solution with min presses.
            // If currentPresses >= minPresses, we might skip checking if we only care about min.
            // But we must check if it IS a solution.
            if (currentPresses >= minPresses) {
                continue;
            }

            if (check(i, machine, numLights, target)) {
                minPresses = currentPresses;
            }
        }
        return minPresses;
    }

    private boolean check(int mask, Machine machine, int numLights, List<Boolean> target) {
        boolean[] current = new boolean[numLights]; // Defaults to false (off)

        List<Button> buttons = machine.button();
        
        for (int b = 0; b < buttons.size(); b++) {
            if ((mask & (1 << b)) != 0) {
                // Button b is pressed
                Button btn = buttons.get(b);
                for (Integer lightIndex : btn.positions()) {
                    if (lightIndex >= 0 && lightIndex < numLights) {
                        current[lightIndex] = !current[lightIndex];
                    }
                }
            }
        }

        // Compare with target
        for (int k = 0; k < numLights; k++) {
            if (current[k] != target.get(k)) {
                return false;
            }
        }
        return true;
    }
}
