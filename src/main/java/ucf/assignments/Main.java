package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ucf/assignments/Main.fxml"));
        primaryStage.setTitle("Inventory");
        primaryStage.setScene(new Scene(root, 515, 550));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}