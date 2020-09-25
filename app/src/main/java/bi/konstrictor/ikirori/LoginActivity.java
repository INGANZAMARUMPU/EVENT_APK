package bi.konstrictor.ikirori;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private EditText field_login_username, field_login_password;
    private ProgressBar progress_login;
    private View login_form_layout;
    private SharedPreferences sessionPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        field_login_username = findViewById(R.id.field_login_username);
        field_login_password = findViewById(R.id.field_login_password);
        progress_login = findViewById(R.id.progress_login);
        login_form_layout = findViewById(R.id.login_form_layout);

        sessionPreference = getSharedPreferences("user_session", Context.MODE_PRIVATE);

        String refresh = sessionPreference.getString("refresh", "");
        String token = sessionPreference.getString("token", "");

        if(!token.trim().isEmpty() & !refresh.trim().isEmpty()){
            Intent intent = new Intent(this, MainActivity.class);
            if(Host.extractUser(this, token)!=null) startActivity(intent);
        }
    }
    public void nextActivity(View view) {
        progress_login.setVisibility(View.VISIBLE);
        login_form_layout.setVisibility(View.GONE);
        String json =
                "{\"username\":\""+field_login_username.getText() +"\", " +
                   "\"password\":\""+field_login_password.getText()
                +"\"}";
        RequestBody body = RequestBody.create(json,
                MediaType.parse("application/json; charset=utf-8"));

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/api/login/").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(LoginActivity.this, "Pas d'access réseau", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new  JSONObject(json);
                    String token = jsonObject.getString("access");
                    String refresh = jsonObject.getString("refresh");
                    SharedPreferences.Editor session = sessionPreference.edit();
                    session.putString("token", token);
                    session.putString("refresh", token);
                    session.commit();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    final String message = e.getMessage();
                    Log.i("==== LOGIN RESPONS ====", json);
                    Log.i("==== LOGIN MESSAGE ====", e.getMessage());
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "incorrect logins", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        progress_login.setVisibility(View.GONE);
        login_form_layout.setVisibility(View.VISIBLE);
    }
}
