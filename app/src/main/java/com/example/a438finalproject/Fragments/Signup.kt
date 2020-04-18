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
import kotlinx.android.synthetic.main.fragment_signup.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Signup.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Signup.newInstance] factory method to
 * create an instance of this fragment.
 */
class Signup : Fragment() {

    private lateinit var firebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = FirebaseAuth.getInstance()
        signup_button.setOnClickListener{
            if(signup_name.text.toString() != "" && signup_email.text.toString() != "" && signup_password.text.toString() != ""){
                val email = signup_email.text.toString()
                val password = signup_password.text.toString()
                val name = signup_name.text.toString()
                firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    Activity(), OnCompleteListener{ task ->
                        if(task.isSuccessful){
                            firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                                Activity(), OnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        var user = firebase.currentUser
                                        val data:HashMap<String, Any> = hashMapOf(
                                            "wins" to 0,
                                            "losses" to 0,
                                            "chips" to 500,
                                            "name" to name
                                        )
                                        //db.collection("users").document(user!!.uid).set(data)

                                        Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG).show()
                                        val playIntent = Intent(activity, MapsActivity::class.java)
                                        startActivity(playIntent)
                                    }else {
                                        Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()
                                    }
                                })
                        }else {
                            Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    })
            }
            else{
                Toast.makeText(context, "Please enter a valid input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
