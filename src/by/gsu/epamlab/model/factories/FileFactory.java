package by.gsu.epamlab.model.factories;

import by.gsu.epamlab.model.impl.FileImpl;
import by.gsu.epamlab.model.interfaces.IFileDAO;

public class FileFactory {
    public static IFileDAO getClassFromFactory(){
        return new FileImpl();
    }
}