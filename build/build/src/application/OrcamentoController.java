package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Orcamento;
import service.OrcamentoService;
import util.Response;

public class OrcamentoController implements Initializable {

	OrcamentoService service;
	List<Orcamento> orcamentos;
	ObservableList<Orcamento> observableListOrcamentos;
	Orcamento selectedOrcamento;

	public OrcamentoController() {
		super();
		this.service = new OrcamentoService();
		this.orcamentos = service.getAll(false);
	}

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnRemover;

	@FXML
	private TableView<Orcamento> tblOrcamento;

	@FXML
	private TableColumn<Orcamento, Integer> clnId;

	@FXML
	private TableColumn<Orcamento, String> clnDescricao;

	@FXML
	private TableColumn<Orcamento, Date> cnlDataInicail;

	@FXML
	private TableColumn<Orcamento, Date> cnlDataFinal;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private DatePicker dtFinal;

	@FXML
	private DatePicker dtInicial;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCopiar;

	@FXML
	void cancelar(ActionEvent event) {
		this.resetForm();
	}

	@FXML
	void copiar(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Copiar");
		alert.setHeaderText("Confirmação");
		alert.setContentText(
				"Ao copiar, dados do último Orçamento cadastrado será\ninserido para o orçamento selecionado.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Response resp = this.service.copiar(this.selectedOrcamento);
			if (resp.getSuccess()) {
				Alert alertRemove = new Alert(AlertType.INFORMATION);
				alertRemove.setTitle("Sucesso");
				alertRemove.setContentText(resp.getMessage());
				alertRemove.showAndWait();
				this.orcamentos = service.getAll(false);
				this.loadOrcamentos();
				resetForm();
			} else {
				Alert alertRemove = new Alert(AlertType.ERROR);
				alertRemove.setTitle("Erro");
				alertRemove.setContentText(resp.getMessage());
				alertRemove.showAndWait();
			}
			
		}
		
		this.resetForm();

	}

	@FXML
	void editar(ActionEvent event) {
		this.disableForm(false);
		this.txtId.setDisable(true);
		this.btnSalvar.setDisable(false);
		this.btnCancelar.setDisable(false);
		this.btnNovo.setDisable(true);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);

	}

	@FXML
	void novo(ActionEvent event) {
		this.disableForm(false);
		this.txtId.setDisable(true);
		this.clearForm();
		this.btnSalvar.setDisable(false);
		this.btnCancelar.setDisable(false);
		this.btnNovo.setDisable(true);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);
	}

	@FXML
	void remover(ActionEvent event) {
		this.btnNovo.setDisable(true);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remover");
		alert.setHeaderText("Confirmação");
		alert.setContentText("Deseja remover este orçamento?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Response resp = this.service.removeById(new Integer(this.txtId.getText()));

			if (resp.getSuccess()) {
				this.orcamentos = service.getAll(false);
				this.loadOrcamentos();
				resetForm();
			} else {
				Alert alertRemove = new Alert(AlertType.ERROR);
				alertRemove.setTitle("Erro");
				alertRemove.setContentText(resp.getMessage());
				alertRemove.showAndWait();
			}

		}

		this.resetForm();
	}

	@FXML
	void salvar(ActionEvent event) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date dataini = Date.from(this.dtInicial.getValue().atStartOfDay(defaultZoneId).toInstant());
		Date datafim = Date.from(this.dtFinal.getValue().atStartOfDay(defaultZoneId).toInstant());
		Response resp = new Response(false, "Erro ao salvar");
		if (this.txtId.getText().isEmpty()) {
			resp = this.service.save(this.txtDescricao.getText(), dataini, datafim);
		} else {
			resp = this.service.update(new Integer(this.txtId.getText()), this.txtDescricao.getText(), dataini,
					datafim);
		}

		if (resp.getSuccess()) {
			this.orcamentos = service.getAll(false);
			this.loadOrcamentos();
			resetForm();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setContentText(resp.getMessage());
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.loadOrcamentos();

		this.tblOrcamento.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.preencherFormulario(newValue));

	}

	private void loadOrcamentos() {
		this.clnId.setCellValueFactory(new PropertyValueFactory<Orcamento, Integer>("id"));
		this.clnDescricao.setCellValueFactory(new PropertyValueFactory<Orcamento, String>("descricao"));
		this.cnlDataInicail.setCellValueFactory(new PropertyValueFactory<Orcamento, Date>("dataInicial"));
		this.cnlDataFinal.setCellValueFactory(new PropertyValueFactory<Orcamento, Date>("dataFinal"));
		this.cnlDataInicail.setCellFactory(column -> this.formatColumn(column));
		this.cnlDataFinal.setCellFactory(column -> this.formatColumn(column));

		this.observableListOrcamentos = FXCollections.observableList(this.orcamentos);
		this.tblOrcamento.setItems(this.observableListOrcamentos);
		this.tblOrcamento.refresh();

	}

	private void preencherFormulario(Orcamento orcamento) {
		if(orcamento == null)
			return;
					
		this.txtId.setText(orcamento.getId().toString());
		this.txtDescricao.setText(orcamento.getDescricao());
		this.dtInicial.setValue(orcamento.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		this.dtFinal.setValue(orcamento.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
		this.btnCopiar.setDisable(false);
		this.selectedOrcamento = orcamento;
	}

	private void disableForm(boolean bool) {
		this.txtId.setDisable(bool);
		this.txtDescricao.setDisable(bool);
		this.dtFinal.setDisable(bool);
		this.dtInicial.setDisable(bool);
		this.btnCancelar.setDisable(bool);
		this.btnSalvar.setDisable(bool);
		this.btnCopiar.setDisable(bool);
	}

	private void clearForm() {
		this.txtId.clear();
		this.txtDescricao.clear();
		this.dtFinal.setValue(null);
		this.dtInicial.setValue(null);
	}

	private void resetForm() {
		this.disableForm(true);
		this.clearForm();
		this.tblOrcamento.getSelectionModel().clearSelection();
		this.btnNovo.setDisable(false);
	}

	private TableCell<Orcamento, Date> formatColumn(TableColumn<Orcamento, Date> column) {
		TableCell<Orcamento, Date> cell = new TableCell<Orcamento, Date>() {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			@Override
			protected void updateItem(Date item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null)
						this.setText(format.format(item));
				}
			}
		};

		return cell;
	}

}
