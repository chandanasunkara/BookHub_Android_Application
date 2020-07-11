package com.chandsemma.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chandsemma.bookhub.model.Book

@Database(entities = [BookEntity::class],version =1) //to tell the compiler that this class, which is of RoomDatabase type would be used as our DB for this application
//the keyword class is used to tell that BookEntity is a class
abstract class BookDatabase: RoomDatabase() {

    //this function below is used, to tell that all the functions that we perform on the data will be performed by the Dao interface
    abstract fun bookDao(): BookDao //it returns a Dao value and in turn allow us to use all the functionalities of a Dao
}