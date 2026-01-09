package software.aoc.day10.b;

import software.aoc.day10.Button;
import software.aoc.day10.InstructionReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record FileInstructionReader(String filePath) implements InstructionReader<MachinesB> {

    @Override
    public MachinesB readAllData() {
        List<Machine> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(createMachine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read instructions from file: " + filePath, e);
        }
        return new MachinesB(list);
    }

    private Machine createMachine(String line) {
        // Parse targets from {x,y,z...}
        String targetPart = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
        List<Integer> targets = parseTargets(targetPart);

        // Parse buttons from (x,y...)
        List<Button> buttons = new ArrayList<>();
        Matcher buttonMatcher = Pattern.compile("\\(([\\d,]+)\\)").matcher(line);
        while (buttonMatcher.find()) {
            buttons.add(parseButton(buttonMatcher.group(1)));
        }

        return new Machine(targets, buttons);
    }

    private List<Integer> parseTargets(String targetStr) {
        String[] parts = targetStr.split(",");
        List<Integer> targets = new ArrayList<>();
        for (String s : parts) {
            targets.add(Integer.parseInt(s.trim()));
        }
        return targets;
    }

    private Button parseButton(String buttonIndicesStr) {
        String cleanStr = buttonIndicesStr.replaceAll("\\s+", "");
        String[] indices = cleanStr.split(",");
        List<Integer> lightIndices = new ArrayList<>();
        for (String indexStr : indices) {
            if (!indexStr.isEmpty()) {
                lightIndices.add(Integer.parseInt(indexStr));
            }
        }
        return new Button(lightIndices);
    }
}