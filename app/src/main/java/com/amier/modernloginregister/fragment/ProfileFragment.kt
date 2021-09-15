package com.amier.modernloginregister.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.amier.modernloginregister.LoginActivity
import com.amier.modernloginregister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var fullname : TextView
    lateinit var phone : TextView
    lateinit var address : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")

        name = view.findViewById(R.id.firstT)
        email = view.findViewById(R.id.emailT)
        fullname= view.findViewById(R.id.userT)
        phone= view.findViewById(R.id.phoneT)
        address = view.findViewById(R.id.addressT)

        loadProfile()
    }
    private fun loadProfile() {

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        email.text = user?.email

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                fullname.text = snapshot.child("Name").value.toString()
                name.text = snapshot.child("Name").value.toString()
                phone.text = snapshot.child("Phone").value.toString()
                address.text = snapshot.child("Address").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu?.add(0,12,0,"Logout")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            12->{
                auth.signOut()

                val i = Intent(activity, LoginActivity::class.java)
                startActivity(i)
                activity?.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}