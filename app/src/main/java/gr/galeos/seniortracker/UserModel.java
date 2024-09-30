package gr.galeos.seniortracker;

import java.util.Locale;

import gr.galeos.seniortracker.models.User;

public class UserModel {
    private static volatile UserModel shared;
    public User user = null;
    private String lang = Locale.getDefault().getLanguage().equals("el") ? "el" : "en";

    private UserModel() {
    }

    public void setUser(String id, String firstname, String lastname, String email, String phone, String accountType) {
        user = new User(id, firstname, lastname, email, phone, accountType);
    }

    public static UserModel getInstance() {
        if (shared == null) {
            synchronized (UserModel.class) {
                if (shared == null) {
                    shared = new UserModel();
                }
            }
        }
        return shared;
    }
}

