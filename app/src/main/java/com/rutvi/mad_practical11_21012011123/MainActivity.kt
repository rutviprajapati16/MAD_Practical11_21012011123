package com.rutvi.mad_practical11_21012011123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var personRecyclerView : RecyclerView
    lateinit var db:DatabaseHelper
    val personList = ArrayList<Person>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val btn= findViewById<FloatingActionButton>(R.id.autorenew_btn)
        personRecyclerView = findViewById(R.id.listview_main)
        personRecyclerView.adapter = PersonAdapter(this,personList)
        db = DatabaseHelper(this)
        getPersonDetailsFromSqliteDb()
        btn.setOnClickListener(){
            JsonDb()
        }
    }
    fun JsonDb(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    "https://api.json-generator.com/templates/qjeKFdjkXCdK/data",
                    "rbn0rerl1k0d3mcwgw7dva2xuwk780z1hxvyvrb1")
                withContext(Dispatchers.Main) {
                    try {
                        if(data != null)
                            runOnUiThread{getPersonDetailsFromJson(data)}
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.windows_btn->{
                getPersonDetailsFromSqliteDb()
                true}
            R.id.renew->{
                JsonDb()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPersonDetailsFromJson(sJson: String?) {
        val personList = ArrayList<Person>()
        val size = personList.size
        personList.clear()
        personRecyclerView.adapter?.notifyItemRangeRemoved(0,size)
        try {
            val jsonArray = JSONArray(sJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                personList.add(person)
                try {
                    if (db.getPerson(person.id) != null)
                        db.updatePerson(person)
                    else
                        db.insertPerson(person)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            personRecyclerView.adapter?.notifyItemRangeInserted(0,personList.size)

        } catch (ee: JSONException) {
            ee.printStackTrace()
        }
        Toast.makeText(this, "Fetched details from JSON", Toast.LENGTH_SHORT).show()
    }
    private fun getPersonDetailsFromSqliteDb(){
        val size = personList.size
        personList.clear()
        personRecyclerView.adapter?.notifyItemRangeRemoved(0,size)
        try {
            personList.addAll(db.allPersons)
            personRecyclerView.adapter?.notifyItemRangeInserted(0,personList.size)
        }catch (ee: JSONException) {
            ee.printStackTrace()
        }
        Toast.makeText(this, "Fetched details from Sqlite database", Toast.LENGTH_SHORT).show()
    }


}