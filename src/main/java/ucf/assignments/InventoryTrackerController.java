package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class InventoryTrackerController {

    @FXML
    public MenuItem saveTSV;
    @FXML
    public MenuItem saveHTML;
    @FXML
    public MenuItem saveJSON;
    @FXML
    public MenuItem loadTSV;
    @FXML
    public MenuItem loadJSON;
    @FXML
    public MenuItem loadHTML;
    @FXML
    public TableColumn valueofItem;
    @FXML
    public TableColumn serialNumberofItem;
    @FXML
    public TableColumn nameofItem;
    @FXML
    public TextField searchTextField;
    @FXML
    public TextField valueTextField;
    @FXML
    public TextField serialNumberTextField;
    @FXML
    public TextField nameTextField;


    private final ObservableList<Inventory> inventorys =  FXCollections.observableArrayList();

    private FileOperations fileOperations;

    private int selectedIndex = -1;



    public void SaveAsTSV(ActionEvent actionEvent) {
    }

    public void SaveFileClicked(ActionEvent actionEvent) {
    }

    public void SaveAsHTML(ActionEvent actionEvent) {
    }

    public void clearButtonClicked(ActionEvent actionEvent) {
    }

    public void addItemClicked(ActionEvent actionEvent) {
        if (!currencyValidator(valueTextField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Value must be monetary and greater than zero").show();

        } else if (!snValidator(serialNumberTextField.getText().trim()) || !snUniquenessValidator(serialNumberTextField.getText().trim()) ) {
            new Alert(Alert.AlertType.ERROR, "Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit").show();

        } else if(!nameValidator(nameTextField.getText().trim())){
            new Alert(Alert.AlertType.ERROR, "Item shall have a name between 2 and 256 characters inclusive").show();
        }else {

            inventorys.add( new Inventory(valueTextField.getText(), serialNumberTextField.getText(), nameTextField.getText()));
            valueTextField.clear();
            serialNumberTextField.clear();
            nameTextField.clear();
        }
    }

    public void searchButtonClicked(ActionEvent actionEvent) {
    }

    public void updateClicked(ActionEvent actionEvent) {
    }

    public void deleteClicked(ActionEvent actionEvent) {
    }

    public void sortBySNclicked(ActionEvent actionEvent) {
    }

    public void sortByNameclicked(ActionEvent actionEvent) {
    }

    public void sortByValueClicked(ActionEvent actionEvent) {
    }

    private boolean currencyValidator(String value) {
        if (value == null) {
            return false;
        }
        return value.trim().matches("(-?\\d+\\.?\\d{0,2})") && Double.parseDouble(value) > 0;
    }

    private boolean snValidator(String value) {
        if (value == null){
            return false;
        }
        return  value.trim().matches("[A-Z0-9]+") && value.trim().length() == 10;
    }

    private boolean snUniquenessValidator(String value){
        return inventorys.stream().noneMatch(i -> i.getSerialNumber().equalsIgnoreCase(value.trim()));
    }

    private boolean nameValidator(String value) {
        if (value == null || value.length() < 2 || value.length() > 256){
            return false;
        }
        return true;
    }
}
