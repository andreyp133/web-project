package by.gsu.epamlab.model.exceptions;

public class InvalidLoginOrPasswordException extends DaoException {

    public InvalidLoginOrPasswordException(String message) {
        super(message);
    }
}