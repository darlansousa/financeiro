package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Categoria;
import model.Lancamento;
import model.Objetivo;

import service.ObjetivoService;
import util.Uteis;
import util.Investimento;
import util.Response;

public class ObjetivoController implements Initializable {

	ObjetivoService service;
	List<Objetivo> objetivos;
	Objetivo objetivo;
	ObservableList<Objetivo> observableListObjetivos;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnRemover;

	@FXML
	private Button verDetalhe;

	@FXML
	private TableView<Objetivo> tblObjetivo;

	@FXML
	private TableColumn<Objetivo, Integer> clnId;

	@FXML
	private TableColumn<Objetivo, String> clnDescricao;

	@FXML
	private TableColumn<Objetivo, Double> clnValor;

	@FXML
	private TableColumn<Objetivo, Date> clnInicio;

	@FXML
	private TableColumn<Objetivo, Date> clnQuando;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private DatePicker dtQuando;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnLancamentos;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtPlanejado;

	@FXML
	private TextField txtMeses;

	@FXML
	private TextField txtTotal;

	@FXML
	private TextField txtAndaFalta;

	@FXML
	private ProgressBar barConcluido;

	@FXML
	private ColorPicker pickCor;

	@FXML
	private TextField txtOrcado;

	@FXML
	private Label lblPercentual;

	@FXML
	void cancelar(ActionEvent event) {
		this.resetForm();
	}

	@FXML
	void openLancamentos(ActionEvent event) {
		TableView<Lancamento> tableView = new TableView<Lancamento>();
		TableColumn<Lancamento, Objetivo> clnObjetivo = new TableColumn<Lancamento, Objetivo>();
		clnObjetivo.setCellValueFactory(new PropertyValueFactory<Lancamento, Objetivo>("objetivo"));
		clnObjetivo.setText("Objetivo");
		clnObjetivo.setPrefWidth(300);
		tableView.getColumns().add(clnObjetivo);

		TableColumn<Lancamento, Double> clnValorL = new TableColumn<Lancamento, Double>();
		clnValorL.setCellValueFactory(new PropertyValueFactory<Lancamento, Double>("valor"));
		clnValorL.setText("Valor");
		clnValorL.setPrefWidth(150);
		clnValorL.setCellFactory(column -> this.formatColumnCurrencyL(column));
		tableView.getColumns().add(clnValorL);

		TableColumn<Lancamento, Date> clnDataL = new TableColumn<Lancamento, Date>();
		clnDataL.setCellValueFactory(new PropertyValueFactory<Lancamento, Date>("data"));
		clnDataL.setText("Data");
		clnDataL.setPrefWidth(150);
		clnDataL.setCellFactory(column -> this.formatColumnDateL(column));
		tableView.getColumns().add(clnDataL);

		TableColumn<Lancamento, Categoria> clnCategoriaL = new TableColumn<Lancamento, Categoria>();
		clnCategoriaL.setCellValueFactory(new PropertyValueFactory<Lancamento, Categoria>("categoria"));
		clnCategoriaL.setText("Categoria");
		clnCategoriaL.setPrefWidth(300);
		tableView.getColumns().add(clnCategoriaL);

		TableColumn<Lancamento, Investimento> clnTipoInv = new TableColumn<Lancamento, Investimento>();
		clnTipoInv.setCellValueFactory(new PropertyValueFactory<Lancamento, Investimento>("tipoInvestimento"));
		clnTipoInv.setText("Tipo");
		clnTipoInv.setPrefWidth(150);
		tableView.getColumns().add(clnTipoInv);

		TableColumn<Lancamento, String> clnInvDesc = new TableColumn<Lancamento, String>();
		clnInvDesc.setCellValueFactory(new PropertyValueFactory<Lancamento, String>("investimento"));
		clnInvDesc.setText("Investimento");
		clnInvDesc.setPrefWidth(150);
		tableView.getColumns().add(clnInvDesc);

		ObservableList<Lancamento> obs = FXCollections.observableArrayList(this.objetivo.getLancamentos());
		tableView.setItems(obs);

		VBox vbox = new VBox(tableView);
		Scene scene = new Scene(vbox);
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Lançamentos");
		primaryStage.setScene(scene);
		primaryStage.show();
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
	void openDetalhes(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ItemObjetivo.fxml"));
			BorderPane root = (BorderPane) loader.load();
			ItemObjetivoController itemController = (ItemObjetivoController) loader.getController();

			itemController.setObjetivo(objetivo);
			itemController.setService(service);
			itemController.setControllerPai(this);

			Scene scene = new Scene(root, 500, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Planejamento: " + this.objetivo.getDescricao());
			stage.setMaximized(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void remover(ActionEvent event) {
		this.btnNovo.setDisable(true);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remover");
		alert.setHeaderText("Confirmação");
		alert.setContentText("Deseja remover este objetivo?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Response resp = this.service.removeById(new Integer(this.txtId.getText()));

			if (resp.getSuccess()) {
				this.objetivos = service.getAll();
				this.loadObjetivos();
				resetForm();
			} else {
				Alert alertRemove = new Alert(AlertType.ERROR);
				alertRemove.setTitle("Erro");
				alertRemove.setContentText(resp.getMessage());
				alertRemove.showAndWait();
			}

			this.resetForm();
		} else {
			this.resetForm();
		}
	}

	@FXML
	void salvar(ActionEvent event) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date dataini = new Date();
		Date datafim = Date.from(this.dtQuando.getValue().atStartOfDay(defaultZoneId).toInstant());

		Response resp = new Response(false, "Erro ao salvar");
		if (this.txtId.getText().isEmpty()) {
			resp = this.service.save(this.txtDescricao.getText(), this.txtValor.getText().replace(",", "."), dataini,
					datafim, this.pickCor.getValue().toString());
		} else {
			resp = this.service.update(this.txtId.getText(), this.txtDescricao.getText(),
					this.txtValor.getText().replace(",", "."), dataini, datafim, this.pickCor.getValue().toString());
		}

		if (resp.getSuccess()) {
			this.objetivos = service.getAll();
			this.loadObjetivos();
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
		this.loadObjetivos();
		this.tblObjetivo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.preencherFormulario(newValue));

	}

	public ObjetivoController() {
		super();
		this.service = new ObjetivoService();
		this.objetivos = service.getAll();
	}

	@SuppressWarnings("unused")
	private void loadObjetivos() {
		this.clnId.setCellValueFactory(new PropertyValueFactory<Objetivo, Integer>("id"));
		this.clnDescricao.setCellValueFactory(new PropertyValueFactory<Objetivo, String>("descricao"));
		this.clnValor.setCellValueFactory(new PropertyValueFactory<Objetivo, Double>("valor"));
		this.clnInicio.setCellValueFactory(new PropertyValueFactory<Objetivo, Date>("dataInicial"));
		this.clnQuando.setCellValueFactory(new PropertyValueFactory<Objetivo, Date>("quando"));

		this.clnInicio.setCellFactory(column -> this.formatColumnDate(column));
		this.clnQuando.setCellFactory(column -> this.formatColumnDate(column));
		this.clnValor.setCellFactory(column -> this.formatColumnCurrency(column));
		this.observableListObjetivos = FXCollections.observableList(this.objetivos);
		this.tblObjetivo.setItems(this.observableListObjetivos);
		this.tblObjetivo.refresh();

	}

	public void preencherFormulario(Objetivo objetivo) {
		if (objetivo == null) {
			return;
		}

		this.objetivo = objetivo;
		this.verDetalhe.setDisable(false);
		this.btnLancamentos.setDisable(false);
		this.txtId.setText(objetivo.getId().toString());
		this.txtDescricao.setText(objetivo.getDescricao());
		this.dtQuando.setValue(objetivo.getQuando().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		this.txtValor.setText(objetivo.getValor().toString());
		Color cor = Color.web(objetivo.getCor());
		this.pickCor.setValue(cor);
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
		this.txtPlanejado.setText(Uteis.convertToCurrency(objetivo.getPlanejado()));
		this.txtMeses.setText(objetivo.getMeses().toString());
		this.txtOrcado.setText(Uteis.convertToCurrency(objetivo.getTotalOrcado()));
		this.barConcluido.setProgress(objetivo.getPercentualAtingido());
		this.lblPercentual.setText(Uteis.convertToPercent(objetivo.getPercentualAtingido() * 100));
		this.txtTotal.setText(Uteis.convertToCurrency(objetivo.getTotalAplicado()));
		this.txtAndaFalta.setText(Uteis.convertToCurrency(objetivo.getTotalOrcado() - objetivo.getTotalAplicado()));

	}

	private void disableForm(boolean bool) {
		this.txtId.setDisable(bool);
		this.txtDescricao.setDisable(bool);
		this.dtQuando.setDisable(bool);
		this.txtValor.setDisable(bool);
		this.pickCor.setDisable(bool);
		this.btnCancelar.setDisable(bool);
		this.btnSalvar.setDisable(bool);
		this.verDetalhe.setDisable(bool);
		this.btnLancamentos.setDisable(bool);
	}

	private void clearForm() {
		this.txtId.clear();
		this.txtDescricao.clear();
		this.dtQuando.setValue(null);
		this.txtValor.clear();
		this.pickCor.setValue(null);
		this.txtPlanejado.clear();
		this.txtMeses.clear();
		this.txtTotal.clear();
		this.txtAndaFalta.clear();
	}

	private void resetForm() {
		this.disableForm(true);
		this.clearForm();
		this.tblObjetivo.getSelectionModel().clearSelection();
		this.btnNovo.setDisable(false);
	}

	private TableCell<Objetivo, Date> formatColumnDate(TableColumn<Objetivo, Date> column) {
		TableCell<Objetivo, Date> cell = new TableCell<Objetivo, Date>() {
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

	private TableCell<Objetivo, Double> formatColumnCurrency(TableColumn<Objetivo, Double> column) {
		TableCell<Objetivo, Double> cell = new TableCell<Objetivo, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null)
						this.setText(Uteis.convertToCurrency(item));
				}
			}
		};

		return cell;
	}
	
	private TableCell<Lancamento, Date> formatColumnDateL(TableColumn<Lancamento, Date> column) {
		TableCell<Lancamento, Date> cell = new TableCell<Lancamento, Date>() {
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

	private TableCell<Lancamento, Double> formatColumnCurrencyL(TableColumn<Lancamento, Double> column) {
		TableCell<Lancamento, Double> cell = new TableCell<Lancamento, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null)
						this.setText(Uteis.convertToCurrency(item));
				}
			}
		};

		return cell;
	}

}
