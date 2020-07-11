package com.chandsemma.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.chandsemma.bookhub.R
import com.chandsemma.bookhub.database.BookDatabase
import com.chandsemma.bookhub.database.BookEntity
import com.chandsemma.bookhub.model.Book
import com.chandsemma.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFavourites: Button
    lateinit var progressBar:ProgressBar
    lateinit var progressLayout: RelativeLayout

    lateinit var toolbar: Toolbar

    var bookId: String?="100"  //random value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDescription)
        btnAddToFavourites = findViewById(R.id.btnAddToFavourites)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"//txtBookName.text

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {//if(bookId.equals("100")){
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)
        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                val success = it.getBoolean("success")
                try {
                    if (success) {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        val bookImageUrl=bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")
                        supportActionBar?.title=txtBookName.text

                        val bookEntity=BookEntity(
                        bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                        txtBookAuthor.text.toString(),
                        txtBookPrice.text.toString(),
                        txtBookRating.text.toString(),
                        txtBookDesc.text.toString(),
                        bookImageUrl
                        )

                        val checkFav=DBAsyncTask(applicationContext,bookEntity,1).execute() //execute method is used ot start the background process
                        val isFav=checkFav.get() //the get method tell weather the result of background process is true or false
                        // so the value in isFav variable will tell if the particular book is in Favourite or not.

                        //Now, if book is present in Favourites we should show Remove from favourites button
                        if(isFav) {
                            btnAddToFavourites.text="Remove from favourites"
                            val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                            btnAddToFavourites.setBackgroundColor(favColor)
                        }
                        //If book is not present in the favourites database, we should show Add to favourites button
                        else {
                            btnAddToFavourites.text="Add to favourites"
                            val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            btnAddToFavourites.setBackgroundColor(noFavColor)
                        }

                        btnAddToFavourites.setOnClickListener{
                            if( ! DBAsyncTask(applicationContext,bookEntity,1).execute().get() ) {
                                val async=DBAsyncTask(applicationContext,bookEntity,2).execute()//saves the book in the DB
                                val result=async.get()//gives the result of this method
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book added to favourites", Toast.LENGTH_SHORT).show()
                                    btnAddToFavourites.text="Remove from favourites"
                                    val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                    btnAddToFavourites.setBackgroundColor(favColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity,"Some error occurred", Toast.LENGTH_SHORT).show()
                                }
                            } else{
                                val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book removed from favourites", Toast.LENGTH_SHORT).show()
                                    btnAddToFavourites.text="Add to favourites"
                                    val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                    btnAddToFavourites.setBackgroundColor(noFavColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity,"Some error occurred", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    println("Response is $it")
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some unexpected error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(
                    this@DescriptionActivity,
                    "Volley error $it occurred!",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "1f0231dbd129ff"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else{
            val dialog= AlertDialog.Builder(this@DescriptionActivity) //using Builder sub-class of AlertDialog class
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text,listner ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS) //Settings class provides the mechanism to
                // open the settings of the phone
                //ACTION_WIRELESS_SETTINGS will have the command for the Android Operating System to open the Settings
                startActivity(settingsIntent)
                finish() //we close the activity here so that when we come back to the app after connecting to the
                // internet, it recreates the fragment and refreshes the List
            }
            dialog.setNegativeButton("Exit"){text,listner ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int): AsyncTask<Void,Void,Boolean>() {//since a java class cannot extend more than one class, we are creating a SUB CLASS here
        /*
        mode 1 -> Check database(DB) if the book is favourite or not
        mode-2 -> Save the book into DB as favourite
        mode-3 -> Remove the favourite book
         */

        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1->{
                    // Check DB if the book is favourite or not

                    //checking if id of the book is present in the database or not
                    val book: BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString()) //this will extract id of the book only if
                    //the book is present in the DB. If not present, it will give null
                    db.close() //It is mandatory to close database after we perform an operation, otherwise it takes up unnecessary memory
                    return book!=null
                }
                2->{
                    // Save the book into DB as favourite
                    //insert operation
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    // Remove the favourite book
                    //delete operation
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}