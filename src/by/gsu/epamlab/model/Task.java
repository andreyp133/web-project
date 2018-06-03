package by.gsu.epamlab.model;

import java.sql.Date;

public class Task {
    private String id;
    private String text;
    private Date  completionDate;
    private String  fileName;


    public Task(String id, String text, Date completionDate, String fileName) {
        this.id = id;
        this.text = text;
        this.completionDate = completionDate;
        this.fileName = fileName;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String  getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}