import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Andreas Stocker Airport System");
        primaryStage.setScene(new Scene(root, 800, 575));
        primaryStage.show();


//        airportsComboBox.setItems();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
