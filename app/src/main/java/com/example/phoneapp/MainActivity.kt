package com.example.phoneapp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*


class MainActivity : AppCompatActivity() {
    private var contactModelArrayList: ArrayList<ContactModel>? = null
    private var contactRV: RecyclerView? = null
    private lateinit var contactAdapter: ContactModel
    private var loadingPB: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactModelArrayList = ArrayList<ContactModel>()
        contactRV = findViewById(R.id.RvContacts)
        val addNewContactFAB = findViewById<FloatingActionButton>(R.id.FbAdd)
        loadingPB = findViewById(R.id.Loading)

        prepareContactRV()

        requestPermissions()

        addNewContactFAB.setOnClickListener {
            val i = Intent(this@MainActivity, NewContactActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchViewItem = menu.findItem(R.id.app_bar_search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText.toLowerCase())
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filter(text: Unit) {
        val filteredlist: ArrayList<ContactModel> = ArrayList<ContactModel>()
        for (item in contactModelArrayList!!) {
            if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show()
        } else { contactAdapter!!.filterList(filteredlist)
        }
    }

    private fun prepareContactRV() {
        contactRV!!.layoutManager = LinearLayoutManager(this)
        this.contactAdapter.also { contactRV!!.adapter = null }
    }
    private fun requestPermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_CONTACTS,  // below is the list of permissions
                Manifest.permission.CALL_PHONE,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        contacts
                        Toast.makeText(
                            this@MainActivity,
                            "All the permissions are granted..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread().check()
    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)


        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
    val contacts: Unit
    get() {
        var contactId = ""
            var displayName = ""
        val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val hasPhoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    if (hasPhoneNumber > 0) {
                        contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        displayName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        )
                        if (phoneCursor!!.moveToNext()) {
                            val phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactModelArrayList!!.add(ContactModel(displayName, phoneNumber))
                        }
                        phoneCursor.close()
                    }
                }
            }
        cursor.close()
        loadingPB!!.visibility = View.GONE
            contactAdapter!!.notifyDataSetChanged()
        }
}

private fun Any.contains(toLowerCase: Any): Boolean {
    TODO("Not yet implemented")
}

private fun Any.toLowerCase() {


}

private fun ContactModel.notifyDataSetChanged() {
    TODO("Not yet implemented")
}

private fun ContactModel.getUserName(): Any {
    TODO("Not yet implemented")
}
