package by.gsu.epamlab.model.impl;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.IFileDAO;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class FileImpl implements IFileDAO {

    private static final int ROOT_INDEX = 0;
    private static final String DIR_NAME = "files";
    private static final String BACK_SLASH = "\\\\";

    @Override
    public String upload(Part part, String path) throws DaoException {
        path = getPathToDir(path);
        String fullFileName = extractFileName(part); //Имя файла вместе с его типом
        if(fullFileName.equals("")){ //Проверка наличия имени у файла
            return "";
        } else {
            //Проверка конечной директории на существование
            File fileSaveDir = new File(path);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir(); // Создание директории, если её не существовало
            }
            //Проверка имени файла на совпадение с именами других фалов в папке
            String filePath = path + File.separator + fullFileName;
            synchronized (this){
                File file = new File(filePath);
                if(file.exists()){
                    String fileName = Constants.EMPTY_STRING;
                    String fileType = Constants.EMPTY_STRING;
                    //Разбор на имя и тип файла
                    if(fullFileName.contains(".")){
                        String[] fileParts = fullFileName.split("\\.");
                        //если имя содержит более чем одну точку - соединяем все части кроме последей, в одно целое имя.
                        for(int i = 0; i < fileParts.length - 1; i++){
                            fileName += fileParts[i];
                            //доставляем точку после каждой части имени, за исключением последней части имени
                            if(i < fileParts.length - 2){
                                fileName += ".";
                            }
                        }
                        fileType = fileParts[fileParts.length - 1]; //получение типа файла
                    } else {
                        fileName = fullFileName; //имя для без типового файла
                    }
                    String newFileName = fileName;//копия имени для его изменения
                    int copyCount = 0; //количество копий файла
                    while (file.exists()){
                        copyCount++;
                        newFileName = fileName + " (" + copyCount + ")";
                        //путь к папке + разделитель + новое имя + точка + тип
                        filePath = path + File.separator + newFileName + "." + fileType;
                        file = new File(filePath);
                    }
                    //получение окончательного имени с типом файла
                    fullFileName = newFileName + "." + fileType;
                }
                //Запись файла
                try{
                    part.write(filePath);
                    return fullFileName;
                } catch (IOException e){
                    throw new DaoException(e);
                }
            }
        }
    }

    @Override
    public void delete(String fileName, String path) throws DaoException {
        path = getPathToDir(path);
        String filePath = path + File.separator + fileName;
        File deleteFile = new File(filePath);
        if(!deleteFile.delete()){
            throw new DaoException("File for deleting not found or isn't available");
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
    public static String getPathToDir(String path){
        String pathToDir = path.split(BACK_SLASH)[ROOT_INDEX];
        pathToDir += File.separator + DIR_NAME;
        return pathToDir;
    }
}