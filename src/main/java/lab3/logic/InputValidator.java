package lab3.logic;

import java.util.Iterator;

public class InputValidator {
    public static String[] getValidSkillsArray(String input) throws IllegalArgumentException{
        if (!input.matches("[a-zA-Z0-9.,+#/ ]+")) {
            throw new IllegalArgumentException("Only numbers, letters or on of the symbols [.,+#/] are allowed!");
        }

        String[] skillsArray = input.split(",");
        for (int i = 0; i < skillsArray.length; i++) {
            skillsArray[i] = skillsArray[i]
                                .replaceAll(",", "")
                                .trim()
                                .toLowerCase();
        }
        return skillsArray;
    }
}
