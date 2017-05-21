package com.example.golu.onlineshopping;

/**
 * Created by GOLU on 11-02-2017.
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detailsActivity extends AppCompatActivity implements View.OnClickListener {
    Product obj;
    TextView pid,cid,iname,idesc,mp,wp,av;
    ImageView image;
    Button b1,b2;
    ArrayList<Product> arrlist=null;
    String pid1="",cid1="",item_name="", wp1;
    double price;

    private SQLiteDatabase db;  //declare SqliteDatabase which is light weight database
    private static final String SELECT_SQL = "SELECT * FROM product";
    private Cursor c;
    int flag=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        pid=(TextView)findViewById(R.id.pid);
        cid=(TextView)findViewById(R.id.cid);
        iname=(TextView)findViewById(R.id.iname);
        idesc=(TextView)findViewById(R.id.item_desc);
        mp=(TextView)findViewById(R.id.mp);
        wp=(TextView)findViewById(R.id.wp);
        av=(TextView)findViewById(R.id.av);
        image=(ImageView)findViewById(R.id.icon);

        pid1 = getIntent().getExtras().getString("pid");
        cid1 = getIntent().getExtras().getString("cid");
        item_name = getIntent().getExtras().getString("item_name");
        String item_desc = getIntent().getExtras().getString("item_desc");
        String mp1 = getIntent().getExtras().getString("mp");
        wp1 = getIntent().getExtras().getString("wp");
        String av1 = getIntent().getExtras().getString("av");
        String image1 = getIntent().getExtras().getString("pi");

        Toast.makeText(detailsActivity.this, pid1+" "+cid1+" "+item_name+" ", Toast.LENGTH_SHORT).show();

        pid.setText(pid1);
        cid.setText(cid1);
        iname.setText(item_name);
        idesc.setText(item_desc);
        mp.setText(mp1);
        wp.setText(wp1);
        av.setText(av1);


        Picasso.with(detailsActivity.this)
                .load(image1)
                //		.placeholder(R.drawable.ic_launcher)  // optional
                //	.error(R.drawable.ic_launcher)      // optional
                .resize(400, 400)                        // optional
                .into(image);
        createDatabase();
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == b1) {
            insertIntoDB();

        }
        if (v == b2) {
            Intent i=new Intent(detailsActivity.this,BuyNowActivity.class);
            startActivity(i);

        }

    }


    protected void createDatabase(){
        db=openOrCreateDatabase("ProductDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS product(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,product_id VARCHAR, item_name VARCHAR,quantity INT,price DOUBLE,total Double);");
    }

    protected void insertIntoDB(){
        c = db.rawQuery(SELECT_SQL,null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                String id = c.getString(1);
                if(id.equals(pid1)) {
                    flag = 1;
                    break;
                }
            } while (c.moveToNext());
        }


        if(flag==0) {
            price = Double.parseDouble(wp1);
            String query = "INSERT INTO product(product_id,item_name,quantity,price,total) VALUES('" + pid1 + "', '" + item_name + "',1,'" + price + "','" + price + "');";
            db.execSQL(query);
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
            Intent i=new Intent(detailsActivity.this,ViewCartActivity.class);

            startActivity(i);
        }else
        {
            Toast.makeText(getApplicationContext(), "already exist", Toast.LENGTH_LONG).show();
            Intent i=new Intent(detailsActivity.this,ViewCartActivity.class);

            startActivity(i);
        }
    }
}

