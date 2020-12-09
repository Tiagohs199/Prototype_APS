package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

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
	private TableColumn<Seller, Seller> tableColumnEdit;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnRemove;
	
	@FXML
	private Button btExit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtExitAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
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
		Utils.formatTableColumnDate(TableColumnBirthDate, "dd/MM/yyyy");
		TableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(TableColumnBaseSalary, 2);
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
	
	public void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociateObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IO Exception", "Erro load View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		// TODO Auto-generated method stub
		
	}
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>(){
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	private void  removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ? ");
		
		if (result.get()== ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
			service.remove(obj);
			updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlerts("error removing object", null, e.getMessage(), AlertType.ERROR);
				
			}
		}
	}
}
