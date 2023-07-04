package prova2.controlechaves.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import prova2.controlechaves.dao.ChaveDAO;
import prova2.controlechaves.dao.ConexaoFactoryPostgreSQL;
import prova2.controlechaves.dao.EmprestimoDAO;
import prova2.controlechaves.dao.ServidorDAO;
import prova2.controlechaves.dao.core.ConnectionFactory;
import prova2.controlechaves.dao.core.DAOFactory;
import prova2.controlechaves.model.Chave;
import prova2.controlechaves.model.Emprestimo;
import prova2.controlechaves.model.Servidor;


public class PrimaryController {

    @FXML
    private ListView<Chave> ListViewChave;

    @FXML
    private ListView<Chave> ListViewDevolucao;

    @FXML
    private ListView<Servidor> ListViewServidor;

    @FXML
    private Button ButtonDevolver;

    @FXML
    private Button ButtonEmprestar;

    @FXML
    private Button ButtomBuscar;

    @FXML
    private Button ButtonDevolucao;

    @FXML
    private Button ButtonServidor;

    @FXML
    private TextField TextFieldChave;

    @FXML
    private TextField TextFieldDevolucao;

    @FXML
    private TextField TextFieldNome;

    private DAOFactory daoFactory;
  

    public void setDAOFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public DAOFactory getDAOFactory() {
        return daoFactory;
    }

    // Chave chave =
    // chavesEmprestadasListView.getSelectionModel().getSelectedItem();

    @FXML
    void ButtomBuscarClicado(ActionEvent event) {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                "postgres", "12345");
        DAOFactory daoFactory = new DAOFactory(conexaoFactory);
        ChaveDAO dao = null;
        try {
            daoFactory.abrirConexao();
            dao = daoFactory.getDAO(ChaveDAO.class);
            ListViewChave.getItems().clear();
            if (TextFieldChave.getText().isEmpty()) {
                List<Chave> chave = dao.buscarTodasDisponiveis();
                for (Chave v : chave) {
                    ListViewChave.getItems().add(v);

                }

            } else {
                List<Chave> chave = dao.pesquisarDisponiveis(TextFieldChave.getText());
                for (Chave v : chave) {
                    ListViewChave.getItems().add(v);
                }
            }
        } catch (SQLException ex) {
            ChaveDAO.showSQLException(ex);
        } finally {
            daoFactory.fecharConexao();
        }

    }

    @FXML
    void ButtonDevolucaoClicado(ActionEvent event) {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                "postgres", "12345");
        DAOFactory daoFactory = new DAOFactory(conexaoFactory);
        ChaveDAO dao = null;
        ListViewDevolucao.getItems().clear();
        try {
            daoFactory.abrirConexao();
            dao = daoFactory.getDAO(ChaveDAO.class);
            if (TextFieldDevolucao.getText().isEmpty()) {
                List<Chave> chave = dao.pesquisarEmprestadas(TextFieldDevolucao.getText());
                for (Chave v : chave) {
                    ListViewDevolucao.getItems().add(v);
                }
            } else {
                List<Chave> chave = dao.pesquisarEmprestadas(TextFieldDevolucao.getText());
                for (Chave v : chave) {
                    ListViewDevolucao.getItems().add(v);
                }

            }

        } catch (SQLException ex) {
            ChaveDAO.showSQLException(ex);
        } finally {
            daoFactory.fecharConexao();
        }
    }

    @FXML
    void ButtonServidorClicado(ActionEvent event) {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                "postgres", "12345");
        DAOFactory daoFactory = new DAOFactory(conexaoFactory);
        ServidorDAO dao = null;
        try {
            daoFactory.abrirConexao();
            dao = daoFactory.getDAO(ServidorDAO.class);
            ListViewServidor.getItems().clear();
            if (TextFieldNome.getText().isEmpty()) {
                List<Servidor> servidores = dao.findAll();
                for (Servidor v : servidores) {
                    ListViewServidor.getItems().add(v);
                }

            } else {
                List<Servidor> servidores = dao.pesquisar(TextFieldNome.getText());
                for (Servidor v : servidores) {
                    ListViewServidor.getItems().add(v);
                }
            }
        } catch (SQLException ex) {
            Servidor.showSQLException(ex);
        } finally {
            daoFactory.fecharConexao();
        }

    }

    @FXML
    void ButtonDevolverClicado(ActionEvent event) {
        Chave chave = ListViewDevolucao.getSelectionModel().getSelectedItem();
        Servidor servidor = ListViewServidor.getSelectionModel().getSelectedItem();
        if (chave == null || servidor == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setTitle("ERRO");
            alert.setHeaderText("Erro");
            alert.setContentText("Preencha todos os campos!");
            alert.showAndWait();
        } else {
            ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                    "postgres", "12345");
            DAOFactory daoFactory = new DAOFactory(conexaoFactory);
            EmprestimoDAO dao = null;
            ChaveDAO daoChave = null;
            try {
                daoFactory.abrirConexao();
                dao = daoFactory.getDAO(EmprestimoDAO.class);
                daoChave = daoFactory.getDAO(ChaveDAO.class);
                Emprestimo emprestimo_novo;
                emprestimo_novo = dao.buscarNaoDevolvido(chave);
                emprestimo_novo.setServidorDevolveu(servidor);
                emprestimo_novo.setDataDevolucao(LocalDateTime.now());
                chave.devolver();
                dao.update(emprestimo_novo);
                daoChave.update(chave);
                ButtonDevolucaoClicado(event);
                ButtomBuscarClicado(event);
               
            } catch (SQLException ex) {
                Servidor.showSQLException(ex);
            } finally {
                daoFactory.fecharConexao();
            }

        }

    }

    @FXML
    void ButtonEmprestarClicado(ActionEvent event) {
        Chave chave = ListViewChave.getSelectionModel().getSelectedItem();
        Servidor servidor = ListViewServidor.getSelectionModel().getSelectedItem();
        if (chave == null || servidor == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setTitle("ERRO");
            alert.setHeaderText("Erro");
            alert.setContentText("Preencha todos os campos!");
            alert.showAndWait();
        } else {
            ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                    "postgres", "12345");
            DAOFactory daoFactory = new DAOFactory(conexaoFactory);
            EmprestimoDAO dao = null;
            ChaveDAO daoChave = null;
            try {
                daoFactory.abrirConexao();
                dao = daoFactory.getDAO(EmprestimoDAO.class);
                daoChave = daoFactory.getDAO(ChaveDAO.class);
                Emprestimo emprestimo_novo = new Emprestimo();
                emprestimo_novo.setChave(chave);
                emprestimo_novo.setServidorRetirou(servidor);
                emprestimo_novo.setDataRetirada(LocalDateTime.now());
                chave.retirar();
                dao.create(emprestimo_novo);
                daoChave.update(chave);
                ButtonDevolucaoClicado(event);
                ButtomBuscarClicado(event);
            } catch (SQLException ex) {
                Servidor.showSQLException(ex);
            } finally {
                daoFactory.fecharConexao();
            }

        }

    }

}
