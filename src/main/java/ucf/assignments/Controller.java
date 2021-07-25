/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Shiv Patel
 */

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

    //observable list declaration
    private ObservableList<Inventory> inventorys;

    //fileOperations declaration from class FileOperation
    private FileOperations fileOperations;
    private int selectedIndex;

    //File chooser to allow user to choose files from their device
    FileChooser fileChooser;


    @FXML
    private AnchorPane anchorPane;

    //table view declartion
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

//Controller class to access observable array list, selected index, and file operations
    public Controller() {
        this.inventorys = FXCollections.observableArrayList();
        this.selectedIndex = -1;
        fileOperations = new FileOperations();

    }

//search method
    public void search(ActionEvent actionEvent) {

        //uses text field from fxml
        String searchText = searchTextField.getText();

        //if statement to find search query
        if (searchText != null && !searchText.isEmpty()) {
            //uses to tableview methods to grab stream to search by either serial number, name , or value
            tableView.getItems().stream()
                    .filter(item -> item.getSerialNumber().toLowerCase().contains(searchText.toLowerCase()) || item.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .findAny()
                    .ifPresent(item -> {
                        tableView.getSelectionModel().select(item);
                        valueTextField.setText(item.getValue().substring(1));
                        serialNumberTextField.setText(item.getSerialNumber());
                        nameTextField.setText(item.getName());

                        //scrolls to item on table view
                        tableView.scrollTo(item);
                    });

            //if query is not found shows alert to user "Not found"
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Not found").show();
        }
    }

    //clear queries in text fields
    public void clear(ActionEvent actionEvent) {
        searchTextField.clear();
        valueTextField.clear();
        serialNumberTextField.clear();
        nameTextField.clear();
    }

    //add function for inventory
    public void add(ActionEvent actionEvent) {

        //gets text from text fields
        System.out.println(valueTextField.getText());
        System.out.println(serialNumberTextField.getText());
        System.out.println(nameTextField.getText());

        //exception throwers in if statements if value, name, or serial number is not in format expected
        if (!currencyValidator(valueTextField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Value must be monetary and greater than zero").show();

        } else if (!snValidator(serialNumberTextField.getText().trim()) || !snUniquenessValidator(serialNumberTextField.getText().trim())) {
            new Alert(Alert.AlertType.ERROR, "Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit").show();

        } else if (!nameValidator(nameTextField.getText().trim())) {
            new Alert(Alert.AlertType.ERROR, "Item shall have a name between 2 and 256 characters inclusive").show();
        } else {

            //adds text field value into observable list
            inventorys.add(new Inventory(valueTextField.getText(), serialNumberTextField.getText(), nameTextField.getText()));

            //sets table view with observable list
            tableView.setItems(inventorys);

            //clear text fields
            valueTextField.clear();
            serialNumberTextField.clear();
            nameTextField.clear();
        }
    }

    //update function
    public void update(ActionEvent actionEvent) {

        //selected index is used from tableview user clicks then changes value in text fields then presses update button
        if (selectedIndex >= 0) {

            //exception throwers in if statements if value, name, or serial number is not in format expected
            if (!currencyValidator(valueTextField.getText())) {
                new Alert(Alert.AlertType.ERROR, "Value must be monetary and greater than zero").show();

            } else if (!snValidator(serialNumberTextField.getText().trim())) {
                new Alert(Alert.AlertType.ERROR, "Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit").show();

            } else if (!nameValidator(nameTextField.getText().trim())) {
                new Alert(Alert.AlertType.ERROR, "Item shall have a name between 2 and 256 characters inclusive").show();
            } else {

                //removes selected index and adds new updated textfields into observable list
                inventorys.remove(selectedIndex);
                inventorys.add(selectedIndex, new Inventory(valueTextField.getText(), serialNumberTextField.getText(), nameTextField.getText()));

                //clears text field after "Update" button is pressed
                valueTextField.clear();
                serialNumberTextField.clear();
                nameTextField.clear();
            }
        }
    }

    //delete function based on selected index
    public void delete(ActionEvent actionEvent) {

        //if statement for selected index
        if (selectedIndex >= 0) {

            //remove from observable list and tableview
            inventorys.remove(selectedIndex);

            //clears textfield after button is pressed
            searchTextField.clear();
            valueTextField.clear();
            serialNumberTextField.clear();
            nameTextField.clear();

            //decrements the index
            selectedIndex = -1;
        }
    }

    //sort by value function uses getprice() function from inventory for sorting
    public void sortByValue(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getPrice));
    }

    //sort by serial Number function uses getSerialNumber() function from inventory for sorting
    public void sortBySerialNumber(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getSerialNumber));

    }

    //sort by Name function uses getName() function from Inventory class for sorting
    public void sortByName(ActionEvent actionEvent) {
        inventorys.sort(Comparator.comparing(Inventory::getName));
    }

//onclick function
    public void onClicked(MouseEvent mouseEvent) {

        //grabs selected item from user from table view
        Inventory selectedItem = tableView.getSelectionModel().getSelectedItem();

        //if selected item is not null
        if (selectedItem != null) {

            //selected index is set to tableView item and grabs the value, serial Number, and Name of selected index
            selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            valueTextField.setText(selectedItem.getValue().substring(1));
            serialNumberTextField.setText(selectedItem.getSerialNumber());
            nameTextField.setText(selectedItem.getName());

            //else sets to nothing
        } else {
            valueTextField.setText("");
            serialNumberTextField.setText("");
            nameTextField.setText("");

        }

    }

    //load TSV function
    public void loadTCV(ActionEvent actionEvent) {

        //create filechooser
        fileChooser = new FileChooser();

        //allows user to select file based on .tsv extension
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TCV Files", "*.tsv"));

        //set title of filechooser
        fileChooser.setTitle("Load");

        //sets stage and throws anchorPane
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        //if file does not equal null
        if (file != null) {
            //load data
            List<Inventory> dataList = fileOperations.loadTSVData(file);

            //adds to observable list
            if (dataList != null && !dataList.isEmpty()) {
                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {
                        inventorys.add(inventory);
                    } else {
                        // exception alert if dupicliates are found
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();

                    }

                }
                //sets to table view
                tableView.setItems(inventorys);
            }
        }
    }

    //loadJSON function
    public void loadJSON(ActionEvent actionEvent) {

        //file chooser declaration
        fileChooser = new FileChooser();

        //allows user to get file based on .json extension
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setTitle("Load JSON");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        //if file is not null
        if (file != null) {
            //load data
            List<Inventory> dataList = fileOperations.loadJSONData(file);

            //if statement to confirm datalist in file is not null or empty
            if (dataList != null && !dataList.isEmpty()) {
                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {

                        //adds to observable list
                        inventorys.add(inventory);


                    } else {

                        //alert thrown if duplicates are found
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();

                    }

                }
                //sets to tableview
                tableView.setItems(inventorys);
            }
        }
    }

    //load Html function
    public void loadHTML(ActionEvent actionEvent) {

        //file chooser declaration
        fileChooser = new FileChooser();

        //allows user to only select html files from their devices
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        fileChooser.setTitle("Load HTML");
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        //if file is not null
        if (file != null) {

            //load data
            List<Inventory> dataList = fileOperations.loadHTMLData(file);
            if (dataList != null && !dataList.isEmpty()) {

                for (Inventory inventory : dataList) {
                    if (!isContainsDuplicate(inventory)) {

                        //adds to observable list inventory
                        inventorys.add(inventory);
                    } else {

                        //throws exception if duplicate is found
                        new Alert(Alert.AlertType.ERROR, "Inventory contains duplicate serial number: " + inventory.getSerialNumber() + ". Not added.").show();
                    }
                }

                //sets to tableview
                tableView.setItems(inventorys);
            }
        }
    }

    //save as TSV function
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

    //save as JSON function
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

    //save as HTMl function
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

    //validator functions to check uniqueness ,format, and duplicates
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
