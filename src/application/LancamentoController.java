package application;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import model.Grupo;
import model.InvestimentoFixo;
import model.InvestimentoVariavel;
import model.Lancamento;
import model.Objetivo;
import model.Orcamento;
import service.CategoriaService;
import service.InvestimentoFixoService;
import service.InvestimentoVariavelService;
import service.ObjetivoService;
import util.Investimento;
import util.Response;

public class LancamentoController implements Initializable{
	private CategoriaService categoriaService;
	List<Categoria> categorias;
	ObservableList<Categoria> observableListCategorias;
	
	private ObjetivoService service;
	List<Objetivo> objetivos;
	ObservableList<Objetivo> observableListObjetivo;
	
	InvestimentoVariavelService investimentoVariavelService;
	InvestimentoFixoService investimentoFixoService;
	
	List<Investimento> investimentos;
	ObservableList<Investimento> observableListInvestimentos;

	private Grupo grupo;
	
	private Orcamento orcamento;
	private Stage stage;
	private GastoController controllerPai;
	private Lancamento lancamento;
	
    public LancamentoController() {
    	this.categoriaService = new CategoriaService();
    	this.investimentoFixoService = new InvestimentoFixoService();
    	this.investimentoVariavelService = new InvestimentoVariavelService();
	}

	@FXML
    private Label lblTitle;

    @FXML
    private TextField txtValor;

    @FXML
    private DatePicker dtData;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    @FXML
    private ComboBox<Objetivo> comboObjetivo;

    @FXML
    private ComboBox<String> comboTipo;
    
    @FXML
    private ComboBox<Investimento> comboInvestimento;

    @FXML
    private Button btnSalvar;

    @FXML
    void cancelar(ActionEvent event) {
    	this.stage.close();
    }

    @FXML
    void salvar(ActionEvent event) {
    	ZoneId defaultZoneId = ZoneId.systemDefault();
    	Date data = Date.from(this.dtData.getValue().atStartOfDay(defaultZoneId).toInstant());
    	Response resp = new Response(false, "Erro ao salvar");
    	
    	lancamento.setValor(new Double(this.txtValor.getText().replace(",", ".")));
    	lancamento.setData(data);
    	lancamento.setCategoria(this.comboCategoria.getValue());
    	
    	lancamento.setTipoInvestimento(this.comboTipo.getValue());
    	lancamento.setOrcamento(this.orcamento);
    	lancamento.setObjetivo(this.comboObjetivo.getValue());
    	if(this.comboInvestimento.getValue() != null) {
    		lancamento.setIdInvestimento(this.comboInvestimento.getValue().getId());
    		lancamento.setDescricaoInv(this.comboInvestimento.getValue().getDescricao());
    	}
    		
    	
    	if(lancamento.getId() == null)
    		resp = this.service.saveLancamento(lancamento);
    	else 
    		resp = this.service.saveLancamento(lancamento);
    	
    	if(resp.getSuccess()) {
    		this.controllerPai.loadLancamnetos();
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
		List<String> tipo =  new ArrayList<String>();
		tipo.add("FIXO");
		tipo.add("VARIAVEL");
		ObservableList<String> obs = FXCollections.observableArrayList(tipo);
		this.comboTipo.setItems(obs);
		this.comboInvestimento.setDisable(true);
		
		this.comboTipo.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> this.loadInvestimentos(newValue));
		
		this.comboTipo.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> this.loadInvestimentos(newValue));
	}
	
	private void loadCategorias() {
		this.categorias = categoriaService.getAll()
				.stream()
				.filter(c -> c.getGrupo().getId().equals(this.grupo.getId()))
				.collect(Collectors.toList());
		this.observableListCategorias = FXCollections.observableArrayList(this.categorias);
		this.comboCategoria.setItems(this.observableListCategorias);
	}
	
	private void loadInvestimentos(String tipo) {
		this.comboInvestimento.setValue(null);
		
		if(tipo.equals("FIXO")) {
			List<InvestimentoFixo> fixos = this.investimentoFixoService.getAll();
			this.investimentos = fixos
					.stream()
					.map(ivf -> new Investimento(ivf.getId(), ivf.getDescricao()))
					.collect(Collectors.toList());
		}else {
			List<InvestimentoVariavel> variaveis =  this.investimentoVariavelService.getAll();
			this.investimentos = variaveis
					.stream()
					.map(ivf -> new Investimento(ivf.getId(), ivf.getDescricao()))
					.collect(Collectors.toList());
		}
		
		this.observableListInvestimentos = FXCollections.observableArrayList(this.investimentos);
		this.comboInvestimento.setItems(this.observableListInvestimentos);
		this.comboInvestimento.setDisable(false);
	}
	

	
	private void loadObjetivos() {
		this.objetivos = this.service.getAll();
		this.observableListObjetivo = FXCollections.observableArrayList(this.objetivos);
		this.comboObjetivo.setItems(this.observableListObjetivo);
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
	
	private void preencherForm(Lancamento lancamento) {
		this.dtData.setValue(lancamento.getData().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate());
		this.txtValor.setText(lancamento.getValor().toString());
		this.comboTipo.setValue(lancamento.getTipoInvestimento());
		this.comboCategoria.setValue(lancamento.getCategoria());
		this.comboObjetivo.setValue(lancamento.getObjetivo());
		this.comboInvestimento.setValue(lancamento.getInvestimento());
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
		if(lancamento.getId() == null) {
			this.lblTitle.setText("Novo");
		}else {
			this.lblTitle.setText("Editar");
			this.preencherForm(lancamento);
		}
	}

	public void setService(ObjetivoService service) {
		this.service = service;
		this.loadObjetivos();
	}

}
