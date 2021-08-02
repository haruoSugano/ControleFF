package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Conta;
import model.services.ContaService;

public class ContaListController implements Initializable, DataChangeListener {
	
	private ContaService service;

	@FXML
	private TableView<Conta> tableViewConta;
	
	@FXML
	private TableColumn<Conta, Integer> tableColumnId;
	
	@FXML 
	private TableColumn<Conta, String> tableColumnName;
	
	@FXML
	private TableColumn<Conta, Conta> tableColumnEDIT;
	
	@FXML
	private TableColumn<Conta, Conta> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Conta> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Conta conta = new Conta();
		createDialogForm(conta,"/gui/ContaForm.fxml", parentStage);
	}
	
	public void setContaService(ContaService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		/**
		 * Corrigindo o redimensionamento da tableViewConta
		 */
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewConta.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Conta> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewConta.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createDialogForm(Conta conta, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ContaFormController controller = loader.getController();
			controller.setConta(conta);
			controller.setContaService(new ContaService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Conta data");//Titulo da janela
			dialogStage.setScene(new Scene(pane));//Nova cena que sera o pane
			dialogStage.setResizable(false);//A janela não poode ser redimensionada
			dialogStage.initOwner(parentStage);//Quem será o pai desta janela
			dialogStage.initModality(Modality.WINDOW_MODAL);//Restringindo o acesso, apenas na janela aberta.
			dialogStage.showAndWait();//
			
		}
		catch(IOException e) {
			e.getStackTrace();
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Conta, Conta>(){
			private final Button button = new Button("Editar");
			
			@Override
			protected void updateItem(Conta conta, boolean empty) {
				super.updateItem(conta, empty);
				
				if(conta == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(conta, "/gui/ContaForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Conta, Conta> (){
			private final Button button = new Button("Remover");
			
			@Override
			protected void updateItem(Conta conta, boolean empty) {
				super.updateItem(conta, empty);
				
				if(conta == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(conta));
				
			}

		});
	}
	
	private void removeEntity(Conta conta) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remover(conta);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing contaect", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
