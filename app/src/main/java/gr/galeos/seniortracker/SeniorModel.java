package gr.galeos.seniortracker;

import java.util.Locale;
import gr.galeos.seniortracker.models.User;

public class SeniorModel {

    private static volatile SeniorModel shared;
    public User user = null;
    private String lang = Locale.getDefault().getLanguage().equals("el") ? "el" : "en";

    private SeniorModel() {
    }

    public void setUser(String id, String firstname, String lastname, String email, String phone, String accountType) {
        user = new User(id, firstname, lastname, email, phone, accountType);
    }

    public static SeniorModel getInstance() {
        if (shared == null) {
            synchronized (SeniorModel.class) {
                if (shared == null) {
                    shared = new SeniorModel();
                }
            }
        }
        return shared;
    }
}
