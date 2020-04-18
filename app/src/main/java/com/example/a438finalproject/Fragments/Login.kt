package com.example.a438finalproject.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.a438finalproject.Activities.MapsActivity
import com.example.a438finalproject.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Login.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {

    private lateinit var firebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = FirebaseAuth.getInstance()

        if(firebase.currentUser!=null){
            val playIntent = Intent(activity, MapsActivity::class.java)
            startActivity(playIntent)
        }

        login_button.setOnClickListener{
            if(login_email.text.toString() != "" && login_password.text.toString() != ""){
                println("email input: " + login_email.text)
                println("password input: " + login_password.text)
                val email = login_email.text.toString()
                val password = login_password.text.toString()
                firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(Activity(), OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val playIntent = Intent(activity, MapsActivity::class.java)
                        startActivity(playIntent)
                    }else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }

            else{
                Toast.makeText(context, "Please enter a valid input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
