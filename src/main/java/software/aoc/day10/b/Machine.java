package software.aoc.day10.b;

import software.aoc.day10.Button;
import java.util.List;

public record Machine(List<Integer> targetCounters, List<Button> buttons) {
}
