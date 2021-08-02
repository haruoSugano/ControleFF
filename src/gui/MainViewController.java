package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.ContaService;
import model.services.UsuarioService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemUsuario;
	
	@FXML
	private MenuItem menuItemConta;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private MenuItem menuItemEntrada;
	
	@FXML
	private MenuItem menuItemDespesa;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@FXML
	public void onMenuItemUsuarioAction() {
		loadView("/gui/UsuarioList.fxml", (UsuarioListController controller) -> {
			controller.setUsuarioService(new UsuarioService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemContaAction() {
		loadView("/gui/ContaList.fxml", (ContaListController controller) -> {
			controller.setContaService(new ContaService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemEntrada() {
		System.out.println("onMenuItemEntrada");
	}
	
	@FXML
	public void onMenuItemDespesa() {
		System.out.println("onMenuItemDespesa");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/Sobre.fxml", x -> {});
	}
	/**
	 * Expressão lambda:
	 * Usada para não repetir métodos com funções parecidas
	 * @param <T>
	 * @param absoluteName
	 * @param initializingAction
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			/**
			 * Será executado esta função:
			 *  loadView("/gui/ContaList.fxml", (ContaListController controller) -> {
				controller.setContaService(new ContaService());
				controller.updateTableView();
				});
			 */
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
