package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Contraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	private SellerService  service;	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dbtBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private TextField txtDepartment;
	@FXML
	private ComboBox<Department> comboBoxdepartment;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorbaseSalary;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	private ObservableList<Department> obsList;
	
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		
		}catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		}
		catch( DbException e) {
			Alerts.showAlerts("Error saving object", null,e.getMessage() , AlertType.ERROR);
		}
	}
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
		
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		if(dbtBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}else {
		Instant instant = Instant.from(dbtBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setBirthDate(Date.from(instant));
		}
		
		if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(comboBoxdepartment.getValue());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	@FXML
	public void onBtCancelAction() {
		System.out.println("bt save action cancel");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	private void initializeNodes() {
		Contraints.setTextFieldInteger(txtId);
		Contraints.setTextFielsMaxLength(txtName, 30);
		Contraints.setTextFielsMaxLength(txtEmail, 20);
		Contraints.setTextFieldDouble(txtBaseSalary);
		Contraints.setTextFielsMaxLength(txtDepartment, 10);
		Utils.formatDatePicker(dbtBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
		txtDepartment.setText(String.valueOf(entity.getDepartment()));
		dbtBirthDate.setValue(LocalDate.ofEpochDay(entity.getBirthDate().getTime()));
		//if(entity.getBirthDate() != null){
		//dbtBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		//}
		if(entity.getDepartment() == null) {
			comboBoxdepartment.getSelectionModel().selectFirst();
		}
		comboBoxdepartment.setValue(entity.getDepartment());
	}
	
	public void loadAssociateObjects() {
		if (departmentService == null) {
			throw new  IllegalStateException("Department Service was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableList(list);
		comboBoxdepartment.setItems(obsList);
	}
	
	private void setErrorMessage(Map<String, String> error) {
		Set<String> fields = error.keySet();
		
		
		labelErrorName.setText(fields.contains("name") ? error.get("name"): "");
		
		labelErrorEmail.setText(fields.contains("email") ? error.get("email") : "");
		
		labelErrorbaseSalary.setText(fields.contains("baseSalary") ? error.get("baseSalary") : "");
		
		labelErrorBirthDate.setText(fields.contains("birthDate") ? error.get("birthDate") : "");
		
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxdepartment.setCellFactory(factory);
		comboBoxdepartment.setButtonCell(factory.call(null));
	}
}
