package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Categoria;
import model.Orcamento;
import model.Rendimento;
import service.CategoriaService;
import service.OrcamentoService;
import service.RendimentoService;
import util.Uteis;
import util.Response;

public class RendimentoController implements Initializable{
	


	RendimentoService service;
	OrcamentoService orcamentoService;
	CategoriaService categoriaService;
	List<Orcamento> orcamentos;
	ObservableList<Orcamento> observableListOrcamentos;
	List<Rendimento> rendimentos;
	ObservableList<Rendimento> observableListRendimentos;
	List<Categoria> categorias;
	ObservableList<Categoria> observableListCategorias;
	String valorAnterior;
	String statusAnterior;
	
	public RendimentoController() {
		this.service = new RendimentoService();
		this.orcamentoService = new OrcamentoService();
		this.categoriaService = new CategoriaService();
		this.orcamentos = orcamentoService.getAll(false)
				.stream().sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId()))
				.collect(Collectors.toList());;
		this.categorias = categoriaService.getAll()
				.stream()
				.filter(c -> c.getGrupo().getId().equals(10))
				.collect(Collectors.toList());
	}
	

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnRemover;

    @FXML
    private ComboBox<Orcamento> comboOrcamentos;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Rendimento> tblRendimento;

    @FXML
    private TableColumn<Rendimento, Integer> clnId;

    @FXML
    private TableColumn<Rendimento, String> clnDescricao;

    @FXML
    private TableColumn<Rendimento, Date> clnData;

    @FXML
    private TableColumn<Rendimento, Double> clnValor;
    
    @FXML
    private TableColumn<Rendimento, String> clnRecebido;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtDescricao;

    @FXML
    private DatePicker dtData;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<String> comboRecebido;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    @FXML
    void cancelar(ActionEvent event) {
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
		this.comboCategoria.getSelectionModel().selectFirst();
		this.comboRecebido.getSelectionModel().selectFirst();
    }

    @FXML
    void remover(ActionEvent event) {
    	this.btnNovo.setDisable(true);
    	this.btnEditar.setDisable(true);
     	this.btnRemover.setDisable(true);
     	
     	Alert alert = new Alert(AlertType.CONFIRMATION);
     	alert.setTitle("Remover");
     	alert.setHeaderText("Confirmação");
     	alert.setContentText("Deseja remover este rendimento?");

     	Optional<ButtonType> result = alert.showAndWait();
     	if (result.get() == ButtonType.OK){
     		Response resp = this.service.removeById(new Integer(this.txtId.getText()));
     		
     		if(resp.getSuccess()) {
        		this.rendimentos = service.getAll();
        		this.loadRendimentos(this.comboOrcamentos.getValue());
        		resetForm();
     		}else {
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
    	Date data = Date.from(this.dtData.getValue().atStartOfDay(defaultZoneId).toInstant());
    
    	Response resp = new Response(false, "Erro ao salvar");
    	if(this.txtId.getText().isEmpty()) {
    		resp = this.service.save(
    				this.txtDescricao.getText(),
    				this.txtValor.getText().replace(",", "."),
    				data,
    				this.comboRecebido.getValue(),
    				this.comboCategoria.getValue().getId(),
    				this.comboOrcamentos.getValue().getId());
    	}else {
    		resp = this.service.update(
    				this.txtId.getText(),
    				this.txtDescricao.getText(),
    				this.txtValor.getText().replace(",", "."),
    				this.valorAnterior,
    				this.statusAnterior,
    				data,
    				this.comboRecebido.getValue(),
    				this.comboCategoria.getValue().getId(),
    				this.comboOrcamentos.getValue().getId());
    	}
    	
    	
    	if(resp.getSuccess()) {
    		this.rendimentos = service.getAll();
    		this.loadRendimentos(this.comboOrcamentos.getValue());
    		resetForm();
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erro");
    		alert.setContentText(resp.getMessage());
    		alert.showAndWait();
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.loadOrcamentos();
		this.loadRendimentos(this.comboOrcamentos.getValue());
		this.loadCategorias();
		
		List<String> recebido =  new ArrayList<String>();
		recebido.add("Sim");
		recebido.add("Não");
		ObservableList<String> obsRecebido = FXCollections.observableArrayList(recebido);
		this.comboRecebido.setItems(obsRecebido);

				
		
		this.tblRendimento.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> this.preencherFormulario(newValue));
		
		this.comboOrcamentos.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> this.loadRendimentos(newValue)
				);
		
	}
	
	private void loadRendimentos(Orcamento orcamento) {
		this.rendimentos = service.getAll();
		this.rendimentos = this.rendimentos
				.stream()
				.filter(r -> r.getOrcamento().getId().equals(orcamento.getId()))
				.collect(Collectors.toList());
		
		this.clnId.setCellValueFactory(new PropertyValueFactory<Rendimento,Integer>("id"));
		this.clnDescricao.setCellValueFactory(new PropertyValueFactory<Rendimento,String>("descricao"));
		this.clnData.setCellValueFactory(new PropertyValueFactory<Rendimento,Date>("data"));
		this.clnValor.setCellValueFactory(new PropertyValueFactory<Rendimento,Double>("valor"));
		this.clnRecebido.setCellValueFactory(new PropertyValueFactory<Rendimento,String>("recebido"));
		
		this.clnData.setCellFactory(column -> this.formatColumnDate(column));
		this.clnValor.setCellFactory(column -> this.formatColumnCurrency(column));
		this.clnRecebido.setCellFactory(column -> this.formatColumnBoolean(column));
		
		this.observableListRendimentos = FXCollections.observableList(this.rendimentos);
		this.tblRendimento.setItems(this.observableListRendimentos);
		this.tblRendimento.refresh();
		
		this.lblTotal.setText(Uteis.convertToCurrency(this.rendimentos
				.stream()
				.map(o -> o.getValor())
				.reduce((v1, v2) -> v1 + v2)
				.orElse(new Double(0)))
				);
		
	}
	
	private void loadOrcamentos() {
		this.observableListOrcamentos = FXCollections.observableArrayList(this.orcamentos);
		this.comboOrcamentos.setItems(this.observableListOrcamentos);
		this.comboOrcamentos.getSelectionModel().selectFirst();
	}
	
	private void loadCategorias() {
		this.observableListCategorias = FXCollections.observableArrayList(this.categorias);
		this.comboCategoria.setItems(this.observableListCategorias);
	}
	
	private void preencherFormulario(Rendimento rendimento) {
		if(rendimento != null) {
			this.txtId.setText(rendimento.getId().toString());
			this.txtDescricao.setText(rendimento.getDescricao());
			this.dtData.setValue(rendimento.getData().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate());
			this.txtValor.setText(rendimento.getValor().toString());
			this.comboCategoria.setValue(rendimento.getCategoria());
			if(rendimento.getRecebido().equals("S"))
				this.comboRecebido.setValue("Sim");
			else
				this.comboRecebido.setValue("Não");
			this.btnEditar.setDisable(false);
			this.btnRemover.setDisable(false);
			this.valorAnterior = rendimento.getValor().toString();
			this.statusAnterior = rendimento.getRecebido();
		}
	}
	
	private void disableForm(boolean bool) {
		this.txtId.setDisable(bool);
		this.txtDescricao.setDisable(bool);
		this.dtData.setDisable(bool);
		this.txtValor.setDisable(bool);
		this.btnCancelar.setDisable(bool);
		this.btnSalvar.setDisable(bool);
		this.comboCategoria.setDisable(bool);
		this.comboRecebido.setDisable(bool);
	}
	
	private void clearForm() {
		this.txtId.clear();
		this.txtDescricao.clear();
		this.dtData.setValue(null);
		this.txtValor.clear();
		this.comboRecebido.setValue(null);
		this.comboCategoria.setValue(null);
	}
	
	private void resetForm() {
		this.disableForm(true);
    	this.clearForm();
    	this.tblRendimento.getSelectionModel().clearSelection();
    	this.btnNovo.setDisable(false);
	}
	
	
	private  TableCell<Rendimento, Date> formatColumnDate(TableColumn<Rendimento, Date> column){
		TableCell<Rendimento, Date> cell = new TableCell<Rendimento, Date>() {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if(item != null)
                    this.setText(format.format(item));
                }
            }
        };

        return cell;
	}
	
	private  TableCell<Rendimento, Double> formatColumnCurrency(TableColumn<Rendimento, Double> column){
		TableCell<Rendimento, Double> cell = new TableCell<Rendimento, Double>() {

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if(item != null)
                    this.setText(Uteis.convertToCurrency(item));
                }
            }
        };

        return cell;
	}
	
	
	private  TableCell<Rendimento, String> formatColumnBoolean(TableColumn<Rendimento, String> column){
		TableCell<Rendimento, String> cell = new TableCell<Rendimento, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if(item != null) {
                    	if(item.equals("S"))
                    		this.setText("Sim");
                    	else
                    		this.setText("Não");
                    }
                    	
                    
                }
            }
        };

        return cell;
	}

}
