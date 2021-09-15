package com.amier.modernloginregister.fragment

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import butterknife.ButterKnife
import butterknife.Unbinder
import com.amier.modernloginregister.Adapter.MyViewPagerAdapter
import com.amier.modernloginregister.Common.Common
import com.amier.modernloginregister.Common.NonSwipeViewPager
import com.amier.modernloginregister.LoginActivity
import com.amier.modernloginregister.R
import com.amier.modernloginregister.model.Centers
import com.amier.modernloginregister.model.Courser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shuhart.stepview.StepView
import dmax.dialog.SpotsDialog
import java.util.ArrayList

class ExamFragment : Fragment() {

    //Copy_1_start
    var step_view: StepView? = null

    var view_pager: NonSwipeViewPager? = null

    var btn_previous_step: Button?= null

    var btn_next_step: Button? = null

    var unbinder: Unbinder? = null

    var localBroadcastManager: LocalBroadcastManager? = null
    var dialog: AlertDialog? = null
    var courseRef: CollectionReference? = null

    //val myContext: HomeFragment? = null
    val myContext: ExamFragment? = null
    //end
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    //copy_2_start
    private fun setupStepView() {
        val stepList: MutableList<String> = ArrayList()
        stepList.add("Select Academy")
        stepList.add("Select Course")
        stepList.add("Select Date")
        stepList.add("Book Exam")
        step_view!!.setSteps(stepList)
    }
    //end

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exam, container, false)
    }*/

    //Copy_4_start
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View =  inflater.inflate(R.layout.fragment_exam, container, false)
        unbinder = ButterKnife.bind(this,view)
        step_view = view!!.findViewById(R.id.step_view)
        view_pager = view!!.findViewById(R.id.view_pager)
        btn_previous_step = view!!.findViewById(R.id.btn_previous_step);
        btn_next_step = view!!.findViewById(R.id.btn_next_step);
        dialog = SpotsDialog.Builder().setContext(context).setCancelable(false).build()
        setupStepView()
        setColorButton()

        btn_previous_step!!.setOnClickListener{
            if (Common.step === 3 || Common.step > 0) {
                Common.step--
                view_pager!!.currentItem = Common.step
                if(Common.step < 3){
                    btn_next_step!!.isEnabled = true
                    setColorButton()
                }
            }
        }

        btn_next_step!!.setOnClickListener {  // Toast.makeText(this, ""+Common.currentCenter.getCenterId(), Toast.LENGTH_SHORT).show();
            if (Common.step < 3 || Common.step === 0) {
                Common.step++ //Increase
                if (Common.step === 1) //After Choose City
                {
                    if (Common.currentCenter != null) loadCenterbyCity(Common.currentCenter.centerId)
                } else if (Common.step === 2) //Pick the time Slot
                {
                    if (Common.currentCourse != null) loadTimeSlotofExam(Common.currentCourse.courseId)
                } else if (Common.step === 3) //Confirm
                {
                    if (Common.currentTimeSlot !== -1) confirmBooking()
                }
                view_pager!!.currentItem = Common.step
            } }

        localBroadcastManager = LocalBroadcastManager.getInstance(this.requireActivity())
        localBroadcastManager!!.registerReceiver(
            buttonNextReceiver,
            IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT)
        )

        //View

        //View
        //view_pager!!.setAdapter(MyViewPagerAdapter(getSupportFragmentManager()))
        view_pager!!.setAdapter(MyViewPagerAdapter(requireActivity()!!.supportFragmentManager))

        view_pager!!.setOffscreenPageLimit(4) //As we have four fragments otherview you will lost state of all View when you press previous

        view_pager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                //Show Steps
                step_view!!.go(position, true)
                if (position == 0) btn_previous_step!!.setEnabled(false) else btn_previous_step!!.setEnabled(
                    true
                )

                //Set disable button next her
                btn_next_step!!.setEnabled(false)
                setColorButton()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


        return view
    }
    //end_4

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    //***** OPTION MENU  *****//

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


    //Copy_3_start
    private fun setColorButton() {
        if (btn_next_step!!.isEnabled) {
            btn_next_step!!.setBackgroundResource(R.color.black)
        } else {
            btn_next_step!!.setBackgroundResource(R.color.grey)
        }

        if (btn_previous_step!!.isEnabled) {
            btn_previous_step!!.setBackgroundResource(R.color.black)
        } else {
            btn_previous_step!!.setBackgroundResource(R.color.grey)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }


    //BoardCast Receiver
    private val buttonNextReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val step = intent.getIntExtra(Common.KEY_STEP, 0)
            if (step == 1) {
                Common.currentCenter =
                    intent.getParcelableExtra<Parcelable>(Common.KEY_CENTER_STORE) as Centers?
            } else if (step == 2) {
                Common.currentCourse =
                    intent.getParcelableExtra<Parcelable>(Common.KEY_COURSE_SELECTED) as Courser?
            } else if (step == 3) {
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1)
            }
            btn_next_step!!.isEnabled = true
            setColorButton()
        }
    }

    override fun onDestroy() {
        localBroadcastManager!!.unregisterReceiver(buttonNextReceiver)
        super.onDestroy()
    }


    private fun confirmBooking() {
        //send Boarcast to Fragment STep four
        val intent = Intent(Common.KEY_CONFIRM_BOOKING)
        localBroadcastManager!!.sendBroadcast(intent)
    }

    private fun loadTimeSlotofExam(courseId: String) {
        //Send Local BoardCast to Fragment
        val intent = Intent(Common.KEY_DISPLAY_TIME_SLOT)
        localBroadcastManager!!.sendBroadcast(intent)
    }

    private fun loadCenterbyCity(centerId: String) {
        dialog!!.show()
        //   /AllCities/Pune/Branch/LECEPzzx7GUXzfkamEI5/Courses/91myv76ko8MMtXhZTx86
        if (!TextUtils.isEmpty(Common.city)) {
            courseRef = FirebaseFirestore.getInstance()
                .collection("AllCities")
                .document(Common.city)
                .collection("Branch")
                .document(centerId)
                .collection("Courses")
            courseRef!!.get()
                .addOnCompleteListener { task ->
                    val coursers = ArrayList<Courser>()
                    for (coursersSnapShot in task.result!!) {
                        val courser = coursersSnapShot.toObject(Courser::class.java)
                        //courser.setPassword(""); Remove password because in client app ^^
                        courser.courseId = coursersSnapShot.id //get ID of course
                        coursers.add(courser)
                    }
                    // Send Broadcast to Booking Step 2 Fragment to load Recycler
                    val intent = Intent(Common.KEY_COURSE_LOAD_DONE)
                    intent.putParcelableArrayListExtra(Common.KEY_COURSE_LOAD_DONE, coursers)
                    localBroadcastManager!!.sendBroadcast(intent)
                    dialog!!.dismiss()
                }
                .addOnFailureListener { dialog!!.dismiss() }
        }
    }

    //end_3

}