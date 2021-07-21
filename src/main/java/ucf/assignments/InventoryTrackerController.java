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

    //observable list declartion
    private ObservableList<Inventory> tasks= FXCollections.observableArrayList();

    //Right click menu items to either delete or update tasks
    MenuItem update= new MenuItem("Update");
    MenuItem delete= new MenuItem("Delete");
    private final ContextMenu contextMenu=new ContextMenu(update,delete);

    //declaration of buttons in program for adding new item, saving, and restoring the original save file
    @FXML
    public Button addNew;

    @FXML
    public Button save;


    //Tableview connection with Item Class and Fxml File
    @FXML private TableView<Inventory> table;
    @FXML private TableColumn<Inventory,String> Description;
    @FXML private TableColumn<Inventory,String>DueDate;
    @FXML private TableColumn<Inventory,String>Status;


    @FXML
    private void AddItemClicked(ActionEvent event) {

        //try-catch statement to open fxml file
        try {
            //opens FXML scene to add a new item
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ucf/assignmets/AddItem.fxml"));
            Parent root = loader.load();
            AddItemController control=loader.getController();
            control.setTableItems(table.getItems());
            Stage stage = new Stage();
            stage.setTitle("Add Task");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }


    public void SaveAsTSV(ActionEvent actionEvent) {
    }

    public void SaveFileClicked(ActionEvent actionEvent) {
    }

    public void SaveAsHTML(ActionEvent actionEvent) {
    }
}
