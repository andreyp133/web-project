package by.gsu.epamlab.model;

public class User {
    private String id;
    private String login;

    public User(){ }
    public User(String login){
        this.login = login;
    }
    public User(String id, String login){
        this.id = id;
        this.login = login;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}