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
import javafx.stage.Stage;
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
    private JFXTextField tfCustomerID;

    @FXML
    private JFXTextArea txtAreaComment;

    @FXML
    private Button btnEnterCustomerID;

    @FXML
    private Text textCustomerName;

    @FXML
    private Text textCustomerAddress;

    @FXML
    private Text textCustomerPhone;

    @FXML
    private Text textCustomerJoinedDate;

    @FXML
    private JFXTextField tfTitleName;

    @FXML
    private Button btnEnterTitleID;

    @FXML
    private ComboBox<Title> cbTitle;

    @FXML
    private ImageView imgTitle;

    @FXML
    private Label txtTitleName;

    @FXML
    private Label txtTitleType;

    @FXML
    private JFXTextArea txtAreaDescription;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnDone;

    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        titleDAO = main.getTitleDAO();
        btnEnterCustomerID.setOnAction(e -> {
           searchForCustomer();
        });
        tfCustomerID.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                searchForCustomer();
            }
        });
        btnDone.setOnAction(e -> {
           if(confirmReservation()){
               //?
           }
        });
        btnCancel.setOnAction(e -> {
            if (requestConfirmExit()) {
                Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
                stage.close();
            }
        });
        tfTitleName.setOnKeyTyped(e -> {
            loadComboTitle(tfTitleName.getText().trim());
        });
        cbTitle.setOnAction(e -> {
            loadTitleInfo(cbTitle.getSelectionModel().getSelectedItem());
        });
        btnReset.setOnAction(e->{
            tfTitleName.setText("");
            txtAreaDescription.setText(null);
            txtTitleType.setText(null);
            cbTitle.getItems().clear();
            txtAreaDescription.setText(null);
            try {
                imgTitle.setImage(new Image(new FileInputStream(new File(main.URL_DEFAULT_POSTER))));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        btnDone.setOnAction(e -> {
            Title title = cbTitle.getSelectionModel().getSelectedItem();
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
                       Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
                       stage.close();
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
            cbTitle.setItems(list);
            cbTitle.getSelectionModel().select(0);
            loadTitleInfo(cbTitle.getSelectionModel().getSelectedItem());
        }

    }
    private void loadTitleInfo(Title title){
        if(title==null)
            return;
        imgTitle.setImage(title.getImage());
        txtTitleName.setText(title.getTitleName());
        txtTitleType.setText(title.getItemClass().getItemClassName());
        txtAreaDescription.setText(title.getDesciption());
    }
    private void searchForCustomer(){
        String customerID = tfCustomerID.getText();
        if (customerID.isEmpty()) {
            tfCustomerID.requestFocus();
            return;
        }
        Customer customer = new CustomerDAO().getById(Customer.class,customerID);
        if (customer != null) {
            textCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
            textCustomerPhone.setText(customer.getPhoneNumber());
            textCustomerAddress.setText(customer.getAddress());
            textCustomerJoinedDate.setText(customer.getJoinedDate().toString());
            this.customer = customer;
        }
        else{
            textCustomerName.setText(null);
            textCustomerPhone.setText(null);
            textCustomerAddress.setText(null);
            textCustomerJoinedDate.setText(null);
            showMessage("Your entered customer's ID does not match","Message",null);
        }
    }
}