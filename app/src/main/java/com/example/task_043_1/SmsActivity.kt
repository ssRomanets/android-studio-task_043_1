package com.example.task_043_1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.task_043_1.databinding.ActivitySmsBinding
import kotlin.system.exitProcess
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast

class SmsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBarSms)
        title = "Мои контакты."

        var contact: ContactModel? = null
        if (intent.hasExtra("contact")) {
            contact = intent.getSerializableExtra("contact") as ContactModel
        }
        if (contact != null) {
            binding.nameDataTV.text = contact.name
            binding.phoneDataTV.text = contact.phone
        }

         binding.sendBTN.setOnClickListener{
            try {
                val smsManager:SmsManager
                if (Build.VERSION.SDK_INT>=23)
                {
                    smsManager = this.getSystemService(SmsManager::class.java)
                }
                else
                {
                    smsManager = SmsManager.getDefault()
                }
                smsManager.sendTextMessage(contact?.phone, null, binding.messageET.text.toString(), null, null)
                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
                binding.messageET.setText("")
            }
            catch (e: Exception)
            {
                Toast.makeText(applicationContext, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sms_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toMainActivity->{
                val intent = Intent(this@SmsActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}