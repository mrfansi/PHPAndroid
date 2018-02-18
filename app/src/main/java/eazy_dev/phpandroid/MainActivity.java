package eazy_dev.phpandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNamaBrg;
    private EditText txtStokBrg;
    private EditText txtHrgBrg;

    private Button btnTambahBrg;
    private Button btnListBrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNamaBrg = findViewById(R.id.txtNamaBrg);
        txtStokBrg = findViewById(R.id.txtStokBrg);
        txtHrgBrg = findViewById(R.id.txtHrgBrg);

        btnTambahBrg = findViewById(R.id.btnTambahBrg);
        btnListBrg = findViewById(R.id.btnListBrg);

        btnTambahBrg.setOnClickListener(this);
        btnListBrg.setOnClickListener(this);

    }

    private void AddBrg() {
        final String NamaBrg = txtNamaBrg.getText().toString().trim();
        final String StokBrg = txtStokBrg.getText().toString().trim();
        final String HrgBrg = txtHrgBrg.getText().toString().trim();

        @SuppressLint("StaticFieldLeak")
        class AddBrg extends AsyncTask<Void, Void, String>{
            private ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Sedang menambahkan...","Mohon tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_BRG_NAMA, NamaBrg);
                params.put(Konfigurasi.KEY_BRG_STOK, StokBrg);
                params.put(Konfigurasi.KEY_BRG_HRG, HrgBrg);

                ReqHandler rh = new ReqHandler();
                return rh.sendPostRequest(Konfigurasi.URL_ADD, params);
            }
        }

        AddBrg Ad = new AddBrg();
        Ad.execute();
    }

    @Override
    public void onClick(View view) {
        if (view == btnTambahBrg) {
            AddBrg();
        }

        if (view == btnListBrg) {
            startActivity(new Intent(this, TampilkanSemuaBarang.class));
        }
    }
}
