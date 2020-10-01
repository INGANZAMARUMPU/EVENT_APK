package bi.konstrictor.ikirori;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Member {
    public String id, username, phone, email, services;

    public Member(String id, String username, String phone, String email, String services) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.services = services;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", services=" + services +
                '}';
    }

    public List<String> getServices() {
        try {
            JSONArray json_services = new JSONArray(this.services);
            List<String> services = new ArrayList<String>();
            for (int i = 0; i < json_services.length(); i++) {
                services.add(json_services.getString(i).toLowerCase());
            }
            return services;
        } catch (Exception e){
            return null;
        }
    }
    public boolean hasService(String services){
        return getServices().contains(services.toLowerCase());
    }
}
