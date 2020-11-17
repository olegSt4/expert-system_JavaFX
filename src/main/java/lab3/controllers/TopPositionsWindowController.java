package lab3.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lab3.models.PositionSummary;
import lab3.models.Skill;


import java.util.List;

public class TopPositionsWindowController {
    @FXML private ListView skillsList;
    @FXML private VBox positionsVBox;


    /** Shows the  list of user positions and corresponding top-positions with theirs conformities*/
    public void showInfo(List<PositionSummary> topPositions) {
        if (topPositions.size() < 1) {
            return;
        }

        showAvailableSkills(topPositions.get(0).getAvailableSkills());

        for (PositionSummary topPosition : topPositions) {
            // vBox representing the top-position info
            VBox vBox = new VBox();
            vBox.setPrefSize(230, 290);
            vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            Image positionImage = topPosition.getPosition().getImage();
            ImageView imageView = new ImageView(positionImage);
            imageView.setFitWidth(230);
            imageView.setFitHeight(230);

            Label posName = new Label(topPosition.getPosition().name);
            posName.setPrefSize(230, 30);
            posName.setAlignment(Pos.CENTER);
            posName.setFont(new Font("System Bold", 16));

            int conformityInPercents = (int) (topPosition.getConformity() * 100);
            Label posConformity = new Label(conformityInPercents + "%");
            posConformity.setPrefSize(320, 30);
            posConformity.setAlignment(Pos.CENTER);
            posConformity.setFont(new Font("System Bold", 16));

            vBox.getChildren().addAll(imageView, posName, posConformity);
            positionsVBox.getChildren().add(vBox);
        }
    }

    private void showAvailableSkills(List<Skill> availableSkills) {
        for (Skill skill : availableSkills) {
            skillsList.getItems().add(skill.name);
        }
    }
}
