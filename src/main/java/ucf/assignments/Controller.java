package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class Controller {

    private ObservableList<Inventory> inventorys;
    private FileOperations fileOperations;
    private int selectedIndex;
    FileChooser fileChooser;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Inventory> tableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private TextField serialNumberTextField;
    @FXML
    private TextField nameTextField;


    public Controller() {
        this.inventorys = FXCollections.observableArrayList();
        this.selectedIndex = -1;
        fileOperations = new FileOperations();

    }


    public void search(ActionEvent actionEvent) {
        String searchText = searchTextField.getText();
        if (searchText != null && !searchText.isEmpty()) {
            tableView.getItems().stream()
                    .filter(item -> item.getSerialNumber().toLowerCase().contains(searchText.toLowerCase()) || item.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .findAny()
                    .ifPresent(item -> {
                        tableView.getSelectionModel().select(item);
                        valueTextField.setText(item.getValue().substring(1));
                        serialNumberTextField.setText(item.getSerialNumber());
                        nameTextField.setText(item.getName());

                        tableView.scrollTo(item);
                    });
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Not found").show();
        }
    }

    public void clear(ActionEvent actionEvent) {
        searchTextField.clear();
        valueTextField.clear();
        serialNumberTextField.clear();
        nameTextField.clear();
    }

    public void add(ActionEvent actionEvent) {
        System.out.println(valueTextField.getText());
        System.out.println(serialNumberTextField.getText());
        System.out.println(nameTextField.getText());
        if (!currencyValidator(valueTextField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Value must be monetary and greater than zero").show();

        } else if (!snValidator(serialNumberTextField.getText().trim()) || !snUniquenessValidator(serialNumberTextField.getText().trim())) {
            new Alert(Alert.AlertType.ERROR, "Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit").show();

        } else if (!nameValidator(nameTextField.getText().trim())) {
            new Alert(Alert.AlertType.ERROR, "Item shall have a name between 2 and 256 characters inclusive").show();
        } else {

            inventorys.add(new Inventory(valueTextField.getText(), serialNumberTextField.getText(), nameTextField.getText()));
            tableView.setItems(inventorys);
            valueTextField.clear();
            serialNumberTextField.clear();
            nameTextField.clear();
        }
    }

    public void update(ActionEvent actionEvent) {
        if (selectedIndex >= 0) {
            if (!currencyValidator(valueTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "Value must be monetary and greater than zero").show();

            } else if (!snValidator(serialNumberTextField.getText().trim())) {
                new Alert(Alert.AlertType.ERROR, "Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit").show();

            } else if (!nameValidator(nameTextField.getText().trim())) {
                new Alert(Alert.AlertType.ERROR, "Item shall have a name between 2 and 256 characters inclusive").show();
            } else {

                inventorys.remove(selectedIndex);
                inventorys.add(selectedIndex, new Inventory(valueTextField.getText(), serialNumberTextField.getText(), nameTextField.getText()));

                valueTextField.clear();
                serialNumberTextField.clear();
                nameTextField.clear();
            }
        }
    }

    public void delete(ActionEvent actionEvent) {
        if (selectedIndex >= 0) {
            inventorys.remove(selectedIndex);
            searchTextField.clear();
            valueTextField.clear();
            serialNumberTextField.clear();
            nameTextField.clear();
            selectedIndex = -1;
        }
    }

    public void sortByValue(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getPrice));
    }

    public void sortBySerialNumber(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getSerialNumber));

    }

    public void sortByName(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getName));
    }

    public void onClicked(MouseEvent mouseEvent) {
        Inventory selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            valueTextField.setText(selectedItem.getValue().substring(1));
            serialNumberTextField.setText(selectedItem.getSerialNumber());
            nameTextField.setText(selectedItem.getName());

        } else {
            valueTextField.setText("");
            serialNumberTextField.setText("");
            nameTextField.setText("");

        }

    }

    public void loadTCV(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TCV Files", "*.tsv"));
        fileChooser.setTitle("Load");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //load data
            List<Inventory> dataList = fileOperations.loadTSVData(file);
            if (dataList != null && !dataList.isEmpty()) {
                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {
                        inventorys.add(inventory);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();

                    }

                }
                tableView.setItems(inventorys);
            }
        }
    }

    public void loadJSON(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setTitle("Load JSON");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //load data
            List<Inventory> dataList = fileOperations.loadJSONData(file);
            if (dataList != null && !dataList.isEmpty()) {
                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {
                        inventorys.add(inventory);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();

                    }

                }
                tableView.setItems(inventorys);
            }
        }
    }

    public void loadHTML(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        fileChooser.setTitle("Load HTML");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //load data
            List<Inventory> dataList = fileOperations.loadHTMLData(file);
            if (dataList != null && !dataList.isEmpty()) {

                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {
                        inventorys.add(inventory);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();
                    }
                }
                tableView.setItems(inventorys);
            }
        }
    }

    public void saveTCV(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TCV Files", "*.tsv"));
        fileChooser.setTitle("Save TCV");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //save data to file
            fileOperations.saveTCVData(file, inventorys);
        }
    }

    public void saveJSON(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        fileChooser.setTitle("Save JSON");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //save data to file
            fileOperations.saveJSONData(file, inventorys);
        }
    }

    public void saveHTML(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        fileChooser.setTitle("Save HTML");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //save data to file
            fileOperations.saveHTMLData(file, inventorys);
        }
    }

    private boolean currencyValidator(String value) {
        if (value == null) {
            return false;
        }
        return value.trim().matches("(-?\\d+\\.?\\d{0,2})") && Double.parseDouble(value) > 0;
    }

    private boolean snValidator(String value) {
        if (value == null) {
            return false;
        }

        return value.trim().matches("[A-Z0-9]+") && value.trim().length() == 10;
    }

    private boolean snUniquenessValidator(String value) {
        return inventorys.stream().noneMatch(i -> i.getSerialNumber().equalsIgnoreCase(value.trim()));
    }

    private boolean nameValidator(String value) {
        if (value == null || value.length() < 2 || value.length() > 256) {
            return false;
        }
        return true;
    }

    //if found duplicate
    private boolean isContainsDuplicate(Inventory inventory) {
        return inventorys.stream().anyMatch(i -> i.getSerialNumber().equals(inventory.getSerialNumber()));
    }


}
