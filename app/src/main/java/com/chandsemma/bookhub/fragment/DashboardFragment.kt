 package com.chandsemma.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.chandsemma.bookhub.R
import com.chandsemma.bookhub.adapter.DashboardRecyclerAdapter
import com.chandsemma.bookhub.model.Book
import com.chandsemma.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

 /*
  // TODO: Rename parameter arguments, choose names that match
 // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
 private const val ARG_PARAM1 = "param1"
 private const val ARG_PARAM2 = "param2"
 */
/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
//  lateinit var btnCheckInternet: Button

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

 /*   val bookList=arrayListOf("The Story Of My Life","Three Days To See","Harry Potter", "P.S. I Love You", "Madame Bovary",
        "War and Peace", "Pride and Prejudice", "Pamela", "The Adventures of Huckleberry Finn", "The Lord of Rings")
 */
    val bookInfoList=arrayListOf<Book>(
 /*       Book("The Story Of My Life", "Helen Keller", "Rs. 399", "4.9", R.drawable.the_story_of_my_life),
        Book("Three Days To See", "Helen Keller", "Rs. 399", "4.5", R.drawable.three_days_to_see),
        Book("Harry Potter", "J.K.Rowling", "Rs. 299", "4.5", R.drawable.harry_potter),
        Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Pride And Prejudice", "Jane Austin", "Rs. 349", "4.5", R.drawable.pride_and_prejudice),
        Book("Pamela", "Samuel Richardson", "Rs. 599", "4.0", R.drawable.pamela),
        Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
*/   )

    var ratingComparator= Comparator<Book>{book1, book2->
        if( book1.bookRating.compareTo(book2.bookRating,true) == 0){
            // if ratings are same then sort alphabetically
            book2.bookName.compareTo(book1.bookName,true)
            // here we re doing reverse becoz at the end we are reversing the list
        }
        else{ //else sort based on rating
            book1.bookRating.compareTo(book2.bookRating,true)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true) //to tell the compiler that this fragment has an options menu
        //This is needed only for a fragment, for an activity, the compiler automatically adds the menu option
        recyclerDashboard=view.findViewById(R.id.recycleDashboard)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE  //To make the progressLayout visible when the fragment is being loaded

/*        btnCheckInternet=view.findViewById(R.id.btnCheckInternet)
        btnCheckInternet.setOnClickListener{
            if(ConnectionManager().checkConnectivity(activity as Context)){//checkConnectivity() is method in ConnectionManager class
                //Internet is Available
                val dialog=AlertDialog.Builder(activity as Context) //using Builder sub-class of AlertDialog class
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("OK"){text,listner ->
                    //Do nothing
                }
                dialog.setNegativeButton("Cancel"){text,listner ->
                    //Do nothing
                }
                dialog.create()
                dialog.show()
            }
            else{
                //Internet is NOT Available
                val dialog=AlertDialog.Builder(activity as Context) //using Builder sub-class of AlertDialog class
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("OK"){text,listner ->
                    //Do nothing
                }
                dialog.setNegativeButton("Cancel"){text,listner ->
                    //Do nothing
                }
                dialog.create()
                dialog.show()
            }
        }
*/
        layoutManager=LinearLayoutManager(activity)
/*        recyclerAdapter= DashboardRecyclerAdapter(activity as Context, bookInfoList)

        recyclerDashboard.adapter=recyclerAdapter
    // "recyclerDashboard.adapter" = initialize adapter and
    // "recyclerAdapter" = attach it to respective files
        recyclerDashboard.layoutManager=layoutManager
    // "recyclerDashboard.layoutManager" = initialize layoutManager and
    // "layoutManager" = attach it to respective files
        recyclerDashboard.addItemDecoration(
            DividerItemDecoration(
                recyclerDashboard.context,
                (layoutManager as LinearLayoutManager).orientation))
*/
        val queue= Volley.newRequestQueue(activity as Context)  //a variable to store queue of requests
        val url="http://13.235.250.119/v1/book/fetch_books/"  //this url gives the response
        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //Here we will handle Responses
                    try {
                        progressLayout.visibility=View.GONE  //To hide the progressLayout when we get the Output
                        val successStatus = it.getBoolean("success")
                        if (successStatus) {
                            //extract data from the JSON array
                            val data = it.getJSONArray("data")
                            //iterate thru JSON Array and retreive each JSON object from it
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                //parsing each Json object into a Book object
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)

                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                           /*     recyclerDashboard.addItemDecoration(    //for the Dark Divider Line ( _______ )
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )   */
                            }
                        } else {
                            if(activity!=null) {
                                Toast.makeText(
                                    activity as Context,
                                    "Some Error occurred!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        println("Response is $it")  // "it" is the variable in which the response will be stored in a string form
                    }
                    catch(e: JSONException) {
                        if (activity != null) {
                            Toast.makeText(activity as Context,"Some unexpected error occurred!",Toast.LENGTH_SHORT).show()
                    }
                    }
                }, Response.ErrorListener {
                    //Here we will handle Errors
                    println("Error is $it")
                    if(activity!=null) {
                        Toast.makeText(activity as Context, "Volley error occurred!",Toast.LENGTH_SHORT).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] ="application/json"  //data sent to and received from API ate of type JSON
                        headers["token"] = "1f0231dbd129ff"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
        else{
            //Internet is NOT Available
            val dialog=AlertDialog.Builder(activity as Context) //using Builder sub-class of AlertDialog class
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text,listner ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS) //Settings class provides the mechanism to
                // open the settings of the phone
                //ACTION_WIRELESS_SETTINGS will have the command for the Android Operating System to open the Settings
                startActivity(settingsIntent)
                activity?.finish() //we close the activity here so that when we come back to the app after connecting to the
                // internet, it recreates the fragment and refreshes the List
            }
            dialog.setNegativeButton("Exit"){text,listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view //parent view of the fragment is being returned
    }

/*    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { //this method is used to add menu items to the toolbar
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()//To notify adapter of the changes
        //without this statement, the above sorting will not reflect on clicking the menu option

        return super.onOptionsItemSelected(item)
    }
}