package bi.konstrictor.ikirori;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServicesForm extends Dialog {

    private final ArrayList<Product> products;
    private ArrayList<String> products_str;
    private Profile profile;
    MainActivity activity;
    TextView lbl_service_fullname, lbl_service_solde, lbl_guest_qtt, lbl_guest_total;
    Spinner combo_service_products;
    Button btn_service_decrease, btn_service_increase, btn_service_cancel, btn_service_submit;
    private Product selected_product;
    private Integer quantity = 0;

    public ServicesForm(MainActivity activity, Profile profile, ArrayList<Product> products, ArrayList<String> products_str) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.dialog_service);
        this.activity = activity;
        this.profile = profile;
        this.products = products;
        this.products_str = products_str;

        lbl_service_fullname = findViewById(R.id.lbl_service_fullname);
        lbl_service_solde = findViewById(R.id.lbl_service_solde);
        lbl_guest_qtt = findViewById(R.id.lbl_guest_qtt);
        lbl_guest_total = findViewById(R.id.lbl_guest_total);
        combo_service_products = findViewById(R.id.combo_service_products);
        btn_service_decrease = findViewById(R.id.btn_service_decrease);
        btn_service_increase = findViewById(R.id.btn_service_increase);
        btn_service_cancel = findViewById(R.id.btn_service_cancel);
        btn_service_submit = findViewById(R.id.btn_service_submit);

        lbl_service_fullname.setText(profile.fullname);
        lbl_service_solde.setText(profile.ticket.consommable.toString());

        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.support_simple_spinner_dropdown_item, products_str);
        combo_service_products.setAdapter(adapter);
        generateLinsteners();
    }
    private void submit(final boolean refreshed) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Host.URL+"/api/consommation/").newBuilder();
        String json =
            "{" +
                    "\"quantity\":\""+ quantity +"\", " +
                    "\"product\":\""+ selected_product.id+
                    "\"profile\":\""+ profile.id+
            "\"}";
        RequestBody body = RequestBody.create(json,
                MediaType.parse("application/json; charset=utf-8"));

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Host.getToken(activity))
                .post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Pas d'access réseau", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject json_object = new JSONObject(json);
                    Double total = json_object.getDouble("total");
                    if (total>0){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "operation éffectuée", Toast.LENGTH_LONG).show();
                            }
                        });
                        dismiss();
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Quelque chose s'est mal passée", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    if(!refreshed) submit(true);
                    final String message = e.getMessage();
                    Log.i("==== SERVICE FORM ====", e.getMessage());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "operation échouée", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private boolean validateFields() {
        return true;
    }
    private Product getSpinnerProduct(String product_name) {
        for(Product product : products){
            if(product.name.equalsIgnoreCase(product_name)){
                return product;
            }
        }
        return null;
    }
    private void generateLinsteners() {
        combo_service_products.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_product = getSpinnerProduct(combo_service_products.getSelectedItem().toString());
                setQuantity(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btn_service_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuantity(quantity+1);
            }
        });
        btn_service_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuantity(quantity-1);
            }
        });
        btn_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(false);
            }
        });
        btn_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void setQuantity(Integer qtt) {
        if(selected_product!=null) {
            Double possible_qtt = profile.ticket.consommable / selected_product.price;
            if (qtt > possible_qtt) {
                qtt = possible_qtt.intValue();
            } else if (qtt < 0) {
                qtt = 0;
            }
            lbl_guest_qtt.setText(qtt.toString());
            lbl_guest_total.setText(Double.toString(selected_product.price * qtt));
            quantity = qtt;
        }
    }
}