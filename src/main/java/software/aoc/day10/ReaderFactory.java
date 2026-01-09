package software.aoc.day10;

import software.aoc.day10.b.FileInstructionReader;

public class ReaderFactory {
    public InstructionReader createFileReader(String path) {
        return new FileInstructionReader(path);
    }
}
