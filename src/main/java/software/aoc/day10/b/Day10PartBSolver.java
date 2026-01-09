package software.aoc.day10.b;

import software.aoc.day10.InstructionReader;
import software.aoc.day10.Solver;
import software.aoc.day10.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10PartBSolver implements Solver {
    private final InstructionReader<MachinesB> reader;

    public Day10PartBSolver(InstructionReader<MachinesB> reader) {
        this.reader = reader;
    }

    @Override
    public Object solve() {
        MachinesB machines = reader.readAllData();
        long totalPresses = 0;
        int i = 0;
        for (Machine machine : machines) {
            // System.out.println("Solving machine " + (++i));
            Long minPresses = solveMachine(machine);
            if (minPresses != null) {
                totalPresses += minPresses;
            } else {
                System.err.println("No solution for machine " + i);
            }
        }
        return totalPresses;
    }

    private Long solveMachine(Machine machine) {
        int numCounters = machine.targetCounters().size();
        int numButtons = machine.buttons().size();

        // Build Matrix A (Rows: counters, Cols: buttons)
        double[][] A = new double[numCounters][numButtons];
        // Upper bounds for each button based on simple constraints
        long[] bounds = new long[numButtons];
        Arrays.fill(bounds, Long.MAX_VALUE);

        for (int j = 0; j < numButtons; j++) {
            Button b = machine.buttons().get(j);
            boolean affectsAny = false;
            for (int counterIdx : b.positions()) {
                if (counterIdx < numCounters) {
                    A[counterIdx][j] = 1.0;
                    long limit = machine.targetCounters().get(counterIdx);
                    bounds[j] = Math.min(bounds[j], limit);
                    affectsAny = true;
                }
            }
            if (!affectsAny) bounds[j] = 0; // Useless button
        }

        double[] t = new double[numCounters];
        for (int k = 0; k < numCounters; k++) {
            t[k] = machine.targetCounters().get(k);
        }

        return new LinearSystemSolver().solve(A, t, bounds);
    }

    private static class LinearSystemSolver {
        private static final double EPSILON = 1e-9;

        public Long solve(double[][] A, double[] t, long[] bounds) {
            int rows = A.length;
            int cols = A[0].length;

            // Gaussian elimination to RREF
            int pivotRow = 0;
            int[] pivotColForRow = new int[rows];
            Arrays.fill(pivotColForRow, -1);
            boolean[] isPivotCol = new boolean[cols];

            for (int col = 0; col < cols && pivotRow < rows; col++) {
                // Find pivot
                int sel = -1;
                for (int row = pivotRow; row < rows; row++) {
                    if (Math.abs(A[row][col]) > EPSILON) {
                        sel = row;
                        break;
                    }
                }
                if (sel == -1) continue;

                // Swap rows
                double[] tempRow = A[pivotRow];
                A[pivotRow] = A[sel];
                A[sel] = tempRow;
                double tempT = t[pivotRow];
                t[pivotRow] = t[sel];
                t[sel] = tempT;

                // Normalize pivot row
                double pivotVal = A[pivotRow][col];
                for (int c = col; c < cols; c++) {
                    A[pivotRow][c] /= pivotVal;
                }
                t[pivotRow] /= pivotVal;

                // Eliminate other rows
                for (int row = 0; row < rows; row++) {
                    if (row != pivotRow) {
                        double factor = A[row][col];
                        for (int c = col; c < cols; c++) {
                            A[row][c] -= factor * A[pivotRow][c];
                        }
                        t[row] -= factor * t[pivotRow];
                    }
                }

                pivotColForRow[pivotRow] = col;
                isPivotCol[col] = true;
                pivotRow++;
            }

            // Check for inconsistencies (0 = non-zero)
            for (int row = pivotRow; row < rows; row++) {
                if (Math.abs(t[row]) > EPSILON) return null;
            }

            // Identify free variables
            List<Integer> freeVars = new ArrayList<>();
            for (int c = 0; c < cols; c++) {
                if (!isPivotCol[c]) freeVars.add(c);
            }

            // Iterate free variables
            return search(0, freeVars, new long[cols], A, t, pivotColForRow, bounds);
        }

        private Long search(int freeVarIndex, List<Integer> freeVars, long[] solution, 
                           double[][] RREF, double[] rhs, int[] pivotColForRow, long[] bounds) {
            if (freeVarIndex == freeVars.size()) {
                // All free vars assigned, calculate pivots
                // Iterate rows backwards to be safe (though RREF makes it easy: x_p = rhs - sum(free))
                // Actually given RREF, each constraint is: x_p + sum(coeff * x_free) = rhs
                // So x_p = rhs - sum(coeff * x_free)
                // We can just iterate through pivot rows.
                
                long currentCost = 0;

                // Calculate pivot variables based on free variables
                for (int r = 0; r < pivotColForRow.length; r++) {
                    int pCol = pivotColForRow[r];
                    if (pCol == -1) continue; // Check next rows

                    double val = rhs[r];
                    for (int fIdx : freeVars) {
                        val -= RREF[r][fIdx] * solution[fIdx];
                    }

                    // Check integer and non-negative
                    long rounded = Math.round(val);
                    if (Math.abs(rounded - val) > EPSILON && Math.abs(rounded - val) < 1.0 - EPSILON) return null; // Not integer
                    if (rounded < 0) return null;

                    solution[pCol] = rounded;
                }

                // Calculate total cost
                for (long s : solution) currentCost += s;
                return currentCost;
            }

            int fCol = freeVars.get(freeVarIndex);
            Long minCost = null;

            // Iterate range for this free variable
            // Since cost is linear, we might not need to scan all?
            // But coefficients can be negative, so function is not monotonic w.r.t free var.
            // We just scan.
            for (long val = 0; val <= bounds[fCol]; val++) {
                solution[fCol] = val;
                Long res = search(freeVarIndex + 1, freeVars, solution, RREF, rhs, pivotColForRow, bounds);
                if (res != null) {
                    if (minCost == null || res < minCost) {
                        minCost = res;
                    }
                }
            }
            return minCost;
        }
    }
}
