package com.example.golu.onlineshopping;

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

public class CategoryActivity extends AppCompatActivity {
    ListView lv;   //Declare list
    ArrayList<Category>arrlist=null;   //Initialize ArrayList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);



        lv=(ListView)findViewById(R.id.lv); //Initialize List View to show main page

        new Myjson().execute(""); //call MyJson Method

        lv.setOnItemClickListener(new OnItemClickListener() {   //after clicking on listView Item go to next Activity according to
                                                                //Its Id
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(CategoryActivity.this, arrlist.get(position).getCat_Id()+"", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(CategoryActivity.this,SubActivity.class);//go to it's SubActivity
                i.putExtra("id", arrlist.get(position).getCat_Id());   //here it's take id according to it's listView Item and pass in it's SubActiviy
                startActivity(i);

            }
        });

    }
    public class Myjson extends AsyncTask<String,integer,String>{  //Define MyJson here by extending Asynctask for getting data from JSON Link
        ProgressDialog progressdialog=new ProgressDialog(CategoryActivity.this);

        @Override
        protected String doInBackground(String... params) {



            arrlist=new ArrayList<Category>();
            JSONParser jparser=new JSONParser();
            JSONObject jobj=jparser.getJsonFromURL("http://220.225.80.177/onlineshoppingapp/show.asmx/GetCatSubcatDetails"); //here we take Json link
                                                                                                      //where all data reside which is in Json Object
            try {

                JSONArray jarr=jobj.getJSONArray("Category");  //here data save in Json Array which value is Category
                for(int i=0;i<jarr.length();i++){   //Here we access all data for setting in ListView Item repeatatly

                    JSONObject jobj1=jarr.getJSONObject(i);
                    Category obj=new Category();

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


        public class Myadp extends BaseAdapter{  // here we define our adapter for setting data in listView Item

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


                Picasso.with(CategoryActivity.this) //here we used Picasso to see clear picture in listView Item ImageView
                        .load(obj.getCat_Image())
                        .into(image);
                return convertView;
            }

        }



    }
}