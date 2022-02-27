package com.example.finalassignment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {

    GridView gridView;
    ArrayList<BooksData> list;
    BookAdaptor bookAdaptor = null;

    //RequestCode for Gallery
    private static final int REQUEST_CODE_GALLERY= 888;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);


        // initializing/typecasting all our variables.

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        bookAdaptor = new BookAdaptor(this, R.layout.activity_bookitems, list);
        gridView.setAdapter(bookAdaptor);


        //Getting Data from SQLite Database
        Cursor cursor = MainActivity.sqLiteHelper.getData("Select * FROM BOOKS");
        list.clear();
        while (cursor.moveToNext()) {
            int book_id = cursor.getInt(0);
            String book_isbn = cursor.getString(1);
            String book_name = cursor.getString(2);
            String book_price = cursor.getString(3);
            byte[] book_image = cursor.getBlob(4);

            list.add(new BooksData(book_id, book_isbn, book_name, book_price, book_image));
        }
        bookAdaptor.notifyDataSetChanged();

        //ClickListener ON GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long book_id) {

                CharSequence[] items = {"Update Data", "Delete Data"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(BookDetails.this);

                dialog.setTitle("Choose an Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT book_id FROM BOOKS");
                            ArrayList<Integer> bookID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                bookID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(BookDetails.this, bookID.get(position));

                        } else {
                            // delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT book_id FROM BOOKS");
                            ArrayList<Integer> bookID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                bookID.add(c.getInt(0));
                            }
                            showDialogDelete(bookID.get(position));
                        }
                    }
                });
                dialog.show();

            }
        });
    }


            //updateitems work
            ImageView updateimage;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_updatebookitems);
        dialog.setTitle("Update Data");

        updateimage = (ImageView) dialog.findViewById(R.id.bookimage_update);
        final EditText updateisbn = (EditText) dialog.findViewById(R.id.bookisbn_update);
        final EditText updatename = (EditText) dialog.findViewById(R.id.bookname_update);
        final EditText updateprice = (EditText) dialog.findViewById(R.id.bookprice_update);
        Button btnUpdate = (Button) dialog.findViewById(R.id.updatebtn);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        updateimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        BookDetails.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // below line is to get data from all edit text fields.
                String BOOK_ISBN = updateisbn.getText().toString();
                String BOOK_NAME = updatename.getText().toString();
                String BOOK_PRICE = updateprice.getText().toString();


                // validating if the text fields are empty or not.
                if (TextUtils.isEmpty(BOOK_ISBN)) {
                    Toast.makeText(BookDetails.this, "PLEASE ENTER BOOK ISBN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_NAME)) {
                    Toast.makeText(BookDetails.this, "PLEASE ENTER BOOK NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(BOOK_PRICE)) {
                    Toast.makeText(BookDetails.this, "PLEASE ENTER BOOK PRICE", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    MainActivity.sqLiteHelper.updateData(
                            updateisbn.getText().toString().trim(),
                            updatename.getText().toString().trim(),
                            updateprice.getText().toString().trim(),
                            MainActivity.imageViewToByte(updateimage),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateData();

            }
        });
    }

    private void showDialogDelete(final int book_id){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(BookDetails.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqLiteHelper.deleteData(book_id);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateData();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }


    //Update Data
    private void updateData(){
        // get all data from sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM BOOKS");
        list.clear();
        while (cursor.moveToNext()) {
            int book_id = cursor.getInt(0);
            String book_isbn = cursor.getString(1);
            String book_name = cursor.getString(2);
            String book_price = cursor.getString(3);
            byte[] book_image = cursor.getBlob(4);

            list.add(new BooksData(book_id, book_isbn, book_name, book_price, book_image));
        }
        bookAdaptor.notifyDataSetChanged();
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
                updateimage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}