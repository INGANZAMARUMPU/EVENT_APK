package bi.konstrictor.ikirori;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KonstrIctor on 24/03/2020.
 */

class Host {
//    public static final String URL = "https://api.rcretraining.com";
    public static final String URL = "http://10.0.2.2:8000";
//    public static final String URL = "http://192.168.8.101:8000";

    public static void refreshToken(final Activity activity){

        final SharedPreferences sharedPreferences = activity.getSharedPreferences("user_login", Context.MODE_PRIVATE);

        String refresh = sharedPreferences.getString("refresh", "");
        String token = sharedPreferences.getString("token", "");

        if (refresh.isEmpty()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        String json = "{\"refresh\":\""+refresh+"\"}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/refresh/").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new  JSONObject(json);
                    String token = jsonObject.getString("access");
                    SharedPreferences.Editor session = sharedPreferences.edit();
                    session.putString("token", token);
                    session.commit();

                } catch (Exception e) {
                    final String message = e.getMessage();
                }
            }
        });
    }
    public static Member extractUser(final Context activity, String token) {
        String[] parts = token.split("\\.");

        byte[] data = Base64.decode(parts[1], Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            Log.i("==== TOKEN ====", text);
            JSONObject json_object = new JSONObject(text);
            Member member = new Member(
                    json_object.getString("user_id"),
                    json_object.getString("username"),
                    json_object.getString("phone"),
                    json_object.getString("mobile"),
                    json_object.getString("services")
            );
            return member;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

