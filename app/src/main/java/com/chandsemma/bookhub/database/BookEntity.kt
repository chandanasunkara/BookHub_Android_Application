package com.chandsemma.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="books") //This is called ANNOTATION. It is used to tell the compiler what we are creating. So this annotation will tell the compiler that it is an Entities class.
data class BookEntity(
    @PrimaryKey val book_id: Int,  //Adding this annotation before the name makes the variable PrimaryKey
    @ColumnInfo(name="book_name")val bookName: String,
    @ColumnInfo(name = "book_author") val bookAuthor: String,
    @ColumnInfo(name="book_price")val bookPrice: String,
    @ColumnInfo(name="book_rating") val bookRating: String,
    @ColumnInfo(name="book_desc") val bookDesc: String,
    @ColumnInfo(name="book_image") val bookImage: String)
