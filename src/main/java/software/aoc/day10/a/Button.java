package software.aoc.day10.a;

import java.util.List;

public record Button(List<Integer> positions) {
    public void press(boolean[] lights) {
        for (Integer pos : positions) {
            if (pos >= 0 && pos < lights.length) {
                lights[pos] = !lights[pos];
            }
        }
    }
}
