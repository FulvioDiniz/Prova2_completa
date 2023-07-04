package prova2.controlechaves.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prova2.controlechaves.dao.core.ConnectionFactory;
import prova2.controlechaves.dao.core.DAOFactory;
import prova2.controlechaves.dao.core.GenericJDBCDAO;
import prova2.controlechaves.model.Chave;
import prova2.controlechaves.model.Situacao;

public class ChaveDAO extends GenericJDBCDAO<Chave, Long> {

    public ChaveDAO(Connection connection) {
        super(connection);
        // TODO Auto-generated constructor stub
    }

    private static final String FIND_ALL_QUERY = "SELECT codigo, sala, disponivel, situacao FROM chave WHERE situacao = 'ATIVO' ";

    private static final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";

    private static final String FIND_ALL_EMPRESTADA = FIND_ALL_QUERY + "AND disponivel= false ";

    private static final String FIND_ALL_DISPONIVEIS = FIND_ALL_QUERY + "AND disponivel= true ";

    private static final String UPDATE_QUERY = "UPDATE chave SET sala=?, disponivel=?, situacao=? WHERE codigo=?";
    //private static final String UPDATE_QUERY = "UPDATE chave SET  disponivel=? WHERE codigo=?";

    private static final String CREATE_QUERY = "INSERT INTO chave (sala, disponivel, situacao) VALUES (?, ?, ?)";

    private static final String REMOVE_QUERY = "DELETE FROM chave WHERE codigo=?";

    // private Long codigo;
    // private String sala;
    // private boolean disponivel;
    // private Situacao situacao = Situacao.ATIVO;

    @Override
    protected Chave toEntity(ResultSet resultSet) throws SQLException {
        Chave chave = new Chave();
        chave.setCodigo(resultSet.getLong("codigo"));
        chave.setSala(resultSet.getString("sala"));
        chave.setDisponivel(resultSet.getBoolean("disponivel"));
        if (resultSet.getString("situacao").equals("ATIVO")) {
            chave.setSituacao(Situacao.ATIVO);
        } else {
            chave.setSituacao(Situacao.INATIVO);
        }
        return chave;
    }

    @Override
    protected void addParameters(PreparedStatement resultSet, Chave entity) throws SQLException {
        //resultSet.setLong(1, entity.getCodigo());
        resultSet.setString(1, entity.getSala());
        resultSet.setBoolean(2, entity.isDisponivel());
        resultSet.setString(3, entity.getSituacao().toString());
        if (entity.getCodigo() != null) {
            resultSet.setLong(4, entity.getCodigo());
        }
    }

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Chave entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setKeyInStatementFromEntity'");
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setKeyInStatement'");
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Chave entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setKeyInEntity'");
    }

    @Override
    protected String findByKeyQuery() {
        return FIND_BY_KEY_QUERY;
    }

    @Override
    protected String findAllQuery() {
        return FIND_ALL_QUERY;
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected String createQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected String removeQuery() {
        return REMOVE_QUERY;
    }

    protected String findAllEmprestada() {
        return FIND_ALL_EMPRESTADA;
    }

    protected String findAllDisponiveis() {
        return FIND_ALL_DISPONIVEIS;
    }

    // PreparedStatement statement = connection.prepareStatement(query);
    // if (filtro.getCodigo() != null) {
    // statement.setLong(1, filtro.getCodigo());
    // parametro++;
    // }
    // System.out.println(statement.toString());
    // ResultSet resultSet = statement.executeQuery();
    // return toEntityList(resultSet);

    public List<Chave> pesquisarDisponiveis(String sala) {
        String query = FIND_ALL_QUERY;
        query += " AND LOWER(sala) like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + sala + "%");
            ResultSet resultSet = statement.executeQuery();
            //System.out.println(query);
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<Chave>();
    }

    public List<Chave> pesquisarEmprestadas(String sala) {
        String query = FIND_ALL_QUERY;
        query += " AND LOWER(sala) like ? AND disponivel = false";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + sala + "%");
            ResultSet resultSet = statement.executeQuery();
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<Chave>();
    }

    public List<Chave> buscarTodasEmprestadas() {
        String query = FIND_ALL_EMPRESTADA;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<Chave>();
    }

    public List<Chave> buscarTodasDisponiveis() {
        String query = FIND_ALL_DISPONIVEIS;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<Chave>();
    }

    public static void main(String[] args) {

        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("192.168.13.106:5432/postgres",
                "postgres", "12345");
        DAOFactory daoFactory = new DAOFactory(conexaoFactory);
        ChaveDAO dao = null;
        try {
            daoFactory.abrirConexao();
            dao = daoFactory.getDAO(ChaveDAO.class);
            List<Chave> chave = dao.pesquisarDisponiveis("3");
            //List<Chave> chave = dao.buscarTodasEmprestadas();
            //List<Chave> chave = dao.buscarTodasDisponiveis(); -- correto
            // List<Chave> chave = dao.findAll();
            for (Chave v : chave) {
                System.out.println(v);
                System.out.println("-----------------------");
            }
        } catch (SQLException ex) {
            ChaveDAO.showSQLException(ex);
        } finally {
            daoFactory.fecharConexao();
        }

    }

}
