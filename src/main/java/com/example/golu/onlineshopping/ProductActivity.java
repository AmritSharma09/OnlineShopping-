package com.example.golu.onlineshopping;

/**
 * Created by GOLU on 11-02-2017.
 */

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Product> arrlist=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lv=(ListView)findViewById(R.id.lv);


        String id = getIntent().getExtras().getString("id");

        new Myjson(id).execute("");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(ProductActivity.this, arrlist.get(position).getProduct_Id()+"", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(ProductActivity.this,detailsActivity.class);
                i.putExtra("pid", arrlist.get(position).getProduct_Id());  //prepare product id for next Activity
                i.putExtra("cid", arrlist.get(position).getCategory_id()); //similarly we prepare all data for next Activity
                i.putExtra("item_name", arrlist.get(position).getItem_Name());
                i.putExtra("item_desc", arrlist.get(position).getItem_Desc());
                i.putExtra("mp", arrlist.get(position).getMarket_Price());
                i.putExtra("wp", arrlist.get(position).getWeb_Price());
                i.putExtra("av", arrlist.get(position).getAvailability());
                i.putExtra("pi", arrlist.get(position).getProduct_Image());

                startActivity(i);

            }
        });
    }

    public class Myjson extends AsyncTask<String,integer,String>{
        ProgressDialog progressdialog=new ProgressDialog(ProductActivity.this);

        String id;

        public Myjson(String id) {
            super();
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            arrlist=new ArrayList<Product>();
            JSONParser jparser=new JSONParser();
            HashMap<String, String> hashmap=new HashMap<String, String>();
            hashmap.put("catId",id); //Id will override

            String url="http://220.225.80.177/onlineshoppingapp/show.asmx/GetProduct";
            JSONObject jobj=jparser.getJsonFromURL1(url,"POST",hashmap);

            try {
                JSONArray jarr=jobj.getJSONArray("Products");
                for(int i=0;i<jarr.length();i++){
                    JSONObject jobj1=jarr.getJSONObject(i);
                    Product obj=new Product();

                    obj.setProduct_Id(jobj1.getString("Product_Id"));
                    obj.setCategory_id(jobj1.getString("Category_Id"));
                    obj.setItem_Name(jobj1.getString("Item_Name"));
                    obj.setItem_Desc(jobj1.getString("Item_Desc"));
                    obj.setMarket_Price(jobj1.getString("Market_Price"));
                    obj.setWeb_Price(jobj1.getString("Web_Price"));
                    obj.setAvailability(jobj1.getString("Availability"));
                    obj.setProduct_Image(jobj1.getString("Product_Image"));

                    arrlist.add(obj);


                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog.show();
            progressdialog.setMessage("");
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressdialog.dismiss();
            lv.setAdapter(new Myadp());
        }


        public class Myadp extends BaseAdapter{

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return  arrlist.size();
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layinf=getLayoutInflater();
                convertView=layinf.inflate(R.layout.child3,parent,false);
                TextView pid=(TextView)convertView.findViewById(R.id.pid);
                TextView cid=(TextView)convertView.findViewById(R.id.cid);
                TextView iname=(TextView)convertView.findViewById(R.id.iname);
                TextView idesc=(TextView)convertView.findViewById(R.id.item_desc);
                TextView mp=(TextView)convertView.findViewById(R.id.mp);
                TextView wp=(TextView)convertView.findViewById(R.id.wp);
                TextView av=(TextView)convertView.findViewById(R.id.av);
                ImageView image=(ImageView)convertView.findViewById(R.id.icon);


                Product  obj=arrlist.get(position);
                pid.setText(obj.getProduct_Id());
                cid.setText(obj.getCategory_id());
                iname.setText(obj.getItem_Name());
                idesc.setText(obj.Item_Desc);
                mp.setText(obj.getMarket_Price());
                wp.setText(obj.getWeb_Price());
                av.setText(obj.getAvailability());



                Picasso.with(ProductActivity.this)
                        .load(obj.getProduct_Image())
                        .into(image);
                return convertView;
            }

        }

    }

}
