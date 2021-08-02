package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import model.entities.Conta;
import model.entities.Usuario;
import model.exceptions.ValidationException;
import model.services.ContaService;
import model.services.UsuarioService;

public class UsuarioFormController implements Initializable {

	private Usuario entity;
	
	private UsuarioService service;
	
	private ContaService contaService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker txtBirthDate;
	
	@FXML
	private ComboBox<Conta> comboBoxConta;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	
	private ObservableList<Conta> obsList;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving usuarioect", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners ) {
			listener.onDataChanged();
		}
		
	}

	private Usuario getFormData() {
		Usuario usuario = new Usuario();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		usuario.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode estar vazio!");
		}
		usuario.setNome(txtNome.getText());
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "O campo não pode estar vazio!");
		}
		usuario.setEmail(txtEmail.getText());
		
		if(txtBirthDate.getValue() == null) {
			exception.addError("birthDate", "O campo não pode estar vazio!");
		}
		else {
			Instant instant = Instant.from(txtBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			usuario.setBirthDate(Date.from(instant));
		}
		
		usuario.setConta(comboBoxConta.getValue());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		return usuario;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {//Restrições nos campos de textos
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(txtBirthDate, "dd/MM/yyyy");
		
		initializaComboBoxConta();
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void setUsuario(Usuario entity) {
		this.entity = entity;
	}
	
	public void setService(UsuarioService service, ContaService contaService ) {
		this.service = service;
		this.contaService = contaService;
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		if(entity.getBirthDate() != null) {
			txtBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entity.getConta() == null) {
			comboBoxConta.getSelectionModel().selectFirst();
		}
		else {
			comboBoxConta.setValue(entity.getConta());
		}
	}
	
	public void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorNome.setText((fields.contains("nome") ? errors.get("nome") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		
	}
	
	public void loadAssociatedObjects() {
		if(contaService == null) {
			throw new IllegalStateException("ContaService was null");
		}
		List<Conta> list = contaService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxConta.setItems(obsList);
	}
	
	private void initializaComboBoxConta() {
		Callback<ListView<Conta>, ListCell<Conta>> factory = lv -> new ListCell<Conta>() {
			@Override
			protected void updateItem(Conta item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		
		comboBoxConta.setCellFactory(factory);
		comboBoxConta.setButtonCell(factory.call(null));
	}

}
