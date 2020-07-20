package application;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Categoria;
import model.Gasto;
import model.Grupo;
import model.Orcamento;
import service.CategoriaService;
import service.GastoService;
import util.Response;

public class DespesaController implements Initializable{

	private GastoService service;
	private CategoriaService categoriaService;
	private Gasto gasto;
	private Grupo grupo;
	private Orcamento orcamento;
	private Stage stage;
	private GastoController controllerPai;
	
	List<Categoria> categorias;
	ObservableList<Categoria> observableListCategorias;
	
	public DespesaController() {
		this.categoriaService = new CategoriaService();
	}

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtValor;

    @FXML
    private DatePicker dtData;

    @FXML
    private TextField txtCartao;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    @FXML
    private Button btnSalvar;

    @FXML
    void cancelar(ActionEvent event) {
    	this.stage.close();
    }

    @FXML
    void salvar(ActionEvent event) {
    	Response resp = new Response(false, "Erro ao salvar");
    	if(this.gasto.getId() == null) {
    		resp = this.service.save(
    				this.txtDescricao.getText(),
    				this.txtValor.getText().replace(",", "."),
    				this.dtData.getValue(),
    				this.comboStatus.getValue(),
    				this.txtCartao.getText(),
    				this.comboCategoria.getValue().getId(),
    				this.orcamento.getId()
    				);
    				
    	}else {
      		resp = this.service.update(
      				this.gasto.getId(),
    				this.txtDescricao.getText(),
    				this.txtValor.getText().replace(",", "."),
    				this.dtData.getValue(),
    				this.comboStatus.getValue(),
    				this.txtCartao.getText(),
    				this.comboCategoria.getValue().getId(),
    				this.orcamento.getId()
    				);
    	}
    	
    	
    	if(resp.getSuccess()) {
    		this.controllerPai.loadGastos();
    		this.stage.close();
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erro");
    		alert.setContentText(resp.getMessage());
    		alert.showAndWait();
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> status =  new ArrayList<String>();
		status.add("BAIXADO");
		status.add("CONFIRMADO");
		status.add("PENDENTE");
		ObservableList<String> obs = FXCollections.observableArrayList(status);
		this.comboStatus.setItems(obs);
	}
	
	private void loadCategorias() {
		this.categorias = categoriaService.getAll()
				.stream()
				.filter(c -> c.getGrupo().getId().equals(this.grupo.getId()))
				.collect(Collectors.toList());
		this.observableListCategorias = FXCollections.observableArrayList(this.categorias);
		this.comboCategoria.setItems(this.observableListCategorias);
	}
	

	public void setService(GastoService service) {
		this.service = service;
	}

	public void setGasto(Gasto gasto) {
		this.gasto = gasto;
		if(gasto.getId() != null) {
			this.lblTitle.setText("Editar");
			this.preencherForm(gasto);
		}else {
			this.lblTitle.setText("Novo");
		}
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
		this.loadCategorias();
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setControllerPai(GastoController controllerPai) {
		this.controllerPai = controllerPai;
	}
	
	private void preencherForm(Gasto gasto) {
		this.txtDescricao.setText(gasto.getDescricao());
		
		this.dtData.setValue(gasto.getDataToLocalDate());
		this.txtValor.setText(gasto.getValor().toString());
		
		this.comboStatus.setValue(gasto.getStatus());
		if(gasto.getStatus().equals("BAIXADO")) {
			this.comboStatus.setDisable(true);
		}
		
		this.comboCategoria.setValue(gasto.getCategoria());
		
		if(gasto.getCartao() != null)
			this.txtCartao.setText(gasto.getCartao());
	}
}
