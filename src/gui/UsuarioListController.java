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
import javafx.beans.property.SimpleObjectProperty;
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
import model.entities.Usuario;
import model.services.ContaService;
import model.services.UsuarioService;

public class UsuarioListController implements Initializable, DataChangeListener {
	
	private UsuarioService service;

	@FXML
	private TableView<Usuario> tableViewUsuario;
	
	@FXML
	private TableColumn<Usuario, Integer> tableColumnId;
	
	@FXML 
	private TableColumn<Usuario, String> tableColumnName;
	
	@FXML
	private TableColumn<Usuario, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Usuario, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Usuario, String> tableColumnConta;
	
	@FXML
	private TableColumn<Usuario, Usuario> tableColumnEDIT;
	
	@FXML
	private TableColumn<Usuario, Usuario> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Usuario> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Usuario usuario = new Usuario();
		createDialogForm(usuario,"/gui/UsuarioForm.fxml", parentStage);
	}
	
	public void setUsuarioService(UsuarioService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		/**
		 * buscar na tabela conta, e inserir na tabela usuario o nome da conta. 
		 */
		tableColumnConta.setCellValueFactory(Usuario -> {
			SimpleObjectProperty<String> property = new SimpleObjectProperty<String>();
			property.setValue(Usuario.getValue().getConta().getNome());
			return property;
		});
		
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		
		/**
		 * Corrigindo o redimensionamento da tableViewUsuario
		 */
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewUsuario.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Usuario> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewUsuario.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createDialogForm(Usuario usuario, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			UsuarioFormController controller = loader.getController();
			controller.setUsuario(usuario);
			controller.setService(new UsuarioService(), new ContaService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Usuario data");//Titulo da janela
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Usuario, Usuario>(){
			private final Button button = new Button("Editar");
			
			@Override
			protected void updateItem(Usuario usuario, boolean empty) {
				super.updateItem(usuario, empty);
				
				if(usuario == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(usuario, "/gui/UsuarioForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Usuario, Usuario> (){
			private final Button button = new Button("Remover");
			
			@Override
			protected void updateItem(Usuario usuario, boolean empty) {
				super.updateItem(usuario, empty);
				
				if(usuario == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(usuario));
				
			}

		});
	}
	
	private void removeEntity(Usuario usuario) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remover(usuario);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing usuarioect", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
