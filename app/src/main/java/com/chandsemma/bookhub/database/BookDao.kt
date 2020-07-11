package com.chandsemma.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import com.chandsemma.bookhub.model.Book

@Dao
interface BookDao {//This interface will take care of all the functions that we need to perform on the database tables. Like: insert, delete, read, etc.
    //operations
    @Insert
    fun insertBook(bookEntity: BookEntity) //it will help add a book to the table

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity> //function to get all the books in the database (to display in the favourites fragment)
        //for this we need to write an sql query (It is written above it with the annotation)

    @Query("SELECT * FROM books WHERE book_id = :bookId") //this colon tells the compiler that the value for bookId will come from the function just below it
    fun getBookById(bookId: String): BookEntity//To check weather a particular book is added to favourites or not

    // all the functions above only have declarations and no function body, beacuse, all the operations are performed in the database class and are taken care of, by the Room Library
    //so we need not give implementations of these functions.
    //This is the reason for Dao to be an Interface and not a Class
}