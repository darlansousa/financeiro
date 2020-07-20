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
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.Grupo;
import model.InvestimentoVariavel;
import service.InvestimentoVariavelService;
import util.Response;
import util.Uteis;

public class VariavelController implements Initializable {

	private InvestimentoVariavelService service;
	private InvestimentoVariavel investimento;
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
	private TextField txtValorUnidade;

	@FXML
	private DatePicker dtVenda;

	@FXML
	private TextField txtValorVenda;

	@FXML
	private ComboBox<String> comboVendido;

	@FXML
	private Button btnSalvar;

	@FXML
	void cancelar(ActionEvent event) {
		this.stage.close();
	}

	@FXML
	void salvar(ActionEvent event) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date dataVenda = null;
		Double valorVenda = null;
		if (this.dtVenda.getValue() != null)
			dataVenda = Date.from(this.dtVenda.getValue().atStartOfDay(defaultZoneId).toInstant());

		if (!this.txtValorVenda.getText().isEmpty()) {
			if (!Uteis.isNumeric(this.txtValorVenda.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setContentText("Valor inválido");
				alert.showAndWait();
				return;
			}
			valorVenda = new Double(this.txtValorVenda.getText().replace(",", "."));

		}

		Date dataCompra = Date.from(this.dtCompra.getValue().atStartOfDay(defaultZoneId).toInstant());

		Response resp = new Response(false, "Erro ao salvar");

		if (this.investimento.getId() == null) {
			resp = this.service.save(this.txtAtivo.getText(), this.txtDescricao.getText(), this.txtQuantidade.getText(),
					dataCompra, this.txtValorUnidade.getText().replace(",", "."), dataVenda, valorVenda,
					this.comboVendido.getValue(), this.grupo.getId());

		} else {
			resp = this.service.update(this.investimento.getId(), this.txtAtivo.getText(), this.txtDescricao.getText(),
					this.txtQuantidade.getText(), dataCompra, this.txtValorUnidade.getText().replace(",", "."),
					dataVenda, valorVenda, this.comboVendido.getValue(), this.investimento.getValorUnidadeAtual(),
					this.investimento.getDataAtualizacao(), this.grupo.getId());
		}

		if (resp.getSuccess()) {
			this.controllerPai.loadInvestimentos();
			this.stage.close();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setContentText(resp.getMessage());
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> vendido = new ArrayList<String>();
		vendido.add("Sim");
		vendido.add("Não");
		ObservableList<String> obs = FXCollections.observableArrayList(vendido);
		this.comboVendido.setItems(obs);

	}

	public void setService(InvestimentoVariavelService service) {
		this.service = service;
	}

	public void setInvestimento(InvestimentoVariavel investimento) {
		this.investimento = investimento;
		if (investimento.getId() == null) {
			this.lblTitle.setText("Novo");
		} else {
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

	private void preencherForm(InvestimentoVariavel investimento) {
		this.txtAtivo.setText(investimento.getAtivo());
		this.txtDescricao.setText(this.investimento.getDescricao());
		this.txtQuantidade.setText(investimento.getQuantidade().toString());
		this.dtCompra.setValue(investimento.getDataCompra().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

		this.txtValorUnidade.setText(investimento.getValorUnidade().toString());

		if (investimento.getVendido().equals("S"))
			this.comboVendido.setValue("Sim");
		else
			this.comboVendido.setValue("Não");

		if (investimento.getDataVenda() != null)
			this.dtVenda.setValue(investimento.getDataVenda().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		if (investimento.getValorVenda() != null)
			this.txtValorVenda.setText(investimento.getValorVenda().toString());
	}

}
