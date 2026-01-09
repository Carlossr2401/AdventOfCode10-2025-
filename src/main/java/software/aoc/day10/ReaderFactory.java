package software.aoc.day10;



public class ReaderFactory {
    public InstructionReader<?> createFileReader(String type, String path) {
        if ("A".equalsIgnoreCase(type)) {
            return new software.aoc.day10.a.FileInstructionReader(path);
        } else if ("B".equalsIgnoreCase(type)) {
            return new software.aoc.day10.b.FileInstructionReader(path);
        }
        throw new IllegalArgumentException("Unknown solver type: " + type);
    }
}
