package bi.konstrictor.ikirori;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ServicesForm extends Dialog {

    private final ArrayList<Product> products;
    private Profile profile;
    MainActivity activity;
    TextView lbl_service_fullname, lbl_service_solde, lbl_guest_qtt, lbl_guest_total;
    Spinner combo_service_products;
    Button btn_service_decrease, btn_service_increase, btn_service_cancel, btn_service_submit;
    private Product selected_product;
    private Double quantity;

    public ServicesForm(MainActivity activity, Profile profile, ArrayList<Product> products) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.dialog_service);
        this.activity = activity;
        this.profile = profile;
        this.products = products;

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

        generateLinsteners();
    }
    private void submit() {
        if (validateFields()){

        }
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
                setQuantity(0.);
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
                submit();
            }
        });
        btn_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void setQuantity(Double qtt) {
        Double possible_qtt = profile.ticket.consommable/selected_product.price;
        if (qtt>possible_qtt){
            qtt = possible_qtt;
        }else if(qtt<0){
            qtt = 0.;
        }
        lbl_guest_qtt.setText(qtt.toString());
        lbl_guest_total.setText(Double.toString(selected_product.price*qtt));
        quantity = qtt;
    }
}