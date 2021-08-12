package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class Main extends Application {

    ContactBook contactBook;
    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        primaryStage.setTitle("Contacts");
        Image icon = new Image(getClass().getResourceAsStream("contactsIcon.png"));
        primaryStage.getIcons().add(icon);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Dialog<String> dialogBox = new Dialog<>();
        dialogBox.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Label fn = new Label("First Name:");
        grid.add(fn, 0, 0);
        GridPane.setHalignment(fn, HPos.RIGHT);
        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 0);

        Label ln = new Label("Last Name:");
        grid.add(ln, 0, 1);
        GridPane.setHalignment(ln, HPos.RIGHT);
        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 1);

        Label number = new Label("Phone Number:");
        grid.add(number, 2, 0);
        GridPane.setHalignment(number, HPos.RIGHT);
        TextField numberTextField = new TextField();
        grid.add(numberTextField, 3, 0);

        Label email = new Label("Email:");
        grid.add(email, 2, 1);
        GridPane.setHalignment(email, HPos.RIGHT);
        TextField emailTextField = new TextField();
        grid.add(emailTextField, 3, 1);

        Button addBtn = new Button("Add Contact");
        HBox hbAddBtn = new HBox(10);
        hbAddBtn.getChildren().add(addBtn);
        grid.add(hbAddBtn, 4,0);

        contactBook = new ContactBook();
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (firstNameTextField.getText().trim().isEmpty() || lastNameTextField.getText().trim().isEmpty() || numberTextField.getText().trim().isEmpty() ||
                    emailTextField.getText().trim().isEmpty())
                {
                    dialogBox.setTitle("Error");
                    dialogBox.setContentText("Could not add contact. One or more fields are empty.");
                    dialogBox.showAndWait();
                }

                else {
                    String fn = firstNameTextField.getText();
                    String ln = lastNameTextField.getText();
                    String number = numberTextField.getText();
                    String email = emailTextField.getText();
                    firstNameTextField.clear();
                    lastNameTextField.clear();
                    numberTextField.clear();
                    emailTextField.clear();

                    dialogBox.setTitle("Contact Added");
                    dialogBox.setContentText("Successfully added contact!");
                    dialogBox.showAndWait();
                    try {
                        contactBook.addContact(fn, ln, number, email);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Label contacts = new Label("Search:");
        grid.add(contacts, 0, 5);
        GridPane.setHalignment(contacts, HPos.RIGHT);
        TextField searchFirstName = new TextField();
        grid.add(searchFirstName, 1, 5, 2, 1);

        Label contactsFound = new Label("Contacts:");
        grid.add(contactsFound, 1, 6);

        ObservableList<Contact> doList = FXCollections.observableArrayList();
        ListView<Contact> listView = new ListView<>(doList);
        grid.add(listView, 1, 7, 3, 3);
        listView.setPrefHeight(400);
        listView.setPrefWidth(250);

        SortedList<Contact> sortedList = new SortedList<>(doList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.lastName.compareToIgnoreCase(o2.lastName);
            }
        });

        Button sortBtn = new Button("Sort By Last Name");
        HBox hbSortBtn = new HBox(10);
        hbSortBtn.getChildren().add(sortBtn);
        grid.add(hbSortBtn, 4,7);
        sortBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                doList.setAll(sortedList);
            }
        });

        Button searchBtn = new Button("Search Contact");
        HBox hbSearchBtn = new HBox(10);
        hbSearchBtn.getChildren().add(searchBtn);
        grid.add(hbSearchBtn, 3,5);

        searchBtn.disableProperty().bind(Bindings.isEmpty(searchFirstName.textProperty()));

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        Scene scene2 = new Scene(alert, 50,50);
//        Dialog<String> dialogBox = new Dialog<>();
//        dialogBox.getDialogPane().getButtonTypes().add(ButtonType.OK);

        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                contactBook.readJson("./src/sample/Contacts.json");
                String fn = searchFirstName.getText();
                searchFirstName.clear();

                firstNameTextField.clear();
                lastNameTextField.clear();
                numberTextField.clear();
                emailTextField.clear();
                doList.clear();

                ArrayList<Contact> c = contactBook.findName(fn);
                if(c.size() == 0){
                    dialogBox.setContentText("No contacts found!");
                    dialogBox.setTitle("Contacts");
                    dialogBox.showAndWait();
//                    alert.setContentText("No contacts found!");
//                    alert.show();

                }
                doList.addAll(c);
            }
        });

        final Contact[] cont = {null};

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {
            @Override
            public void changed(ObservableValue<? extends Contact> observableValue, Contact contact, Contact t1) {
                if(t1 != null) {
                    firstNameTextField.setText(t1.getFirstName());
                    lastNameTextField.setText(t1.getLastName());
                    numberTextField.setText(t1.getPhoneNumber());
                    emailTextField.setText(t1.getEmail());
                    cont[0] = t1;
                }
            }
        });

        Button clearBtn = new Button("Clear");
        HBox hbClearBtn = new HBox(10);
        hbClearBtn.getChildren().add(clearBtn);
        grid.add(hbClearBtn, 4,9);
        clearBtn.setOnAction(actionEvent -> {
            doList.clear();
            firstNameTextField.clear();
            lastNameTextField.clear();
            numberTextField.clear();
            emailTextField.clear(); 
        });

        Button deleteBtn = new Button("Delete Contact");
        HBox hbDeleteBtn = new HBox(10);
        hbDeleteBtn.getChildren().add(deleteBtn);
        grid.add(hbDeleteBtn, 4,8);
        deleteBtn.setOnAction(actionEvent -> {
            contactBook.deleteJson(cont[0]);
            firstNameTextField.clear();
            lastNameTextField.clear();
            numberTextField.clear();
            emailTextField.clear();
            doList.remove(cont[0]);
            dialogBox.setTitle("Contact Deleted");
            dialogBox.setContentText("Successfully deleted contact!");
            dialogBox.showAndWait();
        });

        Button updateBtn = new Button("Update Contact");
        HBox hbUpdateBtn = new HBox(10);
        hbUpdateBtn.getChildren().add(updateBtn);

        grid.add(hbUpdateBtn, 4,1);

        updateBtn.setOnAction(actionEvent -> {
            contactBook.updateJson(cont[0], new Contact(firstNameTextField.getText(), lastNameTextField.getText(), numberTextField.getText(), emailTextField.getText()));
            doList.clear();
            firstNameTextField.clear();
            lastNameTextField.clear();
            numberTextField.clear();
            emailTextField.clear();
            dialogBox.setTitle("Contact Updated");
            dialogBox.setContentText("Successfully updated contact!");
            dialogBox.showAndWait();

        });

        Scene scene = new Scene(grid, 700, 600);

        scene.getStylesheets().add("style.css");


        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
