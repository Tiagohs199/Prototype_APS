package gui;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable {

	private SellerService service;
	@FXML
	private TableView<Seller> TableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> TableColumnId;
	@FXML
	private TableColumn<Seller, String> TableColumnName;
	@FXML
	private TableColumn<Seller, String> TableColumnEmail;
	@FXML
	private TableColumn<Seller, Date> TableColumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> TableColumnBaseSalary;
	@FXML
	private TableColumn<Seller, Integer> TableColumnDepartmentId;
	
	@FXML
	private Button btExit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtExitAction() {
		System.out.println("Exiting");
	}
	
	@FXML
	public void onBtNewAction() {
		System.out.println("bt new Seller");
	}
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeSellerNodes();
	}
	
	private void initializeSellerNodes() {
		TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		TableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		TableColumnDepartmentId.setCellValueFactory(new PropertyValueFactory<>("department"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		TableViewSeller.prefHeightProperty().bind(stage.heightProperty());
		
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException();
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		TableViewSeller.setItems(obsList);
	}
	
}
