package com.example.finalassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdaptor extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<BooksData> booksData;

    public BookAdaptor(Context context, int layout, ArrayList<BooksData> booksData) {
        this.context = context;
        this.layout = layout;
        this.booksData = booksData;
    }

    @Override
    public int getCount() {
        return booksData.size();
    }

    @Override
    public Object getItem(int position) {
        return booksData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //ViewHolder
    private class ViewHolder{

        // initializing/typecasting all our variables.
        ImageView book_image;
        TextView book_isbn,book_name,book_price;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.book_isbn = (TextView) row.findViewById(R.id.bookisbn);
            holder.book_name = (TextView) row.findViewById(R.id.bookname);
            holder.book_price = (TextView) row.findViewById(R.id.bookprice);
            holder.book_image = (ImageView) row.findViewById(R.id.bookimage);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        BooksData booksData1 = booksData.get(position);


        holder.book_isbn.setText(booksData1.getBook_isbn());
        holder.book_name.setText(booksData1.getBook_name());
        holder.book_price.setText(booksData1.getBook_price());

        byte[] book_image = booksData1.getBook_image();
        Bitmap bitmap = BitmapFactory.decodeByteArray(book_image,0,book_image.length);
        holder.book_image.setImageBitmap(bitmap);

        return row;
    }
}
