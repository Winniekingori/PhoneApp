package com.example.phoneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class ContactDetailActivity : AppCompatActivity() {
    private var contactName: String? = null
    private var contactNumber: String? = null
    lateinit var contactTV: TextView
    lateinit var nameTV: TextView
    private var contactIV: ImageView? = null
    lateinit var callIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        contactName = intent.getStringExtra("name")
        contactNumber = intent.getStringExtra("contact")

        nameTV = findViewById(R.id.idTVName)
        contactIV = findViewById(R.id.idIVContact)
        contactTV = findViewById(R.id.idTVPhone)
        nameTV.setText(contactName)
        contactTV.setText(contactNumber)
        callIV = findViewById(R.id.idIVCall)

        callIV.setOnClickListener(View.OnClickListener {
            makeCall(contactNumber)
        })

    }


    private fun makeCall(contactNumber: String?) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$contactNumber")
        if (ActivityCompat.checkSelfPermission(
                this@ContactDetailActivity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        startActivity(callIntent)
    }
}
