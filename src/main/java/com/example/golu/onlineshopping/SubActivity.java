package com.example.golu.onlineshopping;

/**
 * Created by GOLU on 07-02-2017.
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
import android.widget.AdapterView.OnItemClickListener;
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

public class SubActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<Category> arrlist=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lv=(ListView)findViewById(R.id.lv);


        String data = getIntent().getExtras().getString("id"); //get Id from its parent
        int id=Integer.parseInt(data);
        new Myjson(id).execute("");

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(SubActivity.this, arrlist.get(position).getCat_Id()+"", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(SubActivity.this,ProductActivity.class);
                i.putExtra("id", arrlist.get(position).getCat_Id());
                startActivity(i);

            }
        });


    }
    public class Myjson extends AsyncTask<String,integer,String>{
        ProgressDialog progressdialog=new ProgressDialog(SubActivity.this);

        int id;

        public Myjson(int id) {
            super();
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            arrlist=new ArrayList<Category>();
            JSONParser jparser=new JSONParser();
            JSONObject jobj=jparser.getJsonFromURL("http://220.225.80.177/onlineshoppingapp/show.asmx/GetCatSubcatDetails");

            try {
                JSONArray jarr=jobj.getJSONArray("Category");
                JSONObject n_jobj=jarr.getJSONObject(id-1);
                JSONArray jarr_sub=n_jobj.getJSONArray("SubCategory");
                for(int i=0;i<jarr_sub.length();i++){
                    JSONObject jobj1=jarr_sub.getJSONObject(i);
                    Category obj=new Category();
                    String id=jobj1.getString("Cat_Id");
                    obj.setCat_Id(jobj1.getString("Cat_Id"));
                    obj.setCat_Name(jobj1.getString("Cat_Name"));
                    obj.setCat_Image(jobj1.getString("Cat_Image"));

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
                convertView=layinf.inflate(R.layout.child,parent,false);
                TextView cid=(TextView)convertView.findViewById(R.id.cid);
                TextView cname=(TextView)convertView.findViewById(R.id.cname);
                ImageView image=(ImageView)convertView.findViewById(R.id.icon);
                Category obj=new Category();
                obj=arrlist.get(position);
                cid.setText(obj.getCat_Id());
                cname.setText(obj.getCat_Name());


                Picasso.with(SubActivity.this)
                        .load(obj.getCat_Image())
                        .into(image);
                return convertView;
            }

        }



    }
}

