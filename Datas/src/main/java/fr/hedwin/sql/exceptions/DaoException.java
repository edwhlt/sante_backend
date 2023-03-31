package fr.hedwin.sql.exceptions;

import java.sql.SQLException;

public class DaoException extends Exception{
    public DaoException(String message) {
        super(message);
    }
    public static DaoException BDDErrorAcess(SQLException sqlException){
        return new DaoException("Impossible de communiquer avec la base de données : "+sqlException.getMessage());
    }
}
