package by.naty.chat.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class ModalController {

    private static String ipAddress1;
    private static String port1;
    @FXML
    private TextField port;
    @FXML
    private TextField ipAddress;

    static Pair<String, Integer> display() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(ModalController.class.getResource("port.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Data input");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(ipAddress1, Integer.parseInt(port1));

    }

    public void save(ActionEvent actionEvent) {
        ipAddress1 = ipAddress.getText();
        port1 = port.getText();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
