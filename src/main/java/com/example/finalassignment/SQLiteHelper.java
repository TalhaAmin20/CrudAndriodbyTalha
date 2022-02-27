package com.example.finalassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    //COnstructor for SQLITE
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //Insert
    public void insertData(String book_isbn, String book_name, String book_price, byte[] book_image){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO BOOKS VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        sqLiteStatement.clearBindings();

        sqLiteStatement.bindString(1, book_isbn);
        sqLiteStatement.bindString(2, book_name);
        sqLiteStatement.bindString(3,book_price);
        sqLiteStatement.bindBlob(4,book_image);

        sqLiteStatement.executeInsert();
    }

    //Read
    public Cursor getData(String sql){
        SQLiteDatabase db = getReadableDatabase();
        Cursor resData =  db.rawQuery(sql, null);
        return resData;
    }


    //Update
    public void updateData(String book_isbn, String book_name, String book_price, byte[] book_image, int book_id)
     {
        SQLiteDatabase db= getWritableDatabase();

        String sql = "UPDATE BOOKS SET book_isbn = ?, book_name = ?, book_price = ?, book_image = ? WHERE book_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, book_isbn);
        statement.bindString(2, book_name);
        statement.bindString(3, book_price);
        statement.bindBlob(4, book_image);
        statement.bindDouble(0, (double) book_id);

        statement.execute();
        db.close();
    }

    //Update by SCENERio
    public boolean updateTableData(String book_isbn, String book_name, String book_price){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("BOOK ISBN",book_isbn);
        cValues.put("BOOK NAME",book_name);
        cValues.put("BOOK PRICE",book_price);

    Cursor cursor = sqlDB.rawQuery("SELECT * FROM BOOKS where book_name=?", new String[]{book_name});
        if (cursor.getCount() > 0) {
        int res = sqlDB.update("FOOD", cValues, "book_name=?", new String[]{book_name});
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    } else {
        return false;
    }
}
    //Delete by SCENERIO
    public void deleteTableData(String book_isbn){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String sql = "DELETE FROM BOOKS WHERE book_isbn = ?";
        SQLiteStatement statement = sqlDB.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,book_isbn);

        statement.execute();
        sqlDB.close();
    }


    //Delete
    public  void deleteData(int book_id) {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM BOOKS WHERE book_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, book_id);

        statement.execute();
        db.close();
    }


    //For Creating Table
    @Override
    public void onCreate(SQLiteDatabase sqLDB) {

    }

    //FOR Changing in Table
    @Override
    public void onUpgrade(SQLiteDatabase sqLDB, int i, int i1) {

    }

    public void queryData(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }



}
