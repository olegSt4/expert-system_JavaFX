package lab3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lab3.logic.ExpertSimulator;
import lab3.logic.InputValidator;
import lab3.models.PositionSummary;

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


        List<PositionSummary> topSpecializations =  expertSimulator.getTopPositionsBySkillsArray(skillsArray,3);
        for (PositionSummary ss : topSpecializations) {
            double conformity = ss.getConformity() * 100;
            System.out.println(ss.getPosition().name + " - " + (int) conformity + "%");
        }
        System.out.println();



        skillsLine.clear();
    }

    private void drawPositions(List<PositionSummary> topPositions) {
        // TODO: 13.11.2020 draw positions on main window
    }

}
