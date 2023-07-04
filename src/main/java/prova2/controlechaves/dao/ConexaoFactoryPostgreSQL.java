package prova2.controlechaves.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import prova2.controlechaves.dao.core.ConnectionFactory;

public class ConexaoFactoryPostgreSQL implements ConnectionFactory {
    private String dbURL = "192.168.13.106:5432/batalha";
    private String user = "postgres";
    private String password = "12345";
    private static final String classeDriver = "org.postgresql.Driver";     
    private static final String caminho = "jdbc:postgresql";
    
    /**
     * 
     * @param dbURL     localhost:5432/poov
     * @param user      postgres
     * @param password  12345
     */
    public ConexaoFactoryPostgreSQL(String dbURL, String user, String password) {
        this.dbURL = dbURL;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        String url = caminho + "://" + dbURL;
        Connection conexao = null;
        try {
            System.out.println("Conectando com o banco de dados.");
            Class.forName(classeDriver);
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão com o banco de dados estabelecida.");
            return conexao;
        } catch (Exception e) {
            System.out.println("Erro obtendo uma conexão com o banco de dados.");
            e.printStackTrace();
            return null;
        }
    }

}
