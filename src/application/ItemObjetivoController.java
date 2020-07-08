package application;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ItemObjetivo;
import model.Objetivo;
import service.ObjetivoService;
import util.Uteis;
import util.Response;

public class ItemObjetivoController implements Initializable{
	
	private Objetivo objetivo;
	private ObjetivoService service;
	private ObjetivoController controllerPai;
	
	ObservableList<ItemObjetivo> obsItens;
	List<ItemObjetivo> itens;
	ItemObjetivo selectedItem;
	
	

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnRemover;

    @FXML
    private Label lblObjetivoTitle;

    @FXML
    private TableView<ItemObjetivo> tblItens;

    @FXML
    private TableColumn<ItemObjetivo, String> clnDescricao;

    @FXML
    private TableColumn<ItemObjetivo, Double> clnValor;

    @FXML
    private TextField txtDescricao;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField txtValor;

    @FXML
    void cancelar(ActionEvent event) {
    	this.resetForm();
    }

    @FXML
    void novo(ActionEvent event) {
    	this.disableForm(false);
    	this.clearForm();
    	this.btnSalvar.setDisable(false);
    	this.btnCancelar.setDisable(false);
    	this.btnNovo.setDisable(true);
     	this.btnRemover.setDisable(true);
    }

    @FXML
    void remover(ActionEvent event) {
    	this.btnNovo.setDisable(true);
     	this.btnRemover.setDisable(true);
     	
     	if(this.selectedItem == null) {
     		return;
     	}
     	
     	Response resp = this.service.removeItemById(this.selectedItem.getId());
     	
     	if(resp.getSuccess()) {
     		this.objetivo.getItems().remove(this.selectedItem);
     		loadItems();
     		this.resetForm();
     	}else {
     		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erro");
    		alert.setContentText(resp.getMessage());
    		alert.showAndWait();
     	}
    	
    	

    }

    @FXML
    void salvar(ActionEvent event) {
    	ItemObjetivo item = new ItemObjetivo(this.txtDescricao.getText(),new Double(this.txtValor.getText()), this.objetivo);
    	
    	Response resp = this.service.saveItem(item);
    	
    	if(resp.getSuccess()) {
    		if(this.objetivo.getItems() == null || this.objetivo.getItems().isEmpty())
    			this.objetivo.setItems(Arrays.asList(item));
    		else
    			this.objetivo.getItems().add(item);
    		loadItems();
        	this.resetForm();
        	this.controllerPai.preencherFormulario(this.objetivo);
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erro");
    		alert.setContentText(resp.getMessage());
    		alert.showAndWait();
    	}
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.tblItens.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> this.preencherFormulario(newValue));
	}
	
	public ItemObjetivoController() {
		super();
	}
	
	public void loadItems() {
		this.itens = this.service.getAllItens()
				.stream()
				.filter(io -> io.getObjetivo().getId().equals(this.objetivo.getId()))
				.collect(Collectors.toList());

		this.clnDescricao.setCellValueFactory(new PropertyValueFactory<ItemObjetivo,String>("descricao"));
		this.clnValor.setCellValueFactory(new PropertyValueFactory<ItemObjetivo,Double>("valor"));
		this.clnValor.setCellFactory(column -> this.formatColumnCurrency(column));
		this.obsItens = FXCollections.observableList(this.itens);
		this.tblItens.setItems(this.obsItens);
		this.tblItens.refresh();
	}
	

	public Objetivo getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
		this.lblObjetivoTitle.setText("Planejamento: " + objetivo.getDescricao());
	}

	public ObjetivoService getService() {
		return service;
	}

	public void setService(ObjetivoService service) {
		this.service = service;
		this.loadItems();
	}
	
	private  TableCell<ItemObjetivo, Double> formatColumnCurrency(TableColumn<ItemObjetivo, Double> column){
		TableCell<ItemObjetivo, Double> cell = new TableCell<ItemObjetivo, Double>() {

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if(item != null)
                    this.setText(Uteis.convertToCurrency(item));
                }
            }
        };

        return cell;
	}
	
	private void disableForm(boolean bool) {
		this.txtDescricao.setDisable(bool);
		this.txtValor.setDisable(bool);
		this.btnCancelar.setDisable(bool);
		this.btnSalvar.setDisable(bool);
	}
	
	private void clearForm() {
		this.txtDescricao.clear();
		this.txtValor.clear();
	}
	
	private void resetForm() {
		this.disableForm(true);
    	this.clearForm();
    	this.btnNovo.setDisable(false);
	}
	
	private void preencherFormulario(ItemObjetivo item) {
		if(item == null) {
			return;
		}
		this.selectedItem = item;
		this.txtDescricao.setText(item.getDescricao());
		this.txtValor.setText(item.getValor().toString());
		this.btnRemover.setDisable(false);
	}

	public void setControllerPai(ObjetivoController controllerPai) {
		this.controllerPai = controllerPai;
	}

}
