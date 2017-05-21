package com.example.golu.onlineshopping;

/**
 * Created by GOLU on 11-02-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCartActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView TextName;
    private TextView TextPrice;
    private TextView TextId;
    private EditText TextQuantity;

    private Button btnPrev;
    private Button btnNext;
    private Button btnDelete;

    private static final String SELECT_SQL = "SELECT * FROM product";

    private SQLiteDatabase db;

    private Cursor c;
    private double total_price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartview);
        openDatabase();

        TextId = (TextView) findViewById(R.id.editTextId);
        TextName = (TextView) findViewById(R.id.editTextName);
        TextQuantity = (EditText) findViewById(R.id.et_quantity);
        TextPrice = (TextView) findViewById(R.id.editTextPrice);


        // Capture Text in EditText
        TextQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                Toast.makeText(getApplicationContext(), "after", Toast.LENGTH_LONG).show();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                Toast.makeText(getApplicationContext(), "before", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

                Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_LONG).show();
            }

        });

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();

        showRecords();

    }

    protected void openDatabase() {
        db = openOrCreateDatabase("ProductDB", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String id = c.getString(1);
        String name = c.getString(2);
        int quantity = c.getInt(3);
        double price = c.getDouble(4);

        TextId.setText(id);
        TextName.setText(name);
        TextQuantity.setText(String.valueOf(quantity));
        TextPrice.setText(String.valueOf(price));
    }

    protected void moveNext() {
        if (!c.isLast())
            c.moveToNext();

        showRecords();
    }

    protected void movePrev() {
        if (!c.isFirst())
            c.moveToPrevious();

        showRecords();

    }

    private void deleteRecord() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want delete this person?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String id = TextId.getText().toString().trim();

                        String sql = "DELETE FROM product WHERE product_id=" + id + "";
                        db.execSQL(sql);
                        Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                        c = db.rawQuery(SELECT_SQL, null);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        moveNext();

    }

    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }

        if (v == btnPrev) {
            movePrev();
        }

        if (v == btnDelete) {
            deleteRecord();
        }




    }

    protected void UpdateRecord(String id, int quantity, double total) {


        String sql = "UPDATE product SET quantity='" + quantity + "', price='" + total + "' WHERE product_id=" + id + ";";


        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records update Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToPosition(Integer.parseInt(id));
    }




}