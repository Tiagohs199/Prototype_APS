package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Contraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Seller;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private TextField txtDepartment;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("bt save action seller");
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
		Contraints.setTextFieldInteger(txtBirthDate);
		Contraints.setTextFieldDouble(txtBaseSalary);
		Contraints.setTextFielsMaxLength(txtDepartment, 10);
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtBirthDate.setText(String.valueOf(entity.getBirthDate()));
		txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
		txtDepartment.setText(String.valueOf(entity.getDepartment()));
	}

}
