package by.gsu.epamlab.model.factories;

import by.gsu.epamlab.model.impl.DbTasksImpl;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

public class TasksFactory {
    public static ITaskDAO getClassFromFactory(){
        return new DbTasksImpl();
    }
}