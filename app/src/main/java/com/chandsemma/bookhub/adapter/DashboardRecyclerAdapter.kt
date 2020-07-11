package com.chandsemma.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chandsemma.bookhub.R
import com.chandsemma.bookhub.activity.DescriptionActivity
import com.chandsemma.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textBookName: TextView=view.findViewById(R.id.txtBookName)
        val textAuthor: TextView=view.findViewById(R.id.txtBookAuthor)
        val textBookPrice: TextView=view.findViewById(R.id.txtBookPrice)
        val textBookRating: TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView=view.findViewById(R.id.imgBookImage)

        val llContent: LinearLayout=view.findViewById(R.id.llContent)//initialize the view holder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {  //holds the initial 10 ViewHolders
      //  TODO("Not yet implemented")
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {  //stores total number of items in the list
      //  TODO("Not yet implemented")
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {  //for recycling and reusing ViewHolders
    //to place items of the list in their positions
        val book=itemList[position]  //storing element(string) at index=0 of the list in a variable
        holder.textBookName.text=book.bookName  //putting the string in the text views of the list
        holder.textAuthor.text=book.bookAuthor
        holder.textBookPrice.text=book.bookPrice
        holder.textBookRating.text=book.bookRating
     //   holder.imgBookImage.setBackgroundColor(book.bookImage)
        //holder.imgBookImage.setImageResource(book.bookImage)
        //To obtain the image from the link we will use the Android Library, Picasso
        //Picasso library converts an image into its imageView
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage);

        holder.llContent.setOnClickListener{
     //     Toast.makeText(context, "Clicked on ${holder.textBookName.text}", Toast.LENGTH_SHORT).show()
            val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }
}