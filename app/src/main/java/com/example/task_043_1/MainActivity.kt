package com.example.task_043_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.task_043_1.databinding.ActivityMainBinding
import kotlin.system.exitProcess
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var customAdapter: CustomAdapter? = null
    private var contactModelList: MutableList<ContactModel>? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
            PackageManager.PERMISSION_GRANTED)
        {
            permissionContact.launch(Manifest.permission.READ_CONTACTS)
            customAdapter?.notifyDataSetChanged()
        } else {
            getContacts()
        }

        setSupportActionBar(binding.toolBarMain)
        title = "Мои контакты."
    }

    @SuppressLint("Range")
    private fun getContacts() {
        contactModelList = ArrayList()
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel(name, phoneNumber)
            contactModelList?.add(contactModel)
        }
        phones.close()

        val thisContext = this

        binding.recyclerViewRV.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(contactModelList!!)

        adapter.setOnContactPhoneClickListener( object :
            CustomAdapter.OnContactPhoneClickListener {
            override fun onContactPhoneClick(contact: ContactModel, position: Int) {
                val contact = (contactModelList as ArrayList<ContactModel>)[position]
                val number = contact.phone

                if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    permissionOfCall.launch(Manifest.permission.CALL_PHONE)
                } else {
                    callTheNumber(number)
                }
            }
        })

        adapter.setOnContactSmsClickListener( object :
            CustomAdapter.OnContactSmsClickListener {
            override fun onContactSmsClick(contact: ContactModel, position: Int) {
                val contact = (contactModelList as ArrayList<ContactModel>)[position]

                if (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.SEND_SMS) !=
                    PackageManager.PERMISSION_GRANTED)
                {
                    permissionOfSMS.launch(Manifest.permission.SEND_SMS)
                } else {
                    val intent = Intent(this@MainActivity, SmsActivity::class.java)
                    intent.putExtra("contact", contact)
                    startActivity(intent)
                }

            }
        })

        binding.recyclerViewRV.adapter = adapter
        binding.recyclerViewRV.setHasFixedSize(true)
    }

    private fun callTheNumber(number: String?) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private val permissionContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this@MainActivity, "Получен доступ к контактам.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "В разрешении отказано.", Toast.LENGTH_SHORT).show()
        }
    }

    private val permissionOfCall = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
            isGranted ->
        if (isGranted) {
            Toast.makeText(this@MainActivity, "Получен доступ к звонкам.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "В разрешении отказано.", Toast.LENGTH_SHORT).show()
        }
    }

    private val permissionOfSMS = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
            isGranted ->
        if (isGranted) {
            Toast.makeText(this@MainActivity, "Получен доступ к SMS.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "В разрешении отказано.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}