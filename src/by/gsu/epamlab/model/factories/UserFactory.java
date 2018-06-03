package by.gsu.epamlab.model.factories;

import by.gsu.epamlab.model.impl.DbUserImpl;
import by.gsu.epamlab.model.interfaces.IUserDAO;

public class UserFactory {

    public static IUserDAO getClassFromFactory(){
        //return new HardcodeUserImpl();
        return new DbUserImpl();
    }
}