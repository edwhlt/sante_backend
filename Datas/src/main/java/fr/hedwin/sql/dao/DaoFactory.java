package fr.hedwin.sql.dao;

import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.tables.*;
import fr.hedwin.sql.utils.SQLConsumer;
import fr.hedwin.sql.utils.SQLFunction;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DaoFactory {

    private static String SUPP_URL = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private static DaoFactory daoFactory;

    private final String url;
    private final String username;
    private final String password;

    DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {
        Properties prop = new Properties();
        try{
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        }catch (IOException e){
            System.out.println("Fichier de configuration introuvable");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("'com.mysql.cj.jdbc.Driver' introuvable !");
            e.printStackTrace();
        }
        String hostname = prop.getProperty("database_hostname", "db");
        String port = prop.getProperty("database_port", "3306");
        String db = prop.getProperty("database", "food");
        String user = prop.getProperty("user", "root");
        String password = prop.getProperty("password", "h^ltSfg9mfdq");

        if(daoFactory == null) daoFactory = new DaoFactory( "jdbc:mysql://"+hostname+":"+port+"/"+db+"?allowMultiQueries=true", user, password);
        return daoFactory;
    }

    public Connection connection(String information) throws SQLException {
        Connection connexion = DriverManager.getConnection(url, username, password);
        //connexion.setAutoCommit(false);
        if(information != null) System.out.println("SQL Info: "+information);
        return connexion;
    }

    public RecetteDaoImpl getRecetteDao(){
        return new RecetteDaoImpl(this);
    }

    public WeekDaoImpl getWeekDao(){
        return new WeekDaoImpl(this);
    }

    public ProfilDaoImpl getProfilDao(){
        return new ProfilDaoImpl(this);
    }

    public UserDaoImpl getUserDao(){
        return new UserDaoImpl(this);
    }

    public RepasDaoImpl getRepasDao(){
        return new RepasDaoImpl(this);
    }

    public CiqualIngredientDaoImpl getCiqualIngredientDao(){
        return new CiqualIngredientDaoImpl(this);
    }

    public FoodElementDaoImpl getFoodElementDao(){
        return new FoodElementDaoImpl(this);
    }

    public void request(String request, SQLConsumer<PreparedStatement> procedure) throws DaoException {
        Connection connexion = null;
        PreparedStatement statement = null;
        try {
            connexion = connection(null);
            statement = connexion.prepareStatement(request);
            procedure.accept(statement);
            System.out.println(request);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw DaoException.BDDErrorAcess(throwables);
        } finally {
            if(connexion != null) {
                try {
                    connexion.close();
                } catch (SQLException throwables) {
                    throw DaoException.BDDErrorAcess(throwables);
                }
            }
        }
    }

    public void request(String request) throws DaoException {
        request(request, preparedStatement -> {});
    }

    public <T> T request(String request, SQLFunction<ResultSet, T> procedure) throws DaoException {
        Connection connexion = null;
        Statement statement = null;
        ResultSet resultat = null;
        try {
            connexion = connection(null);
            statement = connexion.createStatement();
            System.out.println(request);
            resultat = statement.executeQuery(request);
            return procedure.apply(resultat);
        } catch (SQLException throwables) {
            throw DaoException.BDDErrorAcess(throwables);
        } finally {
            if(connexion != null) {
                try {
                    connexion.close();
                } catch (SQLException throwables) {
                    throw DaoException.BDDErrorAcess(throwables);
                }
            }
        }
    }

    public <T> T request(String request, String getRequest, SQLConsumer<PreparedStatement> procedure, SQLFunction<ResultSet, T> result) throws DaoException {
        Connection connexion = null;
        PreparedStatement statement = null;
        ResultSet resultat = null;
        try {
            connexion = connection(null);
            statement = connexion.prepareStatement(request);
            procedure.accept(statement);
            statement.executeUpdate();

            System.out.println(request);
            ResultSet resultSet = statement.executeQuery(getRequest);

            return result.apply(resultSet);
        } catch (SQLException throwables) {
            throw DaoException.BDDErrorAcess(throwables);
        } finally {
            if(connexion != null) {
                try {
                    connexion.close();
                } catch (SQLException throwables) {
                    throw DaoException.BDDErrorAcess(throwables);
                }
            }
        }
    }

}
