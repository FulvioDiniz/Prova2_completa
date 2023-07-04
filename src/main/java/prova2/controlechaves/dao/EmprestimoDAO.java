package prova2.controlechaves.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import prova2.controlechaves.dao.core.GenericJDBCDAO;
import prova2.controlechaves.model.Chave;
import prova2.controlechaves.model.Emprestimo;
import prova2.controlechaves.model.Servidor;
import prova2.controlechaves.model.Situacao;

public class EmprestimoDAO extends GenericJDBCDAO<Emprestimo, Long> {

    private static final String FIND_ALL_QUERY = "SELECT e.codigo AS codigoEmprestimo, e.data_retirada, e.data_devolucao, e.situacao AS situacaoEmprestimo, sr.codigo AS codigoServidorRetirou, sr.nome AS nomeServidorRetirou, sr.foto AS fotoServidorRetirou, sr.senha AS senhaServidorRetirou, sr.contato AS contatoServidorRetirou, sr.situacao AS situacaoServidorRetirou, sd.codigo AS codigoServidorDevolveu, sd.nome AS nomeServidorDevolveu, sd.foto AS fotoServidorDevolveu, sd.senha AS senhaServidorDevolveu, sd.contato AS contatoServidorDevolveu, sd.situacao AS situacaoServidorDevolveu, c.codigo AS codigoChave, c.sala, c.disponivel, c.situacao AS situacaoChave FROM emprestimo AS e INNER JOIN servidor AS sr ON e.codigo_servidor_retirou = sr.codigo LEFT JOIN servidor AS sd ON e.codigo_servidor_devolveu = sd.codigo INNER JOIN chave AS c ON e.codigo_chave = c.codigo WHERE e.situacao = 'ATIVO' ";
    private static final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";
    private static final String UPDATE_QUERY = "UPDATE emprestimo SET data_retirada=?, data_devolucao=?, codigo_servidor_retirou=?, codigo_servidor_devolveu=?, codigo_chave=?, situacao=? WHERE codigo=?";
    private static final String CREATE_QUERY = "INSERT INTO emprestimo (data_retirada, data_devolucao, codigo_servidor_retirou, codigo_servidor_devolveu, codigo_chave, situacao) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String REMOVE_QUERY = "DELETE FROM emprestimo WHERE codigo=?";

    public EmprestimoDAO(Connection conexao) {
        super(conexao);
    }

    @Override
    protected Emprestimo toEntity(ResultSet resultSet) throws SQLException {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setCodigo(resultSet.getLong("codigoEmprestimo"));
        emprestimo.setDataRetirada(resultSet.getTimestamp("data_retirada").toLocalDateTime());
        // verifica primeiro se foi feita uma devolução
        if (resultSet.getTimestamp("data_devolucao") != null) {
            emprestimo.setDataDevolucao(resultSet.getTimestamp("data_devolucao").toLocalDateTime());
        } else {
            emprestimo.setDataDevolucao(null);
        }
        Servidor servidorRetirou = new Servidor();
        servidorRetirou.setCodigo(resultSet.getLong("codigoServidorRetirou"));
        servidorRetirou.setNome(resultSet.getString("nomeServidorRetirou"));
        servidorRetirou.setFoto(resultSet.getString("fotoServidorRetirou"));
        servidorRetirou.setSenha(resultSet.getString("senhaServidorRetirou"));
        servidorRetirou.setContato(resultSet.getString("contatoServidorRetirou"));
        if (resultSet.getString("situacaoServidorRetirou").equals("ATIVO")) {
            servidorRetirou.setSituacao(Situacao.ATIVO);
        } else {
            servidorRetirou.setSituacao(Situacao.INATIVO);
        }
        emprestimo.setServidorRetirou(servidorRetirou);
        // verifica primeiro se foi feita uma devolução
        if (resultSet.getLong("codigoServidorDevolveu") != 0) {
            
        Servidor servidorDevolveu = new Servidor();
        servidorDevolveu.setCodigo(resultSet.getLong("codigoServidorDevolveu"));
        servidorDevolveu.setNome(resultSet.getString("nomeServidorDevolveu"));
        servidorDevolveu.setFoto(resultSet.getString("fotoServidorDevolveu"));
        servidorDevolveu.setSenha(resultSet.getString("senhaServidorDevolveu"));
        servidorDevolveu.setContato(resultSet.getString("contatoServidorDevolveu"));
        if (resultSet.getString("situacaoServidorDevolveu").equals("ATIVO")) {
            servidorDevolveu.setSituacao(Situacao.ATIVO);
        } else {
            servidorDevolveu.setSituacao(Situacao.INATIVO);
        }
        emprestimo.setServidorDevolveu(servidorDevolveu);
        } else {
            emprestimo.setServidorDevolveu(null);
        }
        Chave chave = new Chave();
        chave.setCodigo(resultSet.getLong("codigoChave"));
        chave.setSala(resultSet.getString("sala"));
        chave.setDisponivel(resultSet.getBoolean("disponivel"));
        if (resultSet.getString("situacaoChave").equals("ATIVO")) {
            chave.setSituacao(Situacao.ATIVO);
        } else {
            chave.setSituacao(Situacao.INATIVO);
        }
        emprestimo.setChave(chave);
        if (resultSet.getString("situacaoEmprestimo").equals("ATIVO")) {
            emprestimo.setSituacao(Situacao.ATIVO);
        } else {
            emprestimo.setSituacao(Situacao.INATIVO);
        }
        return emprestimo;
    }

    @Override
    protected void addParameters(PreparedStatement resultSet, Emprestimo entity) throws SQLException {
        resultSet.setTimestamp(1, Timestamp.valueOf(entity.getDataRetirada()));
        // verifica primeiro se foi feita uma devolução
        if (entity.getDataDevolucao() == null) {
            resultSet.setNull(2, Types.TIMESTAMP);
        } else {
            resultSet.setTimestamp(2, Timestamp.valueOf(entity.getDataDevolucao()));
        }
        resultSet.setLong(3, entity.getServidorRetirou().getCodigo());
        // verifica primeiro se foi feita uma devolução
        if (entity.getServidorDevolveu() == null) {
            resultSet.setNull(4, Types.BIGINT);
        } else {
            resultSet.setLong(4, entity.getServidorDevolveu().getCodigo());
        }
        resultSet.setLong(5, entity.getChave().getCodigo());
        resultSet.setString(6, entity.getSituacao().toString());
        if (entity.getCodigo() != null) {
            resultSet.setLong(7, entity.getCodigo());
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
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Emprestimo entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Emprestimo entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }

    public Emprestimo buscarNaoDevolvido(Chave chave) {
        String query = FIND_ALL_QUERY;
        query += " AND c.sala like ? AND e.data_devolucao is null";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,  chave.getSala());
            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            List<Emprestimo> emprestimos = toEntityList(resultSet);
            return emprestimos.get(0);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return null;
    }
    
}
