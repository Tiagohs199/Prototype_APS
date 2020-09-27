package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Contraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	@FXML
	public void onBtSaveAction() {
		System.out.println("Button Save");
	}
	@FXML
	public void onBtCancelAction() {
		System.out.println("Button Cancel");
	}
	
	
	
	@Override
	public void initialize(URL urln, ResourceBundle rb) {
		initializeNodes();
	}
	private void initializeNodes() {
		Contraints.setTextFieldInteger(txtId);
		Contraints.setTextFielsMaxLength(txtName, 20);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
		
		
		
	}
}