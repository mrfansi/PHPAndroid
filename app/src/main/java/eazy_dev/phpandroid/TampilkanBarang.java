package eazy_dev.phpandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TampilkanBarang extends AppCompatActivity implements View.OnClickListener {

    private EditText IDBrg;
    private EditText NamaBrg;
    private EditText StokBrg;
    private EditText HrgBrg;

    private Button btnUpdateBrg;
    private Button btnHapusBrg;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilkan_barang);

        Intent intent = getIntent();
        id = intent.getStringExtra(Konfigurasi.KEY_BRG_ID);

        IDBrg = findViewById(R.id.txtIDBrg);
        NamaBrg = findViewById(R.id.txtNamaBrg);
        StokBrg = findViewById(R.id.txtStokBrg);
        HrgBrg = findViewById(R.id.txtHrgBrg);

        btnUpdateBrg = findViewById(R.id.btnUpdateBrg);
        btnHapusBrg = findViewById(R.id.btnHapusBrg);

        btnUpdateBrg.setOnClickListener(this);
        btnHapusBrg.setOnClickListener(this);

        IDBrg.setText(id);

        getBarang();
    }

    private void getBarang(){
        @SuppressLint("StaticFieldLeak")
        class getBarang extends AsyncTask<Void,Void,String>{
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilkanBarang.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showBarang(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                ReqHandler rh = new ReqHandler();
                return rh.sendGetRequestParam(Konfigurasi.URL_GET,id);
            }
        }
        getBarang ge = new getBarang();
        ge.execute();
    }

    private void showBarang(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(Konfigurasi.TAG_BRG_NAMA);
            String stok = c.getString(Konfigurasi.TAG_BRG_STOK);
            String harga = c.getString(Konfigurasi.TAG_BRG_HRG);

            NamaBrg.setText(nama);
            StokBrg.setText(stok);
            HrgBrg.setText(harga);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateBarang(){
        final String nama = NamaBrg.getText().toString().trim();
        final String stok = StokBrg.getText().toString().trim();
        final String harga = HrgBrg.getText().toString().trim();

        @SuppressLint("StaticFieldLeak")
        class updateBarang extends AsyncTask<Void,Void,String>{
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilkanBarang.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilkanBarang.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_BRG_ID,id);
                hashMap.put(Konfigurasi.KEY_BRG_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_BRG_STOK,stok);
                hashMap.put(Konfigurasi.KEY_BRG_HRG, harga);

                ReqHandler rh = new ReqHandler();

                return rh.sendPostRequest(Konfigurasi.URL_UPDATE,hashMap);
            }
        }

        updateBarang ue = new updateBarang();
        ue.execute();
    }

    private void hapusBarang(){
        @SuppressLint("StaticFieldLeak")
        class hapusBarang extends AsyncTask<Void,Void,String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilkanBarang.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilkanBarang.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                ReqHandler rh = new ReqHandler();
                return rh.sendGetRequestParam(Konfigurasi.URL_DELETE, id);
            }
        }

        hapusBarang de = new hapusBarang();
        de.execute();
    }

    private void konfirmDelBarang(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Barang ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        hapusBarang();
                        startActivity(new Intent(TampilkanBarang.this, TampilkanSemuaBarang.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnUpdateBrg) {
            updateBarang();
        }

        if (view == btnHapusBrg) {
            konfirmDelBarang();
        }
    }
}
