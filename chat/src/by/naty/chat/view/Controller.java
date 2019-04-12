package by.naty.chat.view;

import by.naty.chat.client.ClientDo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    private ClientDo client;
    @FXML
    private TextField textField;
    @FXML
    private Label lblName;
    @FXML
    private ListView<String> lwMessages;
    private ObservableList<String> messages = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        lwMessages.setItems(messages);
    }

    private void inputName() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Ask");
        dialog.setHeaderText("Enter your name");

        Optional<String> result = dialog.showAndWait();
        String entered = result.orElse("Incognito");
        lblName.setText(entered);
    }

    private void ErrorShow(String nameFxml) throws IOException {
        Pair<String, Integer> data = ModalController.display();
        String ip = data.getKey();
        int port = data.getValue();

        try {
            client = new ClientDo(ip, port);
            client.setController(this);
            inputName();
            client.registration(lblName.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String msg) {
        try {
            messages.add(msg);
        } catch (Exception ignored) {
        }
    }

    public void show(ActionEvent actionEvent) throws IOException {
        ErrorShow("port.fxml");
    }

    @FXML
    void sendClicked(ActionEvent event) {
        client.sendMsg(textField.getText());
    }
}