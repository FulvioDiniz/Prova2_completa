package prova2.controlechaves.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prova2.controlechaves.dao.core.GenericJDBCDAO;
import prova2.controlechaves.model.Servidor;
import prova2.controlechaves.model.Situacao;

public class ServidorDAO extends GenericJDBCDAO<Servidor, Long> {

    private static final String FIND_ALL_QUERY = "SELECT codigo, nome, foto, senha, contato, situacao FROM servidor WHERE situacao = 'ATIVO' ";
    private static final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";
    private static final String UPDATE_QUERY = "UPDATE servidor SET nome=?, foto=?, senha=?, contato=?, situacao=? WHERE codigo=?";
    private static final String CREATE_QUERY = "INSERT INTO servidor (nome, foto, senha, contato, situacao) VALUES (?, ?, ?, ?, ?)";
    private static final String REMOVE_QUERY = "DELETE FROM servidor WHERE codigo=?";

    public ServidorDAO(Connection conexao) {
        super(conexao);
    }

    @Override
    protected Servidor toEntity(ResultSet resultSet) throws SQLException {
        Servidor servidor = new Servidor();
        servidor.setCodigo(resultSet.getLong("codigo"));
        servidor.setNome(resultSet.getString("nome"));
        servidor.setFoto(resultSet.getString("foto"));
        servidor.setSenha(resultSet.getString("senha"));
        servidor.setContato(resultSet.getString("contato"));

        if (resultSet.getString("situacao").equals("ATIVO")) {
            servidor.setSituacao(Situacao.ATIVO);
        } else {
            servidor.setSituacao(Situacao.INATIVO);
        }
        return servidor;
    }

    @Override
    protected void addParameters(PreparedStatement resultSet, Servidor entity) throws SQLException {
        resultSet.setString(1, entity.getNome());
        resultSet.setString(2, entity.getFoto());
        resultSet.setString(3, entity.getSenha());
        resultSet.setString(4, entity.getContato());
        resultSet.setString(5, entity.getSituacao().toString());
        if (entity.getCodigo() != null) {
            resultSet.setLong(6, entity.getCodigo());
        }
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

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Servidor entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Servidor entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }

    public List<Servidor> pesquisar(String nome) {
        String query = FIND_ALL_QUERY;
        query += " AND LOWER(nome) like ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nome + "%");
            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<Servidor>();
    }

}
