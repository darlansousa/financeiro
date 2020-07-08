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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Grupo;
import model.InvestimentoFixo;
import model.InvestimentoVariavel;
import model.Objetivo;
import model.Orcamento;
import service.GrupoService;
import service.InvestimentoFixoService;
import service.InvestimentoVariavelService;
import service.ObjetivoService;
import service.OrcamentoService;
import util.BalancoItem;
import util.DespesaItem;
import util.Uteis;
import util.StatusInvest;

public class HomeController implements Initializable {

	private GrupoService grupoService;
	private List<Grupo> grupos;

	private InvestimentoVariavelService investimentoVariavelService;
	private List<InvestimentoVariavel> investimentosVariaveis;

	private InvestimentoFixoService investimentoFixoService;
	private List<InvestimentoFixo> investimentosFixos;

	private ObjetivoService objetivoService;
	private List<Objetivo> objetivos;

	private OrcamentoService orcamentoService;
	private List<Orcamento> orcamentos;
	private ObservableList<Orcamento> observableListOrcamentos;

	private Orcamento selectedOrcamento;

	private Double despesas;
	private Double receitas;
	private Double lancamentos;
	private Double patrimonio;
	private Double lucros;
	private Double totalInvestido;

	@FXML
	private Label lblTotalReceitas;

	@FXML
	private Label lblTotalDespesas;
	
    @FXML
    private Label lblTotalLancamentos;

	@FXML
	private Label lblDiferenca;

	@FXML
	private Label lblComprasCartao;

	@FXML
	private Label lblGastoMedioDiario;

	@FXML
	private Label lblTotalInvestido;

	@FXML
	private TableView<DespesaItem> tblGastos;

	@FXML
	private TableColumn<DespesaItem, String> clnGrupo;

	@FXML
	private TableColumn<DespesaItem, Double> clnPlanejado;

	@FXML
	private TableColumn<DespesaItem, Double> clnReal;

	@FXML
	private TableColumn<DespesaItem, Double> clnDiferenca;

	@FXML
	private TableView<BalancoItem> tblEquilibrio;

	@FXML
	private TableColumn<BalancoItem, String> clnGrupoInv;

	@FXML
	private TableColumn<BalancoItem, Double> clnVlInvestido;

	@FXML
	private TableColumn<BalancoItem, Double> clnReequilibrar;

	@FXML
	private BarChart<String, Double> chartCategoria;

	@FXML
	private CategoryAxis x;

	@FXML
	private NumberAxis y;

	@FXML
	private ComboBox<Orcamento> comboOrcamento;

	@FXML
	private Label lblPatrimonio;

	@FXML
	private Label lblTotalRendimentos;

	@FXML
	private PieChart chartCarteira;

	@FXML
	private TableView<Objetivo> tblObjetivo;

	@FXML
	private Label lblTotalObjetivosConcluidos;

	@FXML
	private Label lblTotalObjetivosPendentes;

	@FXML
	private Label lblTotalObjetivos;

	public HomeController() {
		super();
		this.orcamentoService = new OrcamentoService();

		this.orcamentos = this.orcamentoService.getAll().stream()
				.sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId())).collect(Collectors.toList());

		this.grupoService = new GrupoService();
		this.grupos = this.grupoService.getAll();

		this.investimentoVariavelService = new InvestimentoVariavelService();
		this.investimentoFixoService = new InvestimentoFixoService();
		this.investimentosFixos = this.investimentoFixoService.getAll();
		this.investimentosVariaveis = this.investimentoVariavelService.getAll();

		this.objetivoService = new ObjetivoService();
		this.objetivos = this.objetivoService.getAll();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadOrcamentos();
		loadTotaisInvestimentos();

		this.clnGrupo.setCellValueFactory(new PropertyValueFactory<DespesaItem, String>("grupo"));
		this.clnPlanejado.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("planejado"));
		this.clnReal.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("real"));
		this.clnDiferenca.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("diferenca"));

		this.clnPlanejado.setCellFactory(column -> this.formatColumnCurrency(column));
		this.clnReal.setCellFactory(column -> this.formatColumnCurrency(column));
		this.clnDiferenca.setCellFactory(column -> this.formatColumnCurrency(column));

		this.clnGrupoInv.setCellValueFactory(new PropertyValueFactory<BalancoItem, String>("grupo"));
		this.clnVlInvestido.setCellValueFactory(new PropertyValueFactory<BalancoItem, Double>("investido"));
		this.clnReequilibrar.setCellValueFactory(new PropertyValueFactory<BalancoItem, Double>("reequilibrar"));

		this.clnVlInvestido.setCellFactory(column -> this.formatColumnCurrencyBalanco(column));
		this.clnReequilibrar.setCellFactory(column -> this.formatColumnCurrencyBalanco(column));

		loadTableDespesas();
		loadCarteira();
		loadTableBalanco();
		loadTotaisObjetivos();
	}

	@FXML
	void atualizar(ActionEvent event) {
		StatusInvest.update();
	}

	@FXML
	void atualizarAll(ActionEvent event) {
		this.orcamentos = this.orcamentoService.getAll().stream()
				.sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId())).collect(Collectors.toList());
		this.grupos = this.grupoService.getAll();
		this.investimentosFixos = this.investimentoFixoService.getAll();
		this.investimentosVariaveis = this.investimentoVariavelService.getAll();
		this.objetivos = this.objetivoService.getAll();
		loadOrcamentos();
		loadTotaisInvestimentos();
		loadCarteira();
		loadTableBalanco();
		loadTotaisObjetivos();

	}
	
    @FXML
    void atualizarInvestimentos(ActionEvent event) {
		loadTotaisInvestimentos();
		loadCarteira();
		loadTableBalanco();
    }

    @FXML
    void atualizarObjetivo(ActionEvent event) {
		loadTotaisObjetivos();
    }

    @FXML
    void atualizarOrcamentos(ActionEvent event) {
		loadOrcamentos();
    }


	/// ORCAMENTOS

	private void loadOrcamentos() {
		this.observableListOrcamentos = FXCollections.observableArrayList(this.orcamentos);
		this.comboOrcamento.setItems(this.observableListOrcamentos);
		this.comboOrcamento.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedOrcamento(newValue));
		this.comboOrcamento.getSelectionModel().selectFirst();

	}

	private void loadTotalDespesas() {
		Double gastos = this.selectedOrcamento.getGastos().stream()
				.filter(g -> g.getStatus().equals("BAIXADO"))
				.map(g -> g.getValor())
				.reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

	

		this.despesas = gastos;
		

		this.lblTotalDespesas.setText(Uteis.convertToCurrency(this.despesas));
	}
	
	private void loadLancamentos() {
		Double lancamentos = this.selectedOrcamento.getLancamentos()
				.stream()
				.map(l -> l.getValor())
				.reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
		this.lancamentos = lancamentos;
		this.lblTotalLancamentos.setText(Uteis.convertToCurrency(this.lancamentos));
	}

	private void loadTotalReceitas() {
		this.receitas = this.selectedOrcamento.getRendimentos().stream().filter(r -> r.getRecebido().equals("S"))
				.map(r -> r.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

		this.lblTotalReceitas.setText(Uteis.convertToCurrency(this.receitas));
	}

	private void loadDiferenca() {
		this.lblDiferenca.setText(Uteis.convertToCurrency(this.receitas - this.despesas));
	}

	private void loadGastoMedio() {
		if (this.selectedOrcamento != null)
			this.lblGastoMedioDiario.setText(
					Uteis.convertToCurrency((this.receitas - this.despesas) / this.selectedOrcamento.getDias()));
		else
			this.lblGastoMedioDiario.setText("R$ 0,00");
	}

	private void loadTotalGastosCartao() {
		this.lblComprasCartao.setText(
				Uteis.convertToCurrency(this.selectedOrcamento.getGastos().stream().filter(g -> g.getCartao() != null)
						.filter(g -> !g.getCartao().equals("")).filter(g -> g.getStatus().equals("BAIXADO"))
						.map(g -> g.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));
	}

	private void loadTableDespesas() {
		List<DespesaItem> despesas = this.grupos.stream().filter(g -> g.getTipo().equals("FINANCEIRO"))
				.filter(g -> !g.getPercentual().equals(0.0))
				.map(g -> new DespesaItem(
						g.getDescricao(),
						(this.receitas * g.getPercentual() / 100),
						g.isInvestimento() ? g.getTotalLancamentosByOrcamento(this.selectedOrcamento) : g.getTotalGastosByOrcamento(this.selectedOrcamento)))
				.collect(Collectors.toList());

		ObservableList<DespesaItem> observableListDespesas = FXCollections.observableArrayList(despesas);
		this.tblGastos.setItems(observableListDespesas);
	}

	private void loadGastosPorCategoria() {
		if (this.selectedOrcamento == null)
			return;

		XYChart.Series<String, Double> dados = new XYChart.Series<String, Double>();
		this.selectedOrcamento.getGastos()
		.stream()
		.filter(g -> g.getStatus().equals("BAIXADO"))
		.map(g -> g.getCategoria())
		.distinct()
		.map(c -> new XYChart.Data<String, Double>(c.getDescricao(), c.getTotalGastosByOrcamento(this.selectedOrcamento)))
		.sorted((d1, d2) -> Double.compare(d2.getYValue(), d1.getYValue()))
		.forEach(s -> dados.getData().add(s));

		if (!this.chartCategoria.getData().isEmpty())
			this.chartCategoria.getData().remove(0);

		this.chartCategoria.getData().add(dados);

		this.chartCategoria.setLegendVisible(false);
		dados.getData().stream().forEach(d -> {
			if (d.getNode() != null)
				d.getNode().setStyle("-fx-background-color: " + Uteis.getRandonColor() + ";");
		});
		;

	}
	// INVESTIMENTOS

	private void loadTotaisInvestimentos() {

		Double fixos = this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
				.map(ivf -> ivf.getValorLiquido()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

		Double variaveis = this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
				.map(ivv -> ivv.getTotalAtual()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

		Double fixosInvestido = this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
				.map(ivf -> ivf.getValorAplicado()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

		Double variaveisInvestido = this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
				.map(ivf -> ivf.getTotal()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
		this.totalInvestido = fixosInvestido + variaveisInvestido;
		this.patrimonio = fixos + variaveis;
		this.lucros = this.patrimonio - totalInvestido;
		this.lblTotalInvestido.setText(Uteis.convertToCurrency(this.totalInvestido));
		this.lblPatrimonio.setText(Uteis.convertToCurrency(this.patrimonio));
		this.lblTotalRendimentos.setText(Uteis.convertToCurrency(this.lucros));

	}

	private void loadCarteira() {
		List<PieChart.Data> dados = new ArrayList<PieChart.Data>();
		dados.addAll(this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
				.map(ivf -> ivf.getGrupo()).distinct()
				.map(g -> new PieChart.Data(g.getDescricao(),
						(g.getTotalInvestido()) / (this.patrimonio * g.getPercentual() / 100)))
				.collect(Collectors.toList()));

		dados.addAll(this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
				.map(ivf -> ivf.getGrupo()).distinct()
				.map(g -> new PieChart.Data(g.getDescricao(),
						(g.getTotalInvestido()) / (this.patrimonio * g.getPercentual() / 100)))
				.collect(Collectors.toList()));

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(dados);

		this.chartCarteira.setData(pieChartData);
		this.chartCarteira.setStartAngle(90);
		pieChartData.stream().forEach(dado -> {

			if (dado.getNode() != null) {
				this.grupos.stream().forEach(g -> {
					if (g.getDescricao().equals(dado.getName()))
						dado.getNode().setStyle("-fx-pie-color: " + g.getCor() + ";");
				});

			}
		});

		this.chartCarteira.setLegendVisible(false);
	}

	private void loadTableBalanco() {
		List<BalancoItem> dados = new ArrayList<BalancoItem>();

		dados.addAll(this.grupos.stream().filter(g -> g.getTipo().equals("INVESTIMENTO"))
				.map(g -> new BalancoItem(g.getDescricao(), g.getTotalInvestido(),
						((this.patrimonio * g.getPercentual() / 100) - g.getTotalInvestido())))
				.collect(Collectors.toList()));

		ObservableList<BalancoItem> obsBalancoIntem = FXCollections.observableArrayList(dados);
		this.tblEquilibrio.setItems(obsBalancoIntem);
	}

	// OBJETIVOS

	private void loadTotaisObjetivos() {
		Long totalObjetivos = this.objetivos.stream().count();

		Long objetivosRealizados = this.objetivos.stream().filter(o -> o.getPercentualAtingido().equals(1.0)).count();

		Long objetivosPendentes = this.objetivos.stream().filter(o -> !o.getPercentualAtingido().equals(1.0)).count();

		this.lblTotalObjetivos.setText(totalObjetivos.toString());
		this.lblTotalObjetivosConcluidos.setText(objetivosRealizados.toString());
		this.lblTotalObjetivosPendentes.setText(objetivosPendentes.toString());
	}

	// MENU

	@FXML
	void openOrcamento(ActionEvent event) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Orcamento.fxml"));
			Scene scene = new Scene(root, 700, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Orçamentos");
			stage.setMaximized(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openObjetivo(ActionEvent event) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Objetivo.fxml"));
			Scene scene = new Scene(root, 900, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Objetivos");
			stage.setMaximized(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openRendimentos(ActionEvent event) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Rendimento.fxml"));
			Scene scene = new Scene(root, 700, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Rendimentos");
			stage.setMaximized(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void openInvestimentos(ActionEvent event) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Investimento.fxml"));
			Scene scene = new Scene(root, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Investimentos");
			stage.setMaximized(true);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openGastos(ActionEvent event) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Gasto.fxml"));
			Scene scene = new Scene(root, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Gastos");
			stage.setMaximized(true);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// SETS

	public void setSelectedOrcamento(Orcamento selectedOrcamento) {
		if (selectedOrcamento != null) {
			this.selectedOrcamento = selectedOrcamento;
			loadTotalGastosCartao();
			loadTotalDespesas();
			loadTotalReceitas();
			loadLancamentos();
			loadTableDespesas();
			loadDiferenca();
			loadGastoMedio();
			loadGastosPorCategoria();
		
		}
	}

	// FORMARTERS

	private TableCell<DespesaItem, Double> formatColumnCurrency(TableColumn<DespesaItem, Double> column) {
		TableCell<DespesaItem, Double> cell = new TableCell<DespesaItem, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null)
						this.setText(Uteis.convertToCurrency(item));
					if(item < 0) {
						this.setStyle("-fx-text-fill: #c92500;-fx-font-weight: bold;");
					}
				}
			}
		};

		return cell;
	}

	private TableCell<BalancoItem, Double> formatColumnCurrencyBalanco(TableColumn<BalancoItem, Double> column) {
		TableCell<BalancoItem, Double> cell = new TableCell<BalancoItem, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null) {
						this.setText(Uteis.convertToCurrency(item));
						if(item < 0) {
							this.setStyle("-fx-text-fill: #c92500;-fx-font-weight: bold;");
						}
					}
				}
			}
		};

		return cell;
	}
}