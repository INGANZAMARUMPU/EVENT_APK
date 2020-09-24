package bi.konstrictor.ikirori;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    TextView lbl_guest_place, lbl_guest_type, lbl_guest_solde, lbl_guest_fullname,
            lbl_guest_others,lbl_guest_inscr_date, lbl_guest_phone;
    ImageView img_guest_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lbl_guest_place = findViewById(R.id.lbl_guest_place);
        lbl_guest_type = findViewById(R.id.lbl_guest_type);
        lbl_guest_solde = findViewById(R.id.lbl_guest_solde);
        lbl_guest_fullname = findViewById(R.id.lbl_guest_fullname);
        lbl_guest_others = findViewById(R.id.lbl_guest_others);
        lbl_guest_inscr_date = findViewById(R.id.lbl_guest_inscr_date);
        lbl_guest_phone = findViewById(R.id.lbl_guest_phone);

        img_guest_profile = findViewById(R.id.img_guest_profile);

    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.user_menu, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
        }
        return super.onOptionsItemSelected(item);
    }

    public void scanQr(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "you cancelled the scan", Toast.LENGTH_LONG).show();
            } else {
                setUserQR(result.getContents());
                Toast.makeText(this, "fetching guest data...", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUserQR(String qr_data) {

    }
}
