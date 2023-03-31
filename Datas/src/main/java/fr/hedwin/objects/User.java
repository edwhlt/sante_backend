package fr.hedwin.objects;

import java.util.Date;

public class User {

    private int id_user;
    private String name;
    private String email;
    private String password;
    private Date date;

    public User(int id_user, String name, String email, String password, Date date) {
        this.id_user = id_user;
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
    }

    public int getId_user() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }
}
