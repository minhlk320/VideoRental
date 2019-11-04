package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import daos.CustomerDAO;
import daos.ItemDAO;
import daos.ReservationDAO;
import daos.TitleDAO;
import entities.Customer;
import entities.Reservation;
import entities.Title;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import ui.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    private Main main;
    private List<Title> titleList;
    private TitleDAO titleDAO;
    private Customer customer;
    @FXML
    private JFXTextField tf_CustomerID;

    @FXML
    private JFXTextArea txtAreaComment;

    @FXML
    private Button btn_EnterCustomerID;

    @FXML
    private Text text_CustomerName;

    @FXML
    private Text text_CustomerAddress;

    @FXML
    private Text text_CustomerPhone;

    @FXML
    private Text text_CustomerJoinedDate;

    @FXML
    private JFXTextField tf_titleName;

    @FXML
    private Button btn_EnterTitleID;

    @FXML
    private ComboBox<Title> cb_title;

    @FXML
    private ImageView img_Title;

    @FXML
    private Label txt_TitleName;

    @FXML
    private Label txt_TitleType;

    @FXML
    private JFXTextArea txtArea_Description;

    @FXML
    private Button btnReset;

    @FXML
    private Button btn_Done;

    @FXML
    private Button btn_Cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        titleDAO = new TitleDAO();
        btn_EnterCustomerID.setOnAction(e -> {
           searchForCustomer();
        });
        tf_CustomerID.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                searchForCustomer();
            }
        });
        btn_Done.setOnAction(e -> {
           if(confirmReservation()){

           }
        });
        btn_Cancel.setOnAction(e -> {
            if (requestConfirmExit()) {
                main.changeScene(main.SCENE_HOME);
            }
        });
        tf_titleName.setOnKeyTyped(e->{
           loadComboTitle(tf_titleName.getText().trim());
        });
        cb_title.setOnAction(e->{
            loadTitleInfo(cb_title.getSelectionModel().getSelectedItem());
        });
        btnReset.setOnAction(e->{
            tf_titleName.setText("");
            txtArea_Description.setText(null);
            txt_TitleType.setText(null);
            cb_title.getItems().clear();
            txtArea_Description.setText(null);
            try {
                img_Title.setImage(new Image(new FileInputStream(new File(Main.URL_DEFAULT_POSTER))));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        btn_Done.setOnAction(e->{
            Title title = cb_title.getSelectionModel().getSelectedItem();
            if(customer==null)
                showMessage("You haven't chosen a customer","Message",null);
            else if(title==null)
                showMessage("You haven't chosen a title","Message",null);
            else{
                List itemList = new ItemDAO().getAvailableItemForRentByTitle(title.getTitleID());
                if(!itemList.isEmpty())
                    showMessage("Your chosen title has some available item for rent, can not make a reservation for this situation","Message",null);
                else
                   if(confirmReservation()){
                       Reservation reservation = new Reservation();
                       reservation.setCustomer(customer);
                       reservation.setTitle(title);
                       reservation.setReservationDate(LocalDate.now());
                       reservation.setComment(txtAreaComment.getText());
                       System.out.println(new ReservationDAO().save(reservation));
                       showMessage("Reservation has been added!","Message",null);
                       main.changeScene(main.SCENE_RESERVATION);
                   }
            }
        });


    }
    private boolean requestConfirmExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to exit ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            return false;
        }
        if (option.get() == ButtonType.OK) {
            return true;
        }
        if (option.get() == ButtonType.CANCEL) {
            return false;
        }
        return false;
    }
    private void showMessage(String message, String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean confirmReservation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to make this reservation ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            return false;
        }
        if (option.get() == ButtonType.OK) {
            return true;
        }
        if (option.get() == ButtonType.CANCEL) {
            return false;
        }
        return false;
    }
    private void loadComboTitle(String name){
        titleList = titleDAO.getTitleListByName(name);
        if(titleList!=null){
            System.out.println(titleList);
            ObservableList<Title> list = FXCollections.observableArrayList(titleList);
            cb_title.setItems(list);
            cb_title.getSelectionModel().select(0);
            loadTitleInfo(cb_title.getSelectionModel().getSelectedItem());
        }

    }
    private void loadTitleInfo(Title title){
        if(title==null)
            return;
        img_Title.setImage(title.getImage());
        txt_TitleName.setText(title.getTitleName());
        txt_TitleType.setText(title.getItemClass().getItemClassName());
        txtArea_Description.setText(title.getDesciption());
    }
    private void searchForCustomer(){
        String customerID = tf_CustomerID.getText();
        if (customerID.isEmpty()) {
            tf_CustomerID.requestFocus();
            return;
        }
        Customer customer = new CustomerDAO().getById(Customer.class,customerID);
        if (customer != null) {
            text_CustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
            text_CustomerPhone.setText(customer.getPhoneNumber());
            text_CustomerAddress.setText(customer.getAddress());
            text_CustomerJoinedDate.setText(customer.getJoinedDate().toString());
            this.customer = customer;
        }
        else{
            text_CustomerName.setText(null);
            text_CustomerPhone.setText(null);
            text_CustomerAddress.setText(null);
            text_CustomerJoinedDate.setText(null);
            showMessage("Your entered customer's ID does not match","Message",null);
        }
    }
}