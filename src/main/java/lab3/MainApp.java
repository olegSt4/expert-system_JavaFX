package lab3.logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/mainWindow.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode);

        stage.setTitle("Find your position");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
