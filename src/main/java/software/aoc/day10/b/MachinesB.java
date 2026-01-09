package software.aoc.day10.b;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public record MachinesB(List<Machine> list) implements Iterable<Machine> {

    @Override
    public Iterator<Machine> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super Machine> action) {
        list.forEach(action);
    }
}