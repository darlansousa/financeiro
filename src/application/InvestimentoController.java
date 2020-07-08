package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Grupo;
import model.InvestimentoFixo;
import model.InvestimentoVariavel;
import service.GrupoService;
import service.InvestimentoFixoService;
import service.InvestimentoVariavelService;
import util.Uteis;
import util.Response;
import util.StatusInvest;

public class InvestimentoController implements Initializable {

	GrupoService grupoService;
	List<Grupo> grupos;
	ObservableList<Grupo> observableListGrupos;

	private Grupo selectedGroup;
	private InvestimentoVariavel selectedInvVarivel;
	private InvestimentoFixo selectedInvFixo;

	InvestimentoVariavelService investimentoVariavelService;
	List<InvestimentoVariavel> investimentosVariaveis;
	ObservableList<InvestimentoVariavel> observableListInvVariaveis;

	InvestimentoFixoService investimentoFixoService;
	List<InvestimentoFixo> investimentosFixos;
	ObservableList<InvestimentoFixo> observableListInvFixo;

	@FXML
	private BorderPane borderPaneWindow;

	private TableView<InvestimentoVariavel> tblInvestimentosVariaveis;

	private TableView<InvestimentoFixo> tblInvestimentosFixos;

	@FXML
	private ComboBox<Grupo> comboGrupo;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnRemover;
	
    @FXML
    private MenuButton btnColunas;

	@FXML
	private Label lblTotalInvestido;

	@FXML
	private Label lblTotalAtual;

	@FXML
	private Label lblTotalRendimentos;

	@FXML
	private Label lblPercentualGrupo;

	@FXML
	private Button btnAtualizar;

	@FXML
	private Label lblDtAtualizacao;

	@FXML
	private Label lblMercado;

	@FXML
	void todasColunas(ActionEvent event) {
		if (isVariavel()) {
			this.tblInvestimentosVariaveis.getColumns().forEach(c -> c.setVisible(true));
		} else {
			this.tblInvestimentosFixos.getColumns().forEach(c -> c.setVisible(true));
		}
		
		this.btnColunas.getItems().stream()
		.map(i -> (CheckMenuItem) i)
		.forEach(i -> i.setSelected(true));
	}

	private void checkColuna(ActionEvent event) {
		if (isVariavel()) {
			this.btnColunas.getItems().stream().map(i -> (CheckMenuItem) i).forEach((i -> {
				this.tblInvestimentosVariaveis.getColumns().stream().forEach(c -> {
					if (i.getText().equals(c.getText())) {
						c.setVisible(i.isSelected());
					}
				});
			}));
		} else {
			this.btnColunas.getItems().stream().map(i -> (CheckMenuItem) i).forEach((i -> {
				this.tblInvestimentosFixos.getColumns().stream().forEach(c -> {
					if (i.getText().equals(c.getText())) {
						c.setVisible(i.isSelected());
					}
				});
			}));
		}

	}

	@FXML
	void atualizar(ActionEvent event) {
		StatusInvest.update();
		Optional<String> data = this.investimentosVariaveis.stream().map(iv -> iv.getDataAtualizacao()).findFirst();

		if (data.isPresent()) {
			this.lblDtAtualizacao.setText(data.get());
		}
	}

	@FXML
	void editar(ActionEvent event) {

		if (this.isVariavel()) {
			// Variavel
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("view/InvestimentoVariavel.fxml"));
				BorderPane root = (BorderPane) loader.load();
				VariavelController variavelController = (VariavelController) loader.getController();

				variavelController.setInvestimento(this.selectedInvVarivel);
				variavelController.setService(this.investimentoVariavelService);
				variavelController.setGrupo(this.selectedGroup);
				variavelController.setControllerPai(this);

				Scene scene = new Scene(root, 650, 260);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Editar");
				stage.setMaximized(false);
				variavelController.setStage(stage);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("view/InvestimentoFixo.fxml"));
				BorderPane root = (BorderPane) loader.load();
				FixoController fixoController = (FixoController) loader.getController();

				fixoController.setInvestimento(this.selectedInvFixo);
				fixoController.setService(this.investimentoFixoService);
				fixoController.setGrupo(this.selectedGroup);
				fixoController.setControllerPai(this);

				Scene scene = new Scene(root, 650, 300);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Novo");
				stage.setMaximized(false);
				fixoController.setStage(stage);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void novo(ActionEvent event) {
		if (this.isVariavel()) {
			// Variavel
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("view/InvestimentoVariavel.fxml"));
				BorderPane root = (BorderPane) loader.load();
				VariavelController variavelController = (VariavelController) loader.getController();

				variavelController.setInvestimento(new InvestimentoVariavel());
				variavelController.setService(this.investimentoVariavelService);
				variavelController.setGrupo(this.selectedGroup);
				variavelController.setControllerPai(this);

				Scene scene = new Scene(root, 650, 260);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Novo");
				stage.setMaximized(false);
				variavelController.setStage(stage);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// Fixo
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("view/InvestimentoFixo.fxml"));
				BorderPane root = (BorderPane) loader.load();
				FixoController fixoController = (FixoController) loader.getController();

				fixoController.setInvestimento(new InvestimentoFixo());
				fixoController.setService(this.investimentoFixoService);
				fixoController.setGrupo(this.selectedGroup);
				fixoController.setControllerPai(this);

				Scene scene = new Scene(root, 650, 300);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Novo");
				stage.setMaximized(false);
				fixoController.setStage(stage);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
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
		alert.setContentText("Deseja remover este investimento?");

		if (this.isVariavel()) {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Response resp = this.investimentoVariavelService.removeById(this.selectedInvVarivel.getId());

				if (resp.getSuccess()) {
					this.investimentosVariaveis = investimentoVariavelService.getAll();
					this.loadInvestimentos();
					this.resetPainel();
				} else {
					Alert alertRemove = new Alert(AlertType.ERROR);
					alertRemove.setTitle("Erro");
					alertRemove.setContentText(resp.getMessage());
					alertRemove.showAndWait();
				}

			}

		} else {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Response resp = this.investimentoFixoService.removeById(this.selectedInvFixo.getId());

				if (resp.getSuccess()) {
					this.investimentosFixos = investimentoFixoService.getAll();
					this.loadInvestimentos();
					this.resetPainel();
				} else {
					Alert alertRemove = new Alert(AlertType.ERROR);
					alertRemove.setTitle("Erro");
					alertRemove.setContentText(resp.getMessage());
					alertRemove.showAndWait();
				}

			}

		}

		this.resetPainel();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.tblInvestimentosVariaveis = new TableView<InvestimentoVariavel>();
		this.tblInvestimentosFixos = new TableView<InvestimentoFixo>();
		constructColumns();

		checarMercado();
		loadGrupos();

		this.comboGrupo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedGroup(newValue));

		this.tblInvestimentosVariaveis.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedInvVarivel(newValue));

		this.tblInvestimentosFixos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.setSelectedInvFixo(newValue));

		this.comboGrupo.getSelectionModel().selectFirst();
	}

	public InvestimentoController() {
		super();
		this.grupoService = new GrupoService();
		this.grupos = grupoService.getAll().stream().filter(g -> g.getTipo().equals("INVESTIMENTO"))
				.collect(Collectors.toList());

		this.investimentoVariavelService = new InvestimentoVariavelService();
		this.investimentoFixoService = new InvestimentoFixoService();
	}

	private void loadGrupos() {
		this.observableListGrupos = FXCollections.observableArrayList(this.grupos);
		this.comboGrupo.setItems(this.observableListGrupos);
	}

	public void loadInvestimentos() {

		if (this.isVariavel()) {
			this.investimentosVariaveis = this.investimentoVariavelService.getAll().stream()
					.filter(iv -> iv.getGrupo().getDescricao().equals(this.selectedGroup.getDescricao()))
					.collect(Collectors.toList());

			this.observableListInvVariaveis = FXCollections.observableArrayList(this.investimentosVariaveis);
			this.tblInvestimentosVariaveis.setItems(this.observableListInvVariaveis);
			this.tblInvestimentosVariaveis.refresh();
			this.borderPaneWindow.setCenter(this.tblInvestimentosVariaveis);

			this.lblTotalAtual.setText(Uteis
					.convertToCurrency(this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
							.map(iv -> iv.getTotalAtual()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.lblTotalInvestido.setText(Uteis
					.convertToCurrency(this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
							.map(iv -> iv.getTotal()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.lblTotalRendimentos.setText(Uteis
					.convertToCurrency(this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
							.map(iv -> iv.getRendimento()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.btnColunas.getItems().clear();
			this.tblInvestimentosVariaveis.getColumns().stream().forEach(c -> {
				CheckMenuItem menu = new CheckMenuItem(c.getText());
				menu.setOnAction(this::checkColuna);
				menu.setSelected(true);
				this.btnColunas.getItems().add(menu);
			});

		} else {
			this.investimentosFixos = this.investimentoFixoService.getAll().stream()
					.filter(ivf -> ivf.getResgatado().equals("N"))
					.filter(iv -> iv.getGrupo().getDescricao().equals(this.selectedGroup.getDescricao()))
					.collect(Collectors.toList());

			this.observableListInvFixo = FXCollections.observableArrayList(this.investimentosFixos);
			this.tblInvestimentosFixos.setItems(this.observableListInvFixo);
			this.tblInvestimentosFixos.refresh();
			this.borderPaneWindow.setCenter(this.tblInvestimentosFixos);

			this.lblTotalAtual.setText(Uteis
					.convertToCurrency(this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
							.map(iv -> iv.getValorLiquido()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.lblTotalInvestido.setText(Uteis
					.convertToCurrency(this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
							.map(iv -> iv.getValorAplicado()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.lblTotalRendimentos.setText(Uteis
					.convertToCurrency(this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
							.map(iv -> iv.getRendimentos()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0))));

			this.btnColunas.getItems().clear();
			this.tblInvestimentosFixos.getColumns().stream().forEach(c -> {
				CheckMenuItem menu = new CheckMenuItem(c.getText());
				menu.setOnAction(this::checkColuna);
				menu.setSelected(true);
				this.btnColunas.getItems().add(menu);
			});
		}

	}

	private void checarMercado() {
		// Horário de funcionamento da B3
		// 10h30 - 17h30 - Segunda a Sexta
		Date dataHoraAtual = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataHoraAtual);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		if ((day > 1 & day < 7 & hour > 11 & hour < 16)) {
			this.lblMercado.setText("Mercado aberto");
			this.lblMercado.setTextFill(Color.web("#99cc00"));
			this.lblMercado.setStyle("-fx-text-fill: #99cc00;");

		} else {
			this.lblMercado.setText("Mercado fechado");
			this.lblMercado.setTextFill(Color.web("#c92500"));
			this.lblMercado.setStyle("-fx-text-fill: #c92500;");
		}
	}

	private void constructColumns() {
		// Fixos
		TableColumn<InvestimentoFixo, String> clnAtivo = new TableColumn<InvestimentoFixo, String>();
		clnAtivo.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, String>("ativo"));
		clnAtivo.setText("Ativo");
		clnAtivo.setPrefWidth(100);
		this.tblInvestimentosFixos.getColumns().add(clnAtivo);

		TableColumn<InvestimentoFixo, String> clnDescricao = new TableColumn<InvestimentoFixo, String>();
		clnDescricao.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, String>("descricao"));
		clnDescricao.setText("Descição");
		clnDescricao.setPrefWidth(300);
		this.tblInvestimentosFixos.getColumns().add(clnDescricao);

		TableColumn<InvestimentoFixo, Integer> clnQuantidade = new TableColumn<InvestimentoFixo, Integer>();
		clnQuantidade.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Integer>("quantidade"));
		clnQuantidade.setText("Quantidade");
		clnQuantidade.setPrefWidth(100);
		this.tblInvestimentosFixos.getColumns().add(clnQuantidade);

		TableColumn<InvestimentoFixo, Date> clnDtCompra = new TableColumn<InvestimentoFixo, Date>();
		clnDtCompra.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Date>("dataCompra"));
		clnDtCompra.setText("Data de Compra");
		clnDtCompra.setPrefWidth(150);
		clnDtCompra.setCellFactory(column -> this.formatColumnDateIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnDtCompra);

		TableColumn<InvestimentoFixo, Double> clnVlAplicado = new TableColumn<InvestimentoFixo, Double>();
		clnVlAplicado.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Double>("valorAplicado"));
		clnVlAplicado.setText("Valor Aplicado");
		clnVlAplicado.setPrefWidth(150);
		clnVlAplicado.setCellFactory(column -> this.formatColumnCurrencyIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnVlAplicado);

		TableColumn<InvestimentoFixo, Double> clnVlLiquido = new TableColumn<InvestimentoFixo, Double>();
		clnVlLiquido.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Double>("valorLiquido"));
		clnVlLiquido.setText("Valor Líquido");
		clnVlLiquido.setPrefWidth(150);
		clnVlLiquido.setCellFactory(column -> this.formatColumnCurrencyIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnVlLiquido);

		TableColumn<InvestimentoFixo, Double> clnVlResgate = new TableColumn<InvestimentoFixo, Double>();
		clnVlResgate.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Double>("valorResgate"));
		clnVlResgate.setText("Valor de Resgate");
		clnVlResgate.setPrefWidth(150);
		clnVlResgate.setCellFactory(column -> this.formatColumnCurrencyIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnVlResgate);

		TableColumn<InvestimentoFixo, Date> clnDtVencimento = new TableColumn<InvestimentoFixo, Date>();
		clnDtVencimento.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, Date>("vencimento"));
		clnDtVencimento.setText("Vencimento");
		clnDtVencimento.setPrefWidth(100);
		clnDtVencimento.setCellFactory(column -> this.formatColumnDateIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnDtVencimento);

		TableColumn<InvestimentoFixo, String> clnResgatado = new TableColumn<InvestimentoFixo, String>();
		clnResgatado.setCellValueFactory(new PropertyValueFactory<InvestimentoFixo, String>("resgatado"));
		clnResgatado.setText("Resgatado");
		clnResgatado.setPrefWidth(100);
		clnResgatado.setCellFactory(column -> this.formatColumnBooleanIF(column));
		this.tblInvestimentosFixos.getColumns().add(clnResgatado);

		// Variaveis

		TableColumn<InvestimentoVariavel, String> clnAtivoV = new TableColumn<InvestimentoVariavel, String>();
		clnAtivoV.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, String>("ativo"));
		clnAtivoV.setText("Ativo");
		clnAtivoV.setPrefWidth(100);
		this.tblInvestimentosVariaveis.getColumns().add(clnAtivoV);

		TableColumn<InvestimentoVariavel, String> clnDescricaoV = new TableColumn<InvestimentoVariavel, String>();
		clnDescricaoV.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, String>("descricao"));
		clnDescricaoV.setText("Descrição");
		clnDescricaoV.setPrefWidth(300);
		this.tblInvestimentosVariaveis.getColumns().add(clnDescricaoV);

		TableColumn<InvestimentoVariavel, Integer> clnQuantidadeV = new TableColumn<InvestimentoVariavel, Integer>();
		clnQuantidadeV.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Integer>("quantidade"));
		clnQuantidadeV.setText("Quantidade");
		clnQuantidadeV.setPrefWidth(100);
		this.tblInvestimentosVariaveis.getColumns().add(clnQuantidadeV);

		TableColumn<InvestimentoVariavel, Date> clnDtCompraV = new TableColumn<InvestimentoVariavel, Date>();
		clnDtCompraV.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Date>("dataCompra"));
		clnDtCompraV.setText("Data de Compra");
		clnDtCompraV.setPrefWidth(150);
		clnDtCompraV.setCellFactory(column -> this.formatColumnDateIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnDtCompraV);

		TableColumn<InvestimentoVariavel, Double> clnVlUnidade = new TableColumn<InvestimentoVariavel, Double>();
		clnVlUnidade.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("valorUnidade"));
		clnVlUnidade.setText("Valor Unidade");
		clnVlUnidade.setPrefWidth(150);
		clnVlUnidade.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlUnidade);

		TableColumn<InvestimentoVariavel, Double> clnVlUnidadeAtual = new TableColumn<InvestimentoVariavel, Double>();
		clnVlUnidadeAtual
				.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("valorUnidadeAtual"));
		clnVlUnidadeAtual.setText("Valor Unidade Atual");
		clnVlUnidadeAtual.setPrefWidth(180);
		clnVlUnidadeAtual.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlUnidadeAtual);

		TableColumn<InvestimentoVariavel, Date> clnDtVenda = new TableColumn<InvestimentoVariavel, Date>();
		clnDtVenda.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Date>("dataVenda"));
		clnDtVenda.setText("Data Venda");
		clnDtVenda.setPrefWidth(100);
		clnDtVenda.setCellFactory(column -> this.formatColumnDateIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnDtVenda);

		TableColumn<InvestimentoVariavel, Double> clnVlTotal = new TableColumn<InvestimentoVariavel, Double>();
		clnVlTotal.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("total"));
		clnVlTotal.setText("Total");
		clnVlTotal.setPrefWidth(100);
		clnVlTotal.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlTotal);

		TableColumn<InvestimentoVariavel, Double> clnVlTotalAtual = new TableColumn<InvestimentoVariavel, Double>();
		clnVlTotalAtual.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("totalAtual"));
		clnVlTotalAtual.setText("Total Atual");
		clnVlTotalAtual.setPrefWidth(100);
		clnVlTotalAtual.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlTotalAtual);

		TableColumn<InvestimentoVariavel, Double> clnVlRendimento = new TableColumn<InvestimentoVariavel, Double>();
		clnVlRendimento.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("rendimento"));
		clnVlRendimento.setText("Rendimento");
		clnVlRendimento.setPrefWidth(100);
		clnVlRendimento.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlRendimento);

		TableColumn<InvestimentoVariavel, Double> clnVlVenda = new TableColumn<InvestimentoVariavel, Double>();
		clnVlVenda.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, Double>("valorVenda"));
		clnVlVenda.setText("Valor Venda");
		clnVlVenda.setPrefWidth(100);
		clnVlVenda.setCellFactory(column -> this.formatColumnCurrencyIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVlVenda);

		TableColumn<InvestimentoVariavel, String> clnVendido = new TableColumn<InvestimentoVariavel, String>();
		clnVendido.setCellValueFactory(new PropertyValueFactory<InvestimentoVariavel, String>("vendido"));
		clnVendido.setText("Vendido");
		clnVendido.setPrefWidth(100);
		clnVendido.setCellFactory(column -> this.formatColumnBooleanIV(column));
		this.tblInvestimentosVariaveis.getColumns().add(clnVendido);

	}

	private TableCell<InvestimentoFixo, Date> formatColumnDateIF(TableColumn<InvestimentoFixo, Date> column) {
		TableCell<InvestimentoFixo, Date> cell = new TableCell<InvestimentoFixo, Date>() {
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

	private TableCell<InvestimentoFixo, Double> formatColumnCurrencyIF(TableColumn<InvestimentoFixo, Double> column) {
		TableCell<InvestimentoFixo, Double> cell = new TableCell<InvestimentoFixo, Double>() {

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

	private TableCell<InvestimentoFixo, String> formatColumnBooleanIF(TableColumn<InvestimentoFixo, String> column) {
		TableCell<InvestimentoFixo, String> cell = new TableCell<InvestimentoFixo, String>() {

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null) {
						if (item.equals("S"))
							this.setText("Sim");
						else
							this.setText("Não");
					}

				}
			}
		};

		return cell;
	}

	private TableCell<InvestimentoVariavel, Date> formatColumnDateIV(TableColumn<InvestimentoVariavel, Date> column) {
		TableCell<InvestimentoVariavel, Date> cell = new TableCell<InvestimentoVariavel, Date>() {
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

	private TableCell<InvestimentoVariavel, Double> formatColumnCurrencyIV(
			TableColumn<InvestimentoVariavel, Double> column) {
		TableCell<InvestimentoVariavel, Double> cell = new TableCell<InvestimentoVariavel, Double>() {

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

	private TableCell<InvestimentoVariavel, String> formatColumnBooleanIV(
			TableColumn<InvestimentoVariavel, String> column) {
		TableCell<InvestimentoVariavel, String> cell = new TableCell<InvestimentoVariavel, String>() {

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					if (item != null) {
						if (item.equals("S"))
							this.setText("Sim");
						else
							this.setText("Não");
					}

				}
			}
		};

		return cell;
	}

	private void resetPainel() {
		this.btnNovo.setDisable(false);
		this.btnEditar.setDisable(true);
		this.btnRemover.setDisable(true);
	}

	public Grupo getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Grupo selectedGroup) {
		this.selectedGroup = selectedGroup;
		this.loadInvestimentos();
	}

	public InvestimentoVariavel getSelectedInvVarivel() {
		return selectedInvVarivel;

	}

	public void setSelectedInvVarivel(InvestimentoVariavel selectedInvVarivel) {
		this.selectedInvVarivel = selectedInvVarivel;
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
	}

	public InvestimentoFixo getSelectedInvFixo() {
		return selectedInvFixo;
	}

	public void setSelectedInvFixo(InvestimentoFixo selectedInvFixo) {
		this.selectedInvFixo = selectedInvFixo;
		this.btnEditar.setDisable(false);
		this.btnRemover.setDisable(false);
	}

	private Boolean isVariavel() {
		return this.selectedGroup.getId().equals(1) || this.selectedGroup.getId().equals(2)
				|| this.selectedGroup.getId().equals(4);
	}
}
