package com.example.record;

/*
 * 作者信息实体
 */
public class AuthorInfo {
    private  String author;
    private  String year;

    public AuthorInfo() {

    }
    public AuthorInfo(String author, String year) {
        this.author = author;
        this.year = year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }
}
