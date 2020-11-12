package lab3.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainWindowController
{
    @FXML TextField skillsLine;

    public void acceptSkills() {
        String input = skillsLine.getText();
        System.out.println(input);
        skillsLine.clear();
    }

}
