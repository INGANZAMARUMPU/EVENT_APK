package bi.konstrictor.ikirori;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomJson extends JSONObject {
    public CustomJson(String json) throws JSONException {
        super(json);
    }

    @Override
    public String getString(String name){
        try {
            return super.getString(name);
        } catch (JSONException e) {
            return "";
        }
    }
}
