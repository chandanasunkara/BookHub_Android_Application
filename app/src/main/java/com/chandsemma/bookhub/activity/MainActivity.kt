package com.chandsemma.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
//import android.widget.Toolbar
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.chandsemma.bookhub.*
import com.google.android.material.navigation.NavigationView
import com.chandsemma.bookhub.fragment.AboutAppFragment
import com.chandsemma.bookhub.fragment.DashboardFragment
import com.chandsemma.bookhub.fragment.FavouritesFragment
import com.chandsemma.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()
    /* Insted of writing this code twice, we can use the openDashboard method
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, DashboardFragment())
            .addToBackStack("Dashboard")
            .commit()
        supportActionBar?.title="Dashboard"
    */  openDashboard()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )//the last 2 parameters are action strings, which will tell the 2 states the toggle will give to the navigation drawer

        drawerLayout.addDrawerListener(actionBarDrawerToggle) //this line makes the hamburger icon function
        actionBarDrawerToggle.syncState() //synchronize state of Toggle with state of Navigation Drawer. //It changes hamburger icon to back arrow(<-) icon and vice-versa

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
        }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){    // it means currently selected item
                R.id.dashboard ->{
                //    Toast.makeText(this@MainActivity,"Clicked on Dashboard",Toast.LENGTH_SHORT).show()
            /*    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,DashboardFragment())
                        .addToBackStack("Dashboard")   //whenever a fragment is opened, this line add the fragment to BackStack
                        .commit()   //transaction is comitted
                    supportActionBar?.title="Dashboard"
                    drawerLayout.closeDrawers()  //once the above transaction is comitted, the drawer must close
            */    openDashboard()
                }
                R.id.favourites ->{
                 //   Toast.makeText(this@MainActivity,"Clicked on Favourites",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        )
             //           .addToBackStack("Favourites")
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
            //        Toast.makeText(this@MainActivity,"Clicked on Profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )

                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp ->{
         //           Toast.makeText(this@MainActivity,"Clicked on About App ",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutAppFragment()
                        )

                        .commit()
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Home button of action bar(here Hamburger icon) is also a menu item
        val id=item.itemId //Extracting id of the item and storing it in the variable "id"
        if(id==android.R.id.home){ //checking if id extracted is equal to id of Hamburger icon (id.home is used to get id of Hamburger icon)
            drawerLayout.openDrawer(GravityCompat.START) //open drawer from the side where the screen starts (left side of the app)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        //Instead of using methods, we are using variables here
        val fragment= DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        //when we open app, dashboard opens but in navigation drawer it is not highlighted. For this
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}