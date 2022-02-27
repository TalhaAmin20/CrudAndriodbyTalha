package com.example.finalassignment;

public class BooksData {
    private int book_id;
    private String book_isbn;
    private String book_name;
    private String book_price;
    private byte[] book_image;


    //Constructor For BooksData
    public BooksData(int book_id, String book_isbn, String book_name, String book_price, byte[] book_image) {
        this.book_id = book_id;
        this.book_isbn = book_isbn;
        this.book_name = book_name;
        this.book_price = book_price;
        this.book_image = book_image;
    }

    //Getter & Setter Method
    public int getBook_id() { return book_id; }
    public void setBook_id(int book_id) { this.book_id = book_id; }


    public String getBook_isbn() { return book_isbn; }
    public void setBook_isbn(String book_isbn) { this.book_isbn = book_isbn; }


    public String getBook_name() { return book_name; }
    public void setBook_name(String book_name) { this.book_name = book_name; }


    public String getBook_price() { return book_price; }
    public void setBook_price(String book_price) { this.book_price = book_price; }


    public byte[] getBook_image() { return book_image; }
    public void setBook_image(byte[] book_image) { this.book_image = book_image; }
}
