package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Categoria;
import model.Gasto;
import model.Grupo;
import model.Lancamento;
import model.Objetivo;
import model.Orcamento;
import service.GastoService;
import service.GrupoService;
import service.ObjetivoService;
import service.OrcamentoService;
import util.Uteis;
import util.Investimento;
import util.Response;

public class GastoController implements Initializable {

	ObjetivoService objetivoService;
	List<Lancamento> lancamentos;
	ObservableList<Lancamento> observableListLancamentos;

	GrupoService grupoService;
	List<Grupo> grupos;
	ObservableList<Grupo> observableListGrupos;

	GastoService gastoService;
	List<Gasto> gastos;
	ObservableList<Gasto> observableListGastos;
	

	OrcamentoService orcamentoService;
	List<Orcamento> orcamentos;
	ObservableList<Orcamento> observableListOrcamentos;

	Grupo selectedGroup;
	Orcamento selectedOrcamento;
	Gasto selectedGasto;
	Lancamento selectedLancamento;
	
	private TableView<Gasto> tblGastos;
	private TableView<Lancamento> tblLancamentos;

	public GastoController() {
		
		this.orcamentoService = new OrcamentoService();
		this.orcamentos = this.orcamentoService.getAll(false).stream()
				.sorted((o1, o2) -> o2.getDataFinal().compareTo(o1.getDataFinal()))
				.collect(Collectors.toList());
		
		this.grupoService = new GrupoService();
		this.grupos = grupoService.getAll().stream().filter(g -> g.getTipo().equals("FINANCEIRO"))
				.filter(g -> !g.getPercentual().equals(0.0)).collect(Collectors.toList());

		this.gastoService = new GastoService();
		this.objetivoService = new ObjetivoService();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.tblGastos = new TableView<Gasto>();
		this.tblLancamentos = new TableView<Lancamento>();

		constructColumns();
		loadOrcamentos();
		loadGrupos();
		this.comboOrcamento.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedOrcamento(newValue));
		
		this.comboGrupo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedGroup(newValue));
		this.comboOrcamento.getSelectionModel().selectFirst();
		this.comboGrupo.getSelectionModel().selectFirst();

		this.tblGastos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedGasto(newValue));
		
		this.tblLancamentos.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> this.setSelectedLancamento(newValue));

	}

	@FXML
	private BorderPane borderPaneWindow;

	@FXML
	private ComboBox<Grupo> comboGrupo;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnRemover;

	@FXML
	private Label lblTotalPlanejado;

	@FXML
	private Label lblTotalGasto;

	@FXML
	private Label lblSaldo;

	@FXML
	private Label lblPercentualGrupo;

	@FXML
	private Button btnBaixar;

	@FXML
	private Button btnConfirmar;

	@FXML
	private Button btnDividir;

	@FXML
	private ComboBox<Orcamento> comboOrcamento;

	@FXML
	void baixar(ActionEvent event) {
		this.selectedGasto.setStatus("BAIXADO");
		this.gastoService.save(this.selectedGasto);
		this.loadGastos();
		this.resetPainel();
	}

	@FXML
	void confirmar(ActionEvent event) {
		this.selectedGasto.setStatus("CONFIRMADO");
		this.gastoService.save(this.selectedGasto);
		this.loadGastos();
		this.resetPainel();
	}

	@FXML
	void editar(ActionEvent event) {
		if(this.isObjetivoType()) {
			this.openLancamento("Editar");
		}else {
			this.openDespesa("Editar");
		}
	}

	@FXML
	void novo(ActionEvent event) {
		if(this.isObjetivoType()) {
			this.openLancamento("Novo");
		}else {
			this.openDespesa("Novo");
		}
	}

	@FXML
	void remover(ActionEvent event) {
		this.btnNovo.setDisable(true);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);
		
		Response resp = new Response(false, "Erro ao remover.");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remover");
		alert.setHeaderText("Confirmação");
		alert.setContentText("Deseja remover esta despesa?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if(isObjetivoType())
				resp = this.objetivoService.removeLancamentoById(this.selectedLancamento.getId());
			else
				resp = this.gastoService.removeById(this.selectedGasto.getId());
			
			

			if (resp.getSuccess()) {
				if(isObjetivoType())
					this.loadLancamnetos();
				else
					this.loadGastos();
				
				this.resetPainel();
			} else {
				Alert alertRemove = new Alert(AlertType.ERROR);
				alertRemove.setTitle("Erro");
				alertRemove.setContentText(resp.getMessage());
				alertRemove.showAndWait();
			}
		}
	}

	@FXML
	void dividir(ActionEvent event) {
		this.selectedGasto.setValor(this.selectedGasto.getValor() / 2);
		this.gastoService.save(this.selectedGasto);
		this.loadGastos();
		this.resetPainel();
	}

	

	private void loadGrupos() {
		this.observableListGrupos = FXCollections.observableArrayList(this.grupos);
		this.comboGrupo.setItems(this.observableListGrupos);
	}

	public void loadGastos() {
		if(this.selectedGroup == null)
			return;
		
		this.gastos = this.gastoService.getAll().stream()
				.filter(g -> g.getCategoria().getGrupo().getId().equals(this.selectedGroup.getId()))
				.filter(g -> g.getOrcamento().getId().equals(this.selectedOrcamento.getId()))
				.collect(Collectors.toList());

		this.observableListGastos = FXCollections.observableArrayList(this.gastos);
		this.tblGastos.setItems(this.observableListGastos);
		this.tblGastos.refresh();
		this.borderPaneWindow.setCenter(this.tblGastos);
		this.tblGastos.getSelectionModel().clearSelection();
		calcularTotais();
	}

	public void loadLancamnetos() {
		this.lancamentos = this.objetivoService.getAllLancamentos().stream()
				.filter(l -> l.getCategoria().getGrupo().getId().equals(this.selectedGroup.getId()))
				.filter(l -> l.getOrcamento().getId().equals(this.selectedOrcamento.getId()))
				.collect(Collectors.toList());
		this.observableListLancamentos = FXCollections.observableArrayList(this.lancamentos);
		this.tblLancamentos.setItems(this.observableListLancamentos);
		this.tblLancamentos.refresh();
		this.borderPaneWindow.setCenter(this.tblLancamentos);
		this.tblLancamentos.getSelectionModel().clearSelection();
		calcularTotais();
	}

	private void calcularTotais() {
		
		Double totalRendimentos = this.selectedOrcamento.getRendimentos().stream().map(r -> r.getValor())
				.reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
		
		if(isObjetivoType()) {
			
			Double planejado = (totalRendimentos * this.selectedGroup.getPercentual()) / 100;
			Double total = this.lancamentos.stream().map(g -> g.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
			Double saldo = planejado - total;

			this.lblSaldo.setText(Uteis.convertToCurrency(saldo));
			this.lblTotalGasto.setText(Uteis.convertToCurrency(total));
			this.lblTotalPlanejado.setText(Uteis.convertToCurrency(planejado));
			
		}else {
			Double planejado = (totalRendimentos * this.selectedGroup.getPercentual()) / 100;
			Double total = this.gastos.stream().map(g -> g.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
			Double saldo = planejado - total;

			this.lblSaldo.setText(Uteis.convertToCurrency(saldo));
			this.lblTotalGasto.setText(Uteis.convertToCurrency(total));
			this.lblTotalPlanejado.setText(Uteis.convertToCurrency(planejado));
		}
		

	}

	private void loadOrcamentos() {
		this.observableListOrcamentos = FXCollections.observableArrayList(this.orcamentos);
		this.comboOrcamento.setItems(this.observableListOrcamentos);
	}

	public void constructColumns() {
		// Gasto

		TableColumn<Gasto, String> clnDescricao = new TableColumn<Gasto, String>();
		clnDescricao.setCellValueFactory(new PropertyValueFactory<Gasto, String>("descricao"));
		clnDescricao.setText("Descrição");
		clnDescricao.setPrefWidth(200);
		this.tblGastos.getColumns().add(clnDescricao);

		TableColumn<Gasto, Double> clnValor = new TableColumn<Gasto, Double>();
		clnValor.setCellValueFactory(new PropertyValueFactory<Gasto, Double>("valor"));
		clnValor.setText("Valor");
		clnValor.setPrefWidth(150);
		clnValor.setCellFactory(column -> this.formatColumnCurrency(column));
		this.tblGastos.getColumns().add(clnValor);

		TableColumn<Gasto, Date> clnData = new TableColumn<Gasto, Date>();
		clnData.setCellValueFactory(new PropertyValueFactory<Gasto, Date>("data"));
		clnData.setText("Data");
		clnData.setPrefWidth(150);
		clnData.setCellFactory(column -> this.formatColumnDate(column));
		this.tblGastos.getColumns().add(clnData);

		TableColumn<Gasto, Categoria> clnCategoria = new TableColumn<Gasto, Categoria>();
		clnCategoria.setCellValueFactory(new PropertyValueFactory<Gasto, Categoria>("categoria"));
		clnCategoria.setText("Categoria");
		clnCategoria.setPrefWidth(150);
		this.tblGastos.getColumns().add(clnCategoria);

		TableColumn<Gasto, String> clnCartao = new TableColumn<Gasto, String>();
		clnCartao.setCellValueFactory(new PropertyValueFactory<Gasto, String>("cartao"));
		clnCartao.setText("Cartão");
		clnCartao.setPrefWidth(150);
		this.tblGastos.getColumns().add(clnCartao);

		TableColumn<Gasto, String> clnSatatus = new TableColumn<Gasto, String>();
		clnSatatus.setCellValueFactory(new PropertyValueFactory<Gasto, String>("status"));
		clnSatatus.setText("Status");
		clnSatatus.setPrefWidth(150);
		this.tblGastos.getColumns().add(clnSatatus);

		// Lancamento

		TableColumn<Lancamento, Objetivo> clnObjetivo = new TableColumn<Lancamento, Objetivo>();
		clnObjetivo.setCellValueFactory(new PropertyValueFactory<Lancamento, Objetivo>("objetivo"));
		clnObjetivo.setText("Objetivo");
		clnObjetivo.setPrefWidth(300);
		this.tblLancamentos.getColumns().add(clnObjetivo);

		TableColumn<Lancamento, Double> clnValorL = new TableColumn<Lancamento, Double>();
		clnValorL.setCellValueFactory(new PropertyValueFactory<Lancamento, Double>("valor"));
		clnValorL.setText("Valor");
		clnValorL.setPrefWidth(150);
		clnValorL.setCellFactory(column -> this.formatColumnCurrencyL(column));
		this.tblLancamentos.getColumns().add(clnValorL);

		TableColumn<Lancamento, Date> clnDataL = new TableColumn<Lancamento, Date>();
		clnDataL.setCellValueFactory(new PropertyValueFactory<Lancamento, Date>("data"));
		clnDataL.setText("Data");
		clnDataL.setPrefWidth(150);
		clnDataL.setCellFactory(column -> this.formatColumnDateL(column));
		this.tblLancamentos.getColumns().add(clnDataL);

		TableColumn<Lancamento, Categoria> clnCategoriaL = new TableColumn<Lancamento, Categoria>();
		clnCategoriaL.setCellValueFactory(new PropertyValueFactory<Lancamento, Categoria>("categoria"));
		clnCategoriaL.setText("Categoria");
		clnCategoriaL.setPrefWidth(300);
		this.tblLancamentos.getColumns().add(clnCategoriaL);

		TableColumn<Lancamento, Investimento> clnTipoInv = new TableColumn<Lancamento, Investimento>();
		clnTipoInv.setCellValueFactory(new PropertyValueFactory<Lancamento, Investimento>("tipoInvestimento"));
		clnTipoInv.setText("Tipo");
		clnTipoInv.setPrefWidth(150);
		this.tblLancamentos.getColumns().add(clnTipoInv);

		TableColumn<Lancamento, String> clnInvDesc = new TableColumn<Lancamento, String>();
		clnInvDesc.setCellValueFactory(new PropertyValueFactory<Lancamento, String>("investimento"));
		clnInvDesc.setText("Investimento");
		clnInvDesc.setPrefWidth(150);
		this.tblLancamentos.getColumns().add(clnInvDesc);

	}

	public void setSelectedOrcamento(Orcamento selectedOrcamento) {
		this.selectedOrcamento = selectedOrcamento;
		if (this.isObjetivoType())
			this.loadLancamnetos();
		else
			this.loadGastos();
	}

	public void setSelectedGroup(Grupo selectedGroup) {
		this.selectedGroup = selectedGroup;
		if (this.isObjetivoType())
			this.loadLancamnetos();
		else
			this.loadGastos();
		this.lblPercentualGrupo.setText("Percentual: " + selectedGroup.getPercentual() + "%");
	}

	public void setSelectedGasto(Gasto selectedGasto) {
		if(selectedGasto == null)
			return;
		
		this.selectedGasto = selectedGasto;
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
		if(selectedGasto.getStatus().equals("BAIXADO")) {
			this.btnBaixar.setDisable(true);
			this.btnConfirmar.setDisable(true);
		}else if(selectedGasto.getStatus().equals("CONFIRMADO")) {
			this.btnBaixar.setDisable(false);
			this.btnConfirmar.setDisable(true);
		}else if(selectedGasto.getStatus().equals("PENDENTE")) {
			this.btnBaixar.setDisable(true);
			this.btnConfirmar.setDisable(false);
		}
		
		this.btnDividir.setDisable(false);
	}
	
	public void setSelectedLancamento(Lancamento selectedLancamento) {
		this.selectedLancamento = selectedLancamento;
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
	}

	private TableCell<Gasto, Date> formatColumnDate(TableColumn<Gasto, Date> column) {
		TableCell<Gasto, Date> cell = new TableCell<Gasto, Date>() {
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

	private TableCell<Gasto, Double> formatColumnCurrency(TableColumn<Gasto, Double> column) {
		TableCell<Gasto, Double> cell = new TableCell<Gasto, Double>() {

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

	private void resetPainel() {
		this.btnNovo.setDisable(false);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);
		this.btnDividir.setDisable(true);
		this.btnBaixar.setDisable(true);
		this.btnConfirmar.setDisable(true);
		if(isObjetivoType()) {
			this.tblLancamentos.getSelectionModel().clearSelection();
		}else {
			this.tblGastos.getSelectionModel().clearSelection();
		}
		
		
	}

	private void openDespesa(String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Despesa.fxml"));
			BorderPane root = (BorderPane) loader.load();
			DespesaController despesaController = (DespesaController) loader.getController();

			if (title.equals("Editar"))
				despesaController.setGasto(this.selectedGasto);
			else
				despesaController.setGasto(new Gasto());

			despesaController.setService(this.gastoService);
			despesaController.setGrupo(this.selectedGroup);
			despesaController.setOrcamento(this.selectedOrcamento);
			despesaController.setControllerPai(this);

			Scene scene = new Scene(root, 500, 260);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(title);
			stage.setMaximized(false);
			despesaController.setStage(stage);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void openLancamento(String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Lancamento.fxml"));
			BorderPane root = (BorderPane) loader.load();
			LancamentoController lancamentoController = (LancamentoController) loader.getController();

			if (title.equals("Editar"))
				lancamentoController.setLancamento(this.selectedLancamento);
			else
				lancamentoController.setLancamento(new Lancamento());

			lancamentoController.setService(this.objetivoService);
			lancamentoController.setGrupo(this.selectedGroup);
			lancamentoController.setOrcamento(this.selectedOrcamento);
			lancamentoController.setControllerPai(this);

			Scene scene = new Scene(root, 500, 300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(title);
			stage.setMaximized(false);
			lancamentoController.setStage(stage);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean isObjetivoType() {
		if(this.selectedGroup == null)
			return false;
		return this.selectedGroup.getDescricao().equals("Objetivos médio/curto prazo")
				|| this.selectedGroup.getDescricao().equals("Aposentadoria");
	}

}
