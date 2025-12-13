package software.aoc.day10.a;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record FileInstructionReader(String filePath) implements InputReader {

    public ListOfMachines readAllData() throws IOException {

        List<Machine> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                list.add(createMachine(line));
            }
        }

        return new ListOfMachines(list);
    }

    private Machine createMachine(String line) {
        String targetPart = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
        List<Button> buttons = new ArrayList<>();

        java.util.regex.Matcher buttonMatcher = java.util.regex.Pattern.compile("\\((.*?)\\)").matcher(line);

        while (buttonMatcher.find()) {
            String buttonIndicesStr = buttonMatcher.group(1);
            buttons.add(parseButton(buttonIndicesStr));
        }

        LightConfiguration target = parseTarget(targetPart);

        return new Machine(target, buttons);
    }

    private LightConfiguration parseTarget(String targetStr) {
        List<Boolean> targetLights = new ArrayList<>();
        for (char c : targetStr.toCharArray()) {
            if (c == '#') {
                targetLights.add(true);
            } else if (c == '.') {
                targetLights.add(false);
            }
        }
        return new LightConfiguration(targetLights);
    }

    private Button parseButton(String buttonIndicesStr) {
        String cleanStr = buttonIndicesStr.replaceAll("\\s+", "");
        String[] indices = cleanStr.split(",");

        List<Integer> lightIndices = new ArrayList<>();

        for (String indexStr : indices) {
            if (!indexStr.isEmpty()) {
                try {
                    lightIndices.add(Integer.parseInt(indexStr));
                } catch (NumberFormatException e) {
                    System.err.println("Error de formato al parsear índice de botón: " + indexStr);
                }
            }
        }

        return new Button(lightIndices);
    }
}