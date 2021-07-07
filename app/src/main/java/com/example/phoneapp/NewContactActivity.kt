package com.example.phoneapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NewContactActivity : AppCompatActivity() {
    private var nameEdt: EditText? = null
    private var phoneEdt: EditText? = null
    lateinit var addContactEdt: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contact)

        nameEdt = findViewById(R.id.idEdtName)
        phoneEdt = findViewById(R.id.idEdtPhoneNumber)
        addContactEdt = findViewById(R.id.idBtnAddContact)

        addContactEdt.setOnClickListener(View.OnClickListener {
            lateinit var name: String
            lateinit var phone:String

            if (TextUtils.isEmpty(name)  && TextUtils.isEmpty(phone)) {
                Toast.makeText(
                    this@NewContactActivity,
                    "Please enter the data in all fields. ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                addContact(name, phone)
            }
        })
    }


    private fun addContact(name: String, phone: String) {
        val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
        contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE
        contactIntent
            .putExtra(ContactsContract.Intents.Insert.NAME, name)
            .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
        startActivityForResult(contactIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show()
                val i = Intent(this@NewContactActivity, MainActivity::class.java)
                startActivity(i)
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    this, "Cancelled Added Contact",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
