package bi.konstrictor.ikirori;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import bi.konstrictor.ikirori.R;

public class LoginActivity extends AppCompatActivity {

    private EditText field_login_username, field_login_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        field_login_username = findViewById(R.id.field_login_username);
        field_login_password = findViewById(R.id.field_login_password);
    }

    public void nextActivity(View view) {

    }
}
