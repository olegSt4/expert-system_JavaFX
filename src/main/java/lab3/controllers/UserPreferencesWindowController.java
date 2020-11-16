package lab3.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lab3.models.PositionSummary;
import lab3.models.Skill;

import java.util.*;

public class UserPreferencesWindowController {
    @FXML VBox skillsVBox;
    @FXML Button acceptButton;

    private LinkedList<CheckBox> checkedBoxes;
    private DBController dbController;
    private List<PositionSummary> topPosSummaries;



    public void initialize() {
        checkedBoxes = new LinkedList<>();
    }

    public void askUser(List<PositionSummary> topPosSummaries, DBController dbController) {
        this.dbController = dbController;
        this.topPosSummaries = topPosSummaries;

        showSkills(topPosSummaries);
    }

    @FXML
    private void acceptButtonClicked() {
        List<String> skillsToLearnNames = new ArrayList<>();
        for (CheckBox checkedBox : checkedBoxes) {
            skillsToLearnNames.add(checkedBox.getText());
        }

        List<Skill> skillsToLearn = dbController.getSkillsListBySkillsNames(skillsToLearnNames);

        for (PositionSummary positionSummary : topPosSummaries) {
            for (Skill skillToLearn : skillsToLearn) {
                positionSummary.addAvailableSkill(skillToLearn);
            }
        }

        topPosSummaries.sort((PositionSummary ss1, PositionSummary ss2) ->
                Double.compare(ss2.getConformity(), ss1.getConformity())
        );

        drawPositions(topPosSummaries);
        Stage currentStage = (Stage) skillsVBox.getScene().getWindow();
        currentStage.close();
    }

    private void showSkills(List<PositionSummary> competitivePosSummaries) {
        Set<Skill> freeSkills = new HashSet<>();

        for (PositionSummary topPos : competitivePosSummaries) {
            freeSkills.addAll(topPos.getFreeSkills());
        }

        for (Skill skill : freeSkills) {
            CheckBox skillCheckBox = new CheckBox(skill.name);
            skillCheckBox.setOnMouseClicked((e) -> {
                if (checkedBoxes.contains(skillCheckBox)) {
                    checkedBoxes.remove(skillCheckBox);
                    skillCheckBox.setSelected(false);
                } else {
                    if (checkedBoxes.size() > 2) {
                        CheckBox firstCB = checkedBoxes.pollFirst();
                        firstCB.setSelected(false);
                    }
                    checkedBoxes.addLast(skillCheckBox);
                    skillCheckBox.setSelected(true);
                }
            });
            skillsVBox.getChildren().add(skillCheckBox);
        }
    }

    private void drawPositions(List<PositionSummary> topSpecializations) {
        try {
            String fxmlFile = "/fxml/topPositionsWindow.fxml";
            FXMLLoader loader = new FXMLLoader();
            Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

            Scene scene = new Scene(rootNode);

            Stage stage = new Stage();
            stage.setTitle("Top positions");
            Image icon = new Image("/images/logo.png");
            stage.getIcons().add(icon);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            TopPositionsWindowController TPWC = loader.getController();
            TPWC.showInfo(topSpecializations);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
