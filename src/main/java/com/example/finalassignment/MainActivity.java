package com.example.finalassignment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    //Defining sqlDB
    public static SQLiteHelper sqLiteHelper;

    // Defining Buttons
    private Button chooseimagebtn,insert,showrecords,delete,update,showupdaterecord;

    //Defining EditText
    private EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7,editText8;

    //Defining ImageView;
    private ImageView showimage;

    //RequestCode for Gallery
    private static final int REQUEST_CODE_GALLERY= 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FOr TypeCasting MEthod
        typecasting();

        //SQL QUERY
        sqLiteHelper = new SQLiteHelper(this, "MYDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS BOOKS(book_id INTEGER PRIMARY KEY AUTOINCREMENT, book_isbn VARCHAR, book_name VARCHAR, book_price VARCHAR, book_image BLOB)");


        // Set Click Listeners for CHOOSE IMAGE button
        chooseimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });


        // Set Click Listeners for INSERT button
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // below line is to get data from all edit text fields.
                String BOOK_ISBN = editText1.getText().toString();
                String BOOK_NAME = editText2.getText().toString();
                String BOOK_PRICE = editText3.getText().toString();


                // validating if the text fields are empty or not.
                if (TextUtils.isEmpty(BOOK_ISBN)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_NAME)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_PRICE)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK PRICE", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    sqLiteHelper.insertData(
                            editText1.getText().toString().trim(),
                            editText2.getText().toString().trim(),
                            editText3.getText().toString().trim(),
                            imageViewToByte(showimage)

                    );
                    Toast.makeText(getApplicationContext(), "Data Inserted Successfully!", Toast.LENGTH_SHORT).show();
                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    showimage.setImageResource(R.drawable.ic_imgback);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();

                }
            }
        });


        // Set Click Listeners for SHOW ALL RECORDS button
        showrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, BookDetails.class);
                startActivity(intent);

            }
        });


        //delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String book_isbn = editText4.getText().toString();
                    MainActivity.sqLiteHelper.deleteTableData(book_isbn);
                    editText4.setText("");
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    Toast.makeText(MainActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //update
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // below line is to get data from all edit text fields.
                String BOOK_ISBN = editText6.getText().toString();
                String BOOK_NAME = editText7.getText().toString();
                String BOOK_PRICE = editText8.getText().toString();


                // validating if the text fields are empty or not.
                if (TextUtils.isEmpty(BOOK_ISBN)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_NAME)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_PRICE)) {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER BOOK PRICE", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    sqLiteHelper.updateTableData(
                            editText6.getText().toString().trim(),
                            editText7.getText().toString().trim(),
                            editText8.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(), "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                    editText6.setText("");
                    editText7.setText("");
                    editText8.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //SHOW UPDATED RECORD
        showupdaterecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookDetails.class);
                startActivity(intent);
            }
        });

    }
    // Function to check and request permission.
    //Permission function starts from here
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
                Toast.makeText(getApplicationContext(), "All Permissions Granted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                showimage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    //ONCLICK METHOD
    public void typecasting(){
        // initializing/typecasting all our variables.

        //buttons
        chooseimagebtn = findViewById(R.id.chooseimagebtn);
        insert = findViewById(R.id.insert);
        showrecords = findViewById(R.id.showallrecord);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        showupdaterecord = findViewById(R.id.showupdaterecord);

        //EditText
        editText1 = findViewById(R.id.ed_1);
        editText2 = findViewById(R.id.ed_2);
        editText3 = findViewById(R.id.ed_3);
        editText4 = findViewById(R.id.ed_4);
        editText5 = findViewById(R.id.ed_5);
        editText6 = findViewById(R.id.ed_6);
        editText7 = findViewById(R.id.ed_7);
        editText8 = findViewById(R.id.ed_8);

        //ImageView
        showimage = findViewById(R.id.showimg);
    };


    //CONVERT THE IMAGE INTO BYTE ARRAY METHOD
    public static byte[] imageViewToByte(ImageView showimage) {
        Bitmap bitmap = ((BitmapDrawable) showimage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



}


