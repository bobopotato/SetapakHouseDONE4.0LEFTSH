package com.example.setapakhouse.Fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.setapakhouse.Adapter.HomeAdapter
import com.example.setapakhouse.Model.Notification
import com.example.setapakhouse.Model.Payment
import com.example.setapakhouse.Model.Property
import com.example.setapakhouse.Model.Rent
import com.example.setapakhouse.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class HomeFragment : Fragment() {
    lateinit var ref : DatabaseReference
    lateinit var propertyList : MutableList<Property>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)

        //autoChangePaymentStatus()

        propertyList = mutableListOf()

        addToList(root)




        return root
    }


//    private fun getTime(): String {
//
//        val today = LocalDateTime.now(ZoneId.systemDefault())
//
//        return today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//    }

    private fun getTime(): String {

        val today = LocalDateTime.now(ZoneId.systemDefault())

        return today.format(DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss "))
    }
//    private fun autoChangePaymentStatus() {
//        var countNotification=0
//        var countRent=0
//        var ref1:DatabaseReference
//        var ref2:DatabaseReference
//        var ref3:DatabaseReference
//        var ref4:DatabaseReference
//        var rentList:MutableList<Rent>
//        var paymentList:MutableList<Payment>
//        rentList= mutableListOf()
//        paymentList= mutableListOf()
//        ref1=FirebaseDatabase.getInstance().getReference("Rent")
//        ref1.addValueEventListener(object :ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    rentList.clear()
//                    if(countRent.toString().equals("0")){
//                    for (h in snapshot.children) {
//                        if (h.child("userID").getValue().toString().equals(currentUserID) &&
//                            h.child("status").getValue().toString().equals("continuing")) {
//                            val rent = h.getValue(Rent::class.java)
//                            rentList.add(rent!!)
//
//                        }
//                    }
//
//                    ref2 = FirebaseDatabase.getInstance().getReference("Payment")
//                    ref2.addValueEventListener(object : ValueEventListener {
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                paymentList.clear()
//                                if (countNotification.toString().equals("0")) {
//                                for (p in snapshot.children) {
//                                    for (r in rentList) {
//                                        if (p.child("rentID").getValue().toString().equals(r.rentID) &&
//                                            !(p.child("status").getValue().toString().equals("paid")) &&
//                                            !(p.child("status").getValue().toString().equals("expired"))) {
//
//                                            //Log.d("Check",p.child("paymentID").getValue().toString())
//                                            var splitTitle =
//                                                p.child("paymentTitle").getValue().toString()
//                                                    .split("/")
//                                            var paymentDate = splitTitle[0]
//                                            var paymentMonth = splitTitle[1]
//                                            var paymentYearSection = splitTitle[2].split(" ")
//                                            var paymentYear = paymentYearSection[0]
//                                            //today
//                                            val cal: Calendar = Calendar.getInstance()
//                                            val thisDay =
//                                                SimpleDateFormat(("d"), Locale.getDefault()).format(
//                                                    cal.time
//                                                )
//                                            val thisMonth = SimpleDateFormat(
//                                                ("MM"),
//                                                Locale.getDefault()
//                                            ).format(cal.time)
//                                            val thisYear = SimpleDateFormat(
//                                                ("yyyy"),
//                                                Locale.getDefault()
//                                            ).format(cal.time)
//                                            //val today = LocalDate.of(thisYear.toInt(), thisMonth.toInt(), thisDay.toInt())
//                                            //var firstt = LocalDate.of(2020, 11, 6)
//                                            var firstt = LocalDate.of(
//                                                paymentYear.toInt(),
//                                                paymentMonth.toInt(),
//                                                paymentDate.toInt()
//                                            )
//                                            var secondd = LocalDate.of(
//                                                thisYear.toInt(),
//                                                thisMonth.toInt(),
//                                                thisDay.toInt()
//                                            )
//
//
//                                            //dayCount >= -4&&<=0 then correct elseif <-4 change semua to cancel
//
//                                            var dayCount = ChronoUnit.DAYS.between(secondd, firstt)
//                                            Log.d("Check",dayCount.toString())
//                                            //Toast.makeText(context,secondd.toString(),Toast.LENGTH_SHORT).show()
//                                            if (dayCount >= -9 && dayCount <= 0) { //10 days limit, 6 to 15 is left 1 day. this is within 10 days
//                                                //change new to pending and add notification
//                                                var payment = p.getValue(Payment::class.java)
//                                                ref2.child(
//                                                    p.child("paymentID").getValue().toString()
//                                                ).child("status").setValue("pending")
//                                                //Toast.makeText(context,countNotification.toString().equals("0").toString(),Toast.LENGTH_SHORT).show()
//
//
//
//                                                    //if no warning
//                                                    if (p.child("warningNotificationID").getValue().toString().equals("")) {
//                                                        //change payment status from pending to pending, new to pending
//                                                        val leftday = 10 + dayCount
//                                                        val notificationContent =
//                                                            "You have " + leftday.toString() + "days left for this payment(" + p.child(
//                                                                "paymentTitle"
//                                                            ).getValue().toString() + ")"
//
//                                                        //add notification
//                                                        ref3 = FirebaseDatabase.getInstance()
//                                                            .getReference("Notification")
//                                                        var notificationID =
//                                                            ref3.push().key.toString()
//                                                        val storeNotification = Notification(
//                                                            notificationID,
//                                                            "system",
//                                                            "delivered",
//                                                            notificationContent,
//                                                            getTime(),
//                                                            "warning",
//                                                            currentUserID
//                                                        )
//                                                        ref3.child(notificationID)
//                                                            .setValue(storeNotification)
//                                                        ref2.child(p.child("paymentID").getValue().toString()).child("warningNotificationID").setValue(notificationID)
//                                                        //Toast.makeText(context,"Here is no notification",Toast.LENGTH_SHORT).show()
//                                                    } else {   //if alr got 1 or more warning
//
//                                                        var countInside = 0
//                                                        var combineWarning =p.child("warningNotificationID").getValue().toString()
//                                                        val splitWarning = combineWarning.split(",")
//                                                        val splitSize = splitWarning.size
//                                                        val lastWarning =splitWarning[splitSize - 1]
//                                                        //Toast.makeText(context,lastWarning,Toast.LENGTH_SHORT).show()
//
//                                                        ref4 = FirebaseDatabase.getInstance().getReference("Notification")
//                                                        ref4.addValueEventListener(object :ValueEventListener {
//                                                            override fun onCancelled(error: DatabaseError) {
//                                                                TODO("Not yet implemented")
//                                                            }
//
//                                                            override fun onDataChange(snapshot: DataSnapshot) {
//                                                                if (snapshot.exists()) {
//                                                                    if (countInside.toString().equals("0")) {
//                                                                        for (n in snapshot.children) {
//
//                                                                            if (n.child("notificationID").getValue().toString().equals(lastWarning)) {
//                                                                                var notificationDate =n.child("notificationDateTime").getValue().toString()
//                                                                                var splitNotificationDate =notificationDate.split(" ")
//                                                                                var notificationDays =splitNotificationDate[0]
//                                                                                var engNotificationMonth =splitNotificationDate[1]
//                                                                                var notificationYear =splitNotificationDate[2]
//                                                                                var notificationMonth ="1"
//                                                                                //convert eng month to number
//                                                                                if (engNotificationMonth.equals(
//                                                                                        "Jan"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "1"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Feb"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "2"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Mar"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "3"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Apr"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "4"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "May"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "5"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Jun"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "6"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Jul"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "7"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Aug"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "8"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Sep"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "9"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Oct"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "10"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Nov"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "11"
//                                                                                } else if (engNotificationMonth.equals(
//                                                                                        "Dec"
//                                                                                    )
//                                                                                ) {
//                                                                                    notificationMonth =
//                                                                                        "12"
//                                                                                }
//
//                                                                                //today
//                                                                                val cal: Calendar =Calendar.getInstance()
//                                                                                val thisDay =SimpleDateFormat(("d"),Locale.getDefault()).format(cal.time)
//                                                                                val thisMonth =SimpleDateFormat(("MM"),Locale.getDefault()).format(cal.time)
//                                                                                val thisYear =SimpleDateFormat(("yyyy"),Locale.getDefault()).format(cal.time)
//                                                                                //val today = LocalDate.of(thisYear.toInt(), thisMonth.toInt(), thisDay.toInt())
//                                                                                var firstt =LocalDate.of(notificationYear.toInt(),notificationMonth.toInt(),notificationDays.toInt())
//                                                                                //var firstt = LocalDate.of(paymentYear.toInt(), paymentMonth.toInt(), paymentDate.toInt())
//                                                                                var secondd =LocalDate.of(thisYear.toInt(),thisMonth.toInt(),thisDay.toInt())
//                                                                                var dayCount =ChronoUnit.DAYS.between(secondd,firstt)
//                                                                                //Toast.makeText(context,"Current:"+secondd.toString(),Toast.LENGTH_SHORT).show()
//
//
//                                                                                var splitTitle1=
//                                                                                    p.child("paymentTitle").getValue().toString()
//                                                                                        .split("/")
//                                                                                var paymentDate1 = splitTitle1[0]
//                                                                                var paymentMonth1 = splitTitle1[1]
//                                                                                var paymentYearSection1 = splitTitle1[2].split(" ")
//                                                                                var paymentYear1 = paymentYearSection1[0]
//                                                                                var third = LocalDate.of(
//                                                                                    paymentYear.toInt(),
//                                                                                    paymentMonth.toInt(),
//                                                                                    paymentDate.toInt()
//                                                                                )
//                                                                                //Toast.makeText(context,third.toString(),Toast.LENGTH_SHORT).show()
//                                                                                if (!(dayCount.toString().equals("0"))) {
//
//                                                                                    var dayCount1 =ChronoUnit.DAYS.between(secondd,third)
//                                                                                    val leftday = 10 + dayCount1
//                                                                                    val notificationContent ="You have " + leftday.toString() + "days left for this payment(" + p.child("paymentTitle").getValue().toString() + ")"
//
//                                                                                    //add notification
//                                                                                    ref3 =FirebaseDatabase.getInstance().getReference("Notification")
//                                                                                    var notificationID =ref3.push().key.toString()
//                                                                                    val storeNotification = Notification(
//                                                                                            notificationID,
//                                                                                            "system",
//                                                                                            "delivered",
//                                                                                            notificationContent,
//                                                                                            getTime(),
//                                                                                            "warning",
//                                                                                            currentUserID
//                                                                                        )
//                                                                                    ref3.child(notificationID).setValue(storeNotification)
//                                                                                    combineWarning += "," + notificationID.toString()
//                                                                                    ref2.child(p.child("paymentID").getValue().toString()).child("warningNotificationID").setValue(combineWarning)
//                                                                                }
//                                                                            }
//
//                                                                        }
//                                                                        countInside++
//                                                                    }
//                                                                }
//                                                            }
//
//                                                        })
//
//                                                        //Toast.makeText(context,"here is got",Toast.LENGTH_SHORT).show()
//                                                    }
//
//                                            } else if (dayCount < -9) { //exceed 10days
//                                                //change status = cancel(many thing and property status to available)
//                                                //send a notification tell user his rent get cancelled
//                                                var countProperty=0
//                                                var countPayment1=0
//                                                //Toast.makeText(context,"here is expired!",Toast.LENGTH_SHORT).show()
//                                                //ref2.child(p.child("paymentID").getValue().toString()).child("status").setValue("expired")
//                                                ref1.child(r.rentID).child("status").setValue("withdraw")
//                                                var ref6=FirebaseDatabase.getInstance().getReference("Payment")
//                                                ref6.addValueEventListener(object:ValueEventListener{
//                                                    override fun onCancelled(error: DatabaseError) {
//                                                        TODO("Not yet implemented")
//                                                    }
//
//                                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                                        if(snapshot.exists()){
//                                                            if(countPayment1.toString().equals("0")) {
//                                                                for (p1 in snapshot.children) {
//                                                                    if(p1.child("rentID").getValue().toString().equals(r.rentID)&&
//                                                                        !(p1.child("status").getValue().toString().equals("paid")) &&
//                                                                        !(p1.child("status").getValue().toString().equals("expired"))){
//                                                                        ref6.child(p1.child("paymentID").getValue().toString()).child("status").setValue("expired")
//
//                                                                    }
//                                                                }
//                                                                countPayment1++
//                                                            }
//                                                        }
//                                                    }
//
//                                                })
//                                                var ref5=FirebaseDatabase.getInstance().getReference("Property")
//                                                ref5.addValueEventListener(object:ValueEventListener{
//                                                    override fun onCancelled(error: DatabaseError) {
//                                                        TODO("Not yet implemented")
//                                                    }
//
//                                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                                        if(snapshot.exists()){
//                                                            if(countProperty.toString().equals("0")){
//                                                                for(pr in snapshot.children){
//                                                                    if(r.propertyID.equals(pr.child("propertyID").getValue().toString())&&
//                                                                        pr.child("status").getValue().toString().equals("renting")){
//                                                                        ref5.child(pr.child("propertyID").getValue().toString()).child("status").setValue("available")
//                                                                    }
//                                                                }
//                                                                countProperty++
//                                                            }
//                                                        }
//                                                    }
//
//                                                })
//
//
//                                            }
//                                        }
//                                    }
//                                }
//                                    countNotification++
//                                }
//                            } //up here
//                        }
//
//                    })
//                        countRent++
//                }
//                }
//            }
//
//        })
//
//    }


    private fun addToList(root:View){
        ref = FirebaseDatabase.getInstance().getReference("Property")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    propertyList.clear()
                    for (h in snapshot.children){
                        if(h.child("status").getValue().toString().equals("available")) {
                            val property = h.getValue(Property::class.java)
                            propertyList.add(property!!)
                            //Log.d(tag,propertyList.toString())
                        }

                    }

                    val mLayoutManager = LinearLayoutManager(context)
                    mLayoutManager.reverseLayout = true

                    root.recycler_view.layoutManager = mLayoutManager
                    root.recycler_view.scrollToPosition(propertyList.size-1)
                    root.recycler_view.adapter = HomeAdapter(propertyList)
                }
            }

        })

    }

}