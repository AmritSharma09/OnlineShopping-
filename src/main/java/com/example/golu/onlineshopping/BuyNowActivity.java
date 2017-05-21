package com.example.golu.onlineshopping;

/**
 * Created by GOLU on 11-02-2017.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class BuyNowActivity extends AppCompatActivity {
    EditText et_cardNo,et_cvvCode,et_expdate,et_amount,et_emailid;
    Button btn_submit;
    ProgressDialog dg;
    JSONParser jsonparser=new JSONParser();
    String str_cardNo="",str_cvvCode="",str_expdate="",str_amount="",str_emailid="",url="",succ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_now);
        dg=new ProgressDialog(this);
        dg.setTitle("Please Wait..........");
        dg.setCancelable(false);
        et_cardNo=(EditText)findViewById(R.id.et_cardNo);
        et_cvvCode=(EditText)findViewById(R.id.et_cvvCode);
        et_expdate=(EditText)findViewById(R.id.et_expdate);
        et_amount=(EditText)findViewById(R.id.et_amount);
        et_emailid=(EditText)findViewById(R.id.et_emailid);
        btn_submit=(Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url="http://220.225.80.177/apptransaction/WebService.asmx/Transaction";
                str_cardNo=et_cardNo.getText().toString();
                str_cvvCode=et_cvvCode.getText().toString();
                str_expdate=et_expdate.getText().toString();
                str_amount=et_amount.getText().toString();
                str_emailid=et_emailid.getText().toString();

                if(str_cardNo.equals("")||str_cvvCode.equals("")||str_expdate.equals("")||str_amount.equals("")||str_emailid.equals("")){
                    Toast.makeText(BuyNowActivity.this, "Please Fillup All Fields", Toast.LENGTH_SHORT).show();
                }else{
                    new loaddata().execute();
                }
            }
        });
    }
    public class loaddata extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            dg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(succ.equals("1")){
            dg.cancel();
            Toast.makeText(BuyNowActivity.this, ""+succ, Toast.LENGTH_SHORT).show();
          /*  }else{
                dg.cancel();
                Toast.makeText(BuyNowActivity.this, ""+succ, Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap=new HashMap<String, String>();
            hashMap.put("cardNo",str_cardNo);
            hashMap.put("cvvCode",str_cvvCode);
            hashMap.put("expdate",str_expdate);
            hashMap.put("amount", str_amount);
            hashMap.put("emailid",str_emailid);


            //JSONObject jobj=jsonparser.getJsonFromURL(url);

            try {
                JSONObject jsonObject = jsonparser.getJsonFromURL1(url, "POST", hashMap);

                JSONObject resObject=jsonObject.getJSONObject("Response");
                succ=resObject.getString("Messagetext");
                Log.d("SUCCESS:@@@@@@@@@@", succ);

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
