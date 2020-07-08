package application;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
import model.Grupo;
import model.InvestimentoFixo;
import service.InvestimentoFixoService;
import util.Response;

public class FixoController implements Initializable{
	
	private InvestimentoFixoService service;
	private InvestimentoFixo investimento;
	private Grupo grupo;
	private Stage stage;
	private InvestimentoController controllerPai;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtAtivo;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private DatePicker dtCompra;

    @FXML
    private TextField txtValorAplicado;

    @FXML
    private DatePicker dtVencimento;

    @FXML
    private TextField txtValorLiquido;

    @FXML
    private ComboBox<String> comboResgatado;

    @FXML
    private TextField txtValorResgate;

    @FXML
    private DatePicker dtResgate;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    void cancelar(ActionEvent event) {
    	this.stage.close();
    }

    @FXML
    void salvar(ActionEvent event) {
    	ZoneId defaultZoneId = ZoneId.systemDefault();
    	Date dataResgate = null;
    	Double valorResgate = null;
    	
    	if(this.dtResgate.getValue() != null)
    		 dataResgate = Date.from(this.dtResgate.getValue().atStartOfDay(defaultZoneId).toInstant());
    	
    	if(!this.txtValorResgate.getText().isEmpty())
    		valorResgate = new Double(this.txtValorResgate.getText().replace(",", "."));
    	
    	
    	
    	
    	
    	Date dataCompra = Date.from(this.dtCompra.getValue().atStartOfDay(defaultZoneId).toInstant());
    	Date dataVenciemnto = Date.from(this.dtVencimento.getValue().atStartOfDay(defaultZoneId).toInstant());
    	
    	Response resp = new Response(false, "Erro ao salvar");

    	if(this.investimento.getId() == null) {
    		resp = this.service.save(
    				this.txtAtivo.getText(),
    				this.txtDescricao.getText(),
    				this.txtQuantidade.getText(),
    				dataCompra,
    				this.txtValorAplicado.getText().replace(",", "."),
    				this.txtValorLiquido.getText().replace(",", "."),
    				valorResgate,
    				dataResgate,
    				dataVenciemnto,
    				this.comboResgatado.getValue(),
    				this.grupo.getId()
    				);
    				
    	}else {
      		resp = this.service.update(
      				this.investimento.getId(),
    				this.txtAtivo.getText(),
    				this.txtDescricao.getText(),
    				this.txtQuantidade.getText(),
    				dataCompra,
    				this.txtValorAplicado.getText().replace(",", "."),
    				this.txtValorLiquido.getText().replace(",", "."),
    				valorResgate,
    				dataResgate,
    				dataVenciemnto,
    				this.comboResgatado.getValue(),
    				this.grupo.getId()
    				);
    	}
    	
    	
    	if(resp.getSuccess()) {
    		this.controllerPai.loadInvestimentos();
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
		List<String> resgatado =  new ArrayList<String>();
		resgatado.add("Sim");
		resgatado.add("Não");
		ObservableList<String> obs = FXCollections.observableArrayList(resgatado);
		this.comboResgatado.setItems(obs);
	}
	
	public void setService(InvestimentoFixoService service) {
		this.service = service;
	}

	public void setInvestimento(InvestimentoFixo investimento) {
		this.investimento = investimento;
		if(investimento.getId() == null) {
			this.lblTitle.setText("Novo");
		}else {
			this.lblTitle.setText("Editar");
			this.preencherForm(investimento);
		}
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setControllerPai(InvestimentoController controllerPai) {
		this.controllerPai = controllerPai;
	}
	
	private void preencherForm(InvestimentoFixo investimento) {
		
		this.txtAtivo.setText(investimento.getAtivo());
		this.txtDescricao.setText(this.investimento.getDescricao());
		this.txtQuantidade.setText(investimento.getQuantidade().toString());
		this.dtCompra.setValue(investimento.getDataCompra().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate());
	
	
		this.txtValorAplicado.setText(investimento.getValorAplicado().toString());
		this.txtValorLiquido.setText(investimento.getValorLiquido().toString());
		this.dtVencimento.setValue(investimento.getVencimento().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate());
		
		if(investimento.getResgatado().equals("S"))
			this.comboResgatado.setValue("Sim");
		else
			this.comboResgatado.setValue("Não");
		
		if(investimento.getDataResgate() != null)
			this.dtResgate.setValue(investimento.getDataResgate().toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate());
		if(investimento.getValorResgate() != null)
			this.txtValorResgate.setText(investimento.getValorResgate().toString());
	}

}
