package lab3.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lab3.controllers.DBController;
import lab3.controllers.TopPositionsWindowController;
import lab3.controllers.UserPreferencesWindowController;
import lab3.models.Position;
import lab3.models.Skill;
import lab3.models.PositionSummary;

import java.util.*;

public class ExpertSimulator {
    private DBController dbController;

    public ExpertSimulator(DBController dbController) {
        this.dbController = dbController;
    }

    public void showTopPositionsBySkillsArray(List<String> skillsNames, int topAmount) {
        List<Skill> skillsList = dbController.getSkillsListBySkillsNames(skillsNames);
        Set<Position> relevantPositions = dbController.getRelevantPositionsForSkills(skillsList);
        List<PositionSummary> relevantPosSummaries = getPositionSummaries(relevantPositions, skillsList);

        showTopPositions(relevantPosSummaries, topAmount);
    }

    private List<PositionSummary> getPositionSummaries(Set<Position> relevantPositions, List<Skill> skillsList) {
        List<PositionSummary> posSummaries = new ArrayList<>();

        for (Position position : relevantPositions) {
            PositionSummary posSummary = dbController.getPositionSummary(position);
            posSummary.setAvailableSkills(skillsList);

            posSummaries.add(posSummary);

        }

        return posSummaries;
    }

    private void showTopPositions(List<PositionSummary> relevantPosSummaries, int topAmount) {
        relevantPosSummaries.sort((PositionSummary ss1, PositionSummary ss2) ->
                Double.compare(ss2.getConformity(), ss1.getConformity())
        );


        List<PositionSummary> topPositions;
        if (relevantPosSummaries.size() > topAmount) {
            topPositions = relevantPosSummaries.subList(0, topAmount);
        } else {
            topPositions = relevantPosSummaries.subList(0, relevantPosSummaries.size());
        }

        if (topPositions.size() < 1) {
            return;
        }

        if (topPositions.size() > 1) {
            double topConformity = topPositions.get(0).getConformity();
            if (topConformity < 1) {
                int competitivePosCount = 0;
                for (PositionSummary topPos : topPositions) {
                    if (Math.abs(topPos.getConformity() - topConformity) <= 0.1) {
                        competitivePosCount++;

                        if (competitivePosCount > 1) {
                            askUserPreferences(topPositions);
                        }
                    }
                }
            }
        }

        drawPositions(topPositions);


    }

    public void askUserPreferences(List<PositionSummary> competetivePosSummaries) {
        try {
            String fxmlFile = "/fxml/userPreferencesWindow.fxml";
            FXMLLoader loader = new FXMLLoader();
            Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

            Scene scene = new Scene(rootNode);

            Stage stage = new Stage();
            stage.setTitle("User Preferences");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            UserPreferencesWindowController UPWC = loader.getController();
            UPWC.askUser(competetivePosSummaries, dbController);
        } catch (Exception ex) {
            ex.printStackTrace();
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
