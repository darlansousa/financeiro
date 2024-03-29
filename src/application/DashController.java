package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


import config.Config;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Gasto;
import model.Grupo;
import model.InvestimentoFixo;
import model.InvestimentoVariavel;
import model.Lancamento;
import model.Objetivo;
import model.Orcamento;
import model.Rendimento;
import service.GrupoService;
import service.InvestimentoFixoService;
import service.InvestimentoVariavelService;
import service.ObjetivoService;
import service.OrcamentoService;
import util.BalancoItem;
import util.DespesaItem;
import util.Uteis;
import util.StatusInvest;
import util.TipoCartao;

public class DashController implements Initializable {

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

	private List<Gasto> gastos;
	private List<Lancamento> lancamentos;
	private List<Rendimento> rendimentos;

	private Orcamento selectedOrcamento;

	private Double despesas;
	private Double receitas;
	private Double totalLancamentos;
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
	private TableView<TipoCartao> tblCartao;

	@FXML
	private TableColumn<TipoCartao, String> clnCartao;

	@FXML
	private TableColumn<TipoCartao, Double> clnGastoCartao;

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

	@FXML
	private GridPane gridObjetivo;
	
	private Boolean gridObjetivoLoaded = false;
	
	@FXML
	private GridPane gridCategoria;
	
	private Boolean gridCategoriaLoaded = false;
	
	@FXML
	private GridPane gridInvestimento;
	
	private Boolean gridInvestimentoLoaded = false;
	
	public DashController() {
		super();
		this.orcamentoService = new OrcamentoService();
		this.orcamentos = this.orcamentoService.getAll(false).stream()
				.sorted((o1, o2) -> o2.getDataFinal().compareTo(o1.getDataFinal())).collect(Collectors.toList());
		this.grupoService = new GrupoService();
		this.investimentoVariavelService = new InvestimentoVariavelService();
		this.investimentoFixoService = new InvestimentoFixoService();
		this.objetivoService = new ObjetivoService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.orcamentos = this.orcamentoService.getAll(true).stream()
				.sorted((o1, o2) -> o2.getDataFinal().compareTo(o1.getDataFinal())).collect(Collectors.toList());
		this.grupos = this.grupoService.getAll();
		this.investimentosFixos = this.investimentoFixoService.getAll();
		this.investimentosVariaveis = this.investimentoVariavelService.getAll();
		this.objetivos = this.objetivoService.getAll();
		loadOrcamentos();
		loadTotaisInvestimentos();
		loadCarteira();
		loadTableBalanco();
		loadChartInvestimentos();
		loadTotaisObjetivos();
		loadChartObjetivos();

		this.clnGrupo.setCellValueFactory(new PropertyValueFactory<DespesaItem, String>("grupo"));
		this.clnPlanejado.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("planejado"));
		this.clnReal.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("real"));
		this.clnDiferenca.setCellValueFactory(new PropertyValueFactory<DespesaItem, Double>("diferenca"));

		this.clnCartao.setCellValueFactory(new PropertyValueFactory<TipoCartao, String>("cartao"));
		this.clnGastoCartao.setCellValueFactory(new PropertyValueFactory<TipoCartao, Double>("valorTotal"));
		this.clnGastoCartao.setCellFactory(column -> this.formatColumnCurrencyCartao(column));

		this.clnPlanejado.setCellFactory(column -> this.formatColumnCurrency(column));
		this.clnReal.setCellFactory(column -> this.formatColumnCurrency(column));
		this.clnDiferenca.setCellFactory(column -> this.formatColumnCurrency(column));

		this.clnGrupoInv.setCellValueFactory(new PropertyValueFactory<BalancoItem, String>("grupo"));
		this.clnVlInvestido.setCellValueFactory(new PropertyValueFactory<BalancoItem, Double>("investido"));
		this.clnReequilibrar.setCellValueFactory(new PropertyValueFactory<BalancoItem, Double>("reequilibrar"));
		
		this.clnVlInvestido.setCellFactory(column -> this.formatColumnCurrencyBalanco(column));
		this.clnReequilibrar.setCellFactory(column -> this.formatColumnCurrencyBalanco(column));
	}

	@FXML
	void atualizar(ActionEvent event) {
		String result = StatusInvest.update(this.investimentoVariavelService);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Api Status invest");
		alert.setHeaderText("Buscando Valor de investimentos");

		Label label = new Label("Console:");

		TextArea textArea = new TextArea(result);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	@FXML
	void atualizarAll(ActionEvent event) {
		this.orcamentos = this.orcamentoService.getAll(true).stream()
				.sorted((o1, o2) -> o2.getDataFinal().compareTo(o1.getDataFinal())).collect(Collectors.toList());
		this.grupos = this.grupoService.getAll();
		this.investimentosFixos = this.investimentoFixoService.getAll();
		this.investimentosVariaveis = this.investimentoVariavelService.getAll();
		this.objetivos = this.objetivoService.getAll();
		loadOrcamentos();
		loadTotaisInvestimentos();
		loadCarteira();
		loadTableBalanco();
		loadChartInvestimentos();
		loadTotaisObjetivos();
		loadChartObjetivos();
	}

	@FXML
	void atualizarInvestimentos(ActionEvent event) {
		loadTotaisInvestimentos();
		loadCarteira();
		loadTableBalanco();
		loadChartInvestimentos();
	}

	@FXML
	void atualizarObjetivo(ActionEvent event) {
		loadTotaisObjetivos();
		loadChartObjetivos();
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
		Double gastos = this.gastos.stream().filter(g -> g.getStatus().equals("BAIXADO")).map(g -> g.getValor())
				.reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

		this.despesas = gastos;

		this.lblTotalDespesas.setText(Uteis.convertToCurrency(this.despesas));
	}

	private void loadLancamentos() {
		Double lancamentos = this.lancamentos.stream().map(l -> l.getValor()).reduce((v1, v2) -> v1 + v2)
				.orElse(new Double(0));
		this.totalLancamentos = lancamentos;
		this.lblTotalLancamentos.setText(Uteis.convertToCurrency(this.totalLancamentos));
	}

	private void loadTotalReceitas() {
		this.receitas = this.rendimentos.stream().filter(r -> r.getRecebido().equals("S")).map(r -> r.getValor())
				.reduce((v1, v2) -> v1 + v2).orElse(new Double(0));

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
		this.lblComprasCartao.setText(Uteis.convertToCurrency(this.gastos.stream().filter(g -> g.getCartao() != null)
				.filter(g -> !g.getCartao().equals("")).filter(g -> g.getStatus().equals("BAIXADO"))
				.map(g -> g.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));
	}

	private void loadTableDespesas() {
		if (this.grupos == null)
			return;

		List<DespesaItem> despesas = this.grupos
				.stream().filter(g -> g.getTipo().equals("FINANCEIRO")).filter(g -> !g.getPercentual().equals(0.0)).map(
						g -> new DespesaItem(g.getDescricao(), (this.receitas * g.getPercentual() / 100),
								g.isInvestimento() ? g.getTotalLancamentosByOrcamento(this.selectedOrcamento)
										: g.getTotalGastosByOrcamento(this.selectedOrcamento)))
				.collect(Collectors.toList());

		ObservableList<DespesaItem> observableListDespesas = FXCollections.observableArrayList(despesas);
		this.tblGastos.setItems(observableListDespesas);
	}

	private void loadTableCartao() {
		if (this.grupos == null)
			return;

		List<TipoCartao> despesasCartao = new ArrayList<TipoCartao>();

		this.gastos.stream().filter(g -> g.getCartao() != null).filter(g -> !g.getCartao().equals(""))
				.collect(Collectors.groupingBy(g -> g.getCartao(), Collectors.summarizingDouble(g -> g.getValor())))
				.forEach((cartao, valor) -> {
					despesasCartao.add(new TipoCartao(cartao.toString(), valor.getSum()));
				});

		ObservableList<TipoCartao> observableListDespesas = FXCollections.observableArrayList(despesasCartao);
		this.tblCartao.setItems(observableListDespesas);
	}

	private void loadGastosPorCategoria() {
		if (this.selectedOrcamento == null)
			return;

		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
		
		if(!this.gridCategoriaLoaded) {
			this.gridCategoria.add(bc, 0, 0);
			this.gridCategoriaLoaded = true;
		}

		bc.setTitle("Gastos por Categoria");
		bc.setLegendVisible(false);
		xAxis.setLabel("Valor");
		xAxis.setTickLabelRotation(0);
		yAxis.setLabel("Categoria");

		XYChart.Series<Number, String> dados = new XYChart.Series<Number, String>();
		
		this.gastos.stream().filter(g -> g.getStatus().equals("BAIXADO")).map(g -> g.getCategoria()).distinct()
				.map(c -> new XYChart.Data<Number, String>(
						c.getTotalGastosByOrcamento(this.selectedOrcamento),
						c.getDescricao()
						))
				.sorted((d1, d2) -> Double.compare(new Double(d1.getXValue().toString()), new Double(d2.getXValue().toString())))
				.forEach(s -> dados.getData().add(s));

		if (!bc.getData().isEmpty())
			bc.getData().remove(0);

		bc.getData().add(dados);

		bc.setLegendVisible(false);
		dados.getData().stream().forEach(d -> {
			if (d.getNode() != null)
				d.getNode().setStyle("-fx-background-color: " + Uteis.getRandonColor() + ";");
		});
		
		

	}
	// INVESTIMENTOS

	private void loadTotaisInvestimentos() {

		if (this.investimentosFixos == null || this.investimentosVariaveis == null)
			return;

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
		if (this.investimentosFixos == null || this.investimentosVariaveis == null)
			return;

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
		if (this.grupos == null)
			return;

		List<BalancoItem> dados = new ArrayList<BalancoItem>();

		dados.addAll(this.grupos.stream().filter(g -> g.getTipo().equals("INVESTIMENTO"))
				.map(g -> new BalancoItem(g.getDescricao(), g.getTotalInvestido(),
						((this.patrimonio * g.getPercentual() / 100) - g.getTotalInvestido())))
				.collect(Collectors.toList()));

		ObservableList<BalancoItem> obsBalancoIntem = FXCollections.observableArrayList(dados);
		this.tblEquilibrio.setItems(obsBalancoIntem);
	}

	private void loadChartInvestimentos() {
		final NumberAxis xAxis = new NumberAxis(0,100,10);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
		
		if(!this.gridInvestimentoLoaded) {
			this.gridInvestimento.add(bc, 0, 0);
			this.gridInvestimentoLoaded = true;
		}
		
		bc.setTitle("Planejamentos dos investimentos");
		bc.setLegendVisible(false);
		xAxis.setLabel("Percentual");
		xAxis.setTickLabelRotation(0);
		yAxis.setLabel("Investimentos");
		
		XYChart.Series<Number, String> dados = new XYChart.Series<Number, String>();
	
		this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N")).forEach(ivf -> {
			Double planejado = objetivoService.getAllLancamentos().stream().filter(l -> l.getIdInvestimento() != null)
					.filter(l -> l.getTipoInvestimento().equals("FIXO"))
					.filter(l -> l.getIdInvestimento().equals(ivf.getId())).map(l -> l.getValor())
					.reduce((la1, la2) -> la1 + la2).orElse(new Double(0));
			
			dados.getData().add(new XYChart.Data<Number, String>(
					((planejado / ivf.getValorAplicado() * 100)),
					ivf.getDescricao()
					));

		});

		this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N")).forEach(ivv -> {
			Double planejado = objetivoService.getAllLancamentos().stream().filter(l -> l.getIdInvestimento() != null)
					.filter(l -> l.getTipoInvestimento().equals("VARIAVEL"))
					.filter(l -> l.getIdInvestimento().equals(ivv.getId())).map(l -> l.getValor())
					.reduce((la1, la2) -> la1 + la2).orElse(new Double(0));
			
			dados.getData().add(new XYChart.Data<Number, String>(
					((planejado / ivv.getTotal()) * 100),
					ivv.getDescricao()
					));

		});
		
		if (!bc.getData().isEmpty())
			bc.getData().remove(0);
		
		bc.getData().add(dados);
		
		dados.getData().stream().forEach(d -> {
			if (d.getNode() != null)
				d.getNode().setStyle("-fx-background-color: " + Uteis.getRandonColor() + ";");
		});
	}

	// OBJETIVOS

	private void loadTotaisObjetivos() {
		if (this.objetivos == null)
			return;

		Long totalObjetivos = this.objetivos.stream().count();
		Long objetivosRealizados = this.objetivos.stream().filter(o -> o.getPercentualAtingido().equals(1.0)).count();
		Long objetivosPendentes = this.objetivos.stream().filter(o -> !o.getPercentualAtingido().equals(1.0)).count();

		this.lblTotalObjetivos.setText(totalObjetivos.toString());
		this.lblTotalObjetivosConcluidos.setText(objetivosRealizados.toString());
		this.lblTotalObjetivosPendentes.setText(objetivosPendentes.toString());
	}

	private void loadChartObjetivos() {
		if (this.objetivos == null)
			return;

		final NumberAxis xAxis = new NumberAxis(0,100,10);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
		
		if(!this.gridObjetivoLoaded) {
			this.gridObjetivo.add(bc, 0, 0);
			this.gridObjetivoLoaded = true;
		}
		
		bc.setTitle("Percentual de conclusão dos objetivos");
		bc.setLegendVisible(false);
		xAxis.setLabel("Percentual");
		xAxis.setTickLabelRotation(0);
		yAxis.setLabel("Objetivos");

		XYChart.Series<Number, String> dados = new XYChart.Series<Number, String>();
		this.objetivos.stream().filter(o -> !o.getDescricao().equals("Aposentadoria")).forEach(o -> {
			dados.getData().add(new XYChart.Data<Number, String>((o.getPercentualAtingido() * 100), o.getDescricao()));
		});
		if (!bc.getData().isEmpty())
			bc.getData().remove(0);
		
		bc.getData().add(dados);
		
		dados.getData().stream().forEach(dado -> {
			if (dado.getNode() != null) {
				this.objetivos.stream().forEach(o -> {
					if (o.getDescricao().equals(dado.getYValue()))
						dado.getNode().setStyle("-fx-background-color: #" + o.getCor().substring(0, o.getCor().length() - 2).replace("0x", "") + ";");
				});

			}
		});
	}

	// MENU

	@FXML
	void sobre(ActionEvent event) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("view/Sobre.fxml"));
			Scene scene = new Scene(root, 668, 286);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Sobre");
			stage.setMaximized(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	@FXML
	void salvar(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar arquivo");
		Stage stage = new Stage();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("*.db", "*.db"));
		if (!Config.get("ambiente").equals("dev"))
			fileChooser.setInitialFileName("financeiro.db");
		else
			fileChooser.setInitialFileName("financeirodev.db");
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			if (!Config.get("ambiente").equals("dev"))
				Files.copy(Paths.get("resources/financeiro.db"), Paths.get(file.getPath()),
						StandardCopyOption.REPLACE_EXISTING);
			else
				Files.copy(Paths.get("resources/financeirodev.db"), Paths.get(file.getPath()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}

	// SETS

	public void setSelectedOrcamento(Orcamento selectedOrcamento) {
		if (selectedOrcamento != null) {
			this.selectedOrcamento = selectedOrcamento;
			this.gastos = selectedOrcamento.getGastos();
			this.lancamentos = selectedOrcamento.getLancamentos();
			this.rendimentos = selectedOrcamento.getRendimentos();
			loadTotalGastosCartao();
			loadTotalDespesas();
			loadTotalReceitas();
			loadLancamentos();
			loadTableDespesas();
			loadTableCartao();
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
					if (item < 0) {
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
						if (item < 0) {
							this.setStyle("-fx-text-fill: #c92500;-fx-font-weight: bold;");
						}
					}
				}
			}
		};

		return cell;
	}

	private TableCell<TipoCartao, Double> formatColumnCurrencyCartao(TableColumn<TipoCartao, Double> column) {
		TableCell<TipoCartao, Double> cell = new TableCell<TipoCartao, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null) {
						this.setText(Uteis.convertToCurrency(item));
						if (item < 0) {
							this.setStyle("-fx-text-fill: #c92500;-fx-font-weight: bold;");
						}
					}
				}
			}
		};

		return cell;
	}
}