package lab3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lab3.logic.ExpertSimulator;
import lab3.logic.InputValidator;
import lab3.models.SpecializationSummary;

import java.util.List;

public class MainWindowController
{
    @FXML TextField skillsLine;

    private ExpertSimulator expertSimulator;

    public void initialize() {
        expertSimulator = new ExpertSimulator(new DBController());
    }

    public void acceptSkills() {

        String input = skillsLine.getText();
        String[] skillsArray;
        try {
            skillsArray = InputValidator.getValidSkillsArray(input);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return;
        }


        List<SpecializationSummary> topSpecializations =  expertSimulator.getSpecializationsBySkillsArray(skillsArray);
        for (SpecializationSummary ss : topSpecializations) {
            System.out.println(ss.getSpecialization().name + " - " + (int) ss.getPotentionalInPercentages() + "%");
        }
        System.out.println();

        skillsLine.clear();
    }

}
