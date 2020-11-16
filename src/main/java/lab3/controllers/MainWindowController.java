package lab3.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import lab3.logic.ExpertSimulator;
import lab3.models.PositionSummary;
import lab3.models.Skill;

import java.util.ArrayList;
import java.util.List;

public class MainWindowController
{
    @FXML ListView languagesList;
    @FXML ListView technologiesList;
    @FXML ListView instrumentsList;

    private DBController dbController;
    private ExpertSimulator expertSimulator;

    public void initialize() {
        try {
            dbController = DBController.getInstance();
        } catch (ClassNotFoundException ex) {
            System.out.println("The DB hasn't been configured properly!");
            System.exit(-1);
        }
        expertSimulator = new ExpertSimulator(dbController);

        fillLists();
        configureLists();
    }

    @FXML
    private void findPositionButtonClicked() {
        ObservableList<String> selectedLanguages = languagesList.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedTechnologies = technologiesList.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedInstruments = instrumentsList.getSelectionModel().getSelectedItems();

        List<String> skillsNames = new ArrayList<>(selectedLanguages);
        skillsNames.addAll(selectedTechnologies);
        skillsNames.addAll(selectedInstruments);

        expertSimulator.showTopPositionsBySkillsArray(skillsNames, 3);
    }

    private void fillLists() {
        List<Skill> allSkillsFromDB = dbController.getAllSkills();
        for (Skill skill : allSkillsFromDB) {
            if (skill.type.equals("language")) {
                languagesList.getItems().add(skill.name);
            } else if (skill.type.equals("technology")) {
                technologiesList.getItems().add(skill.name);
            } else {
                instrumentsList.getItems().add(skill.name);
            }
        }
    }

    private void configureLists() {
        languagesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        technologiesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        instrumentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
