package com.chandsemma.bookhub.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chandsemma.bookhub.R

class ProfileFragment : Fragment() {

    lateinit var etYourName: TextView
    lateinit var etYourMail: TextView
    lateinit var etYourPhone:TextView
    lateinit var profileView: RelativeLayout
//*    var valStr: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        profileView=view.findViewById(R.id.profileView)
      //  profileView.visibility=View.GONE

        etYourName=view.findViewById(R.id.etYourName)
        etYourMail=view.findViewById(R.id.etYourMail)
        etYourPhone=view.findViewById(R.id.etYourPhone)

        return view
    }


}