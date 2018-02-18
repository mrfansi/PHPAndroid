package eazy_dev.phpandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TampilkanSemuaBarang extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView ListBrg;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilkan_semua_barang);

        ListBrg = findViewById(R.id.ListBrg);
        ListBrg.setOnItemClickListener(this);
        getJSON();
    }

    private void getJSON() {
        @SuppressLint("StaticFieldLeak")
        class getJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilkanSemuaBarang.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showBarang();
            }

            @Override
            protected String doInBackground(Void... voids) {
                ReqHandler rh = new ReqHandler();
                return rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
            }
        }

        getJSON gj = new getJSON();
        gj.execute();
    }

    private void showBarang() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String brg_id = jo.getString(Konfigurasi.TAG_BRG_ID);
                String brg_name = jo.getString(Konfigurasi.TAG_BRG_NAMA);

                HashMap<String,String> Brgs = new HashMap<>();
                Brgs.put(Konfigurasi.TAG_BRG_ID, brg_id);
                Brgs.put(Konfigurasi.TAG_BRG_NAMA, brg_name);
                list.add(Brgs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                TampilkanSemuaBarang.this, list, R.layout.list_item,
                new String[]{Konfigurasi.TAG_BRG_ID, Konfigurasi.TAG_BRG_NAMA},
                new int[]{R.id.id, R.id.name});

        ListBrg.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, TampilkanBarang.class);
        HashMap map =(HashMap) adapterView.getItemAtPosition(i);
        String empId = map.get(Konfigurasi.TAG_BRG_ID).toString();
        intent.putExtra(Konfigurasi.TAG_BRG_ID,empId);
        startActivity(intent);
    }
}
