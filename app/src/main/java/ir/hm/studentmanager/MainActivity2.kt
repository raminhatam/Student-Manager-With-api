package ir.hm.studentmanager

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import ir.hm.studentmanager.databinding.ActivityMain2Binding
import ir.hm.studentmanager.net.ApiService
import ir.hm.studentmanager.recycler.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var apiService: ApiService
    var isInserting = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.edtFirstName.requestFocus()

        val testMode = intent.getParcelableArrayExtra("student")
        if ( testMode == null ){
            isInserting = true
        }else{
            isInserting = false

            binding.fabDone.text = "update"
            val dataFromIntent : Student = intent.getParcelableExtra("student")!!
            binding.edtCourse.setText(dataFromIntent.course)
            binding.edtScore.setText(dataFromIntent.score.toString())

            val splettedName = dataFromIntent.name.split(" ")
            binding.edtFirstName.setText(splettedName[0])
            binding.edtLastName.setText(splettedName.size -1)

        }


        binding.fabDone.setOnClickListener {
            if (isInserting){
                insertNewStudent()
            }else{
                updateStudent()
            }
        }
    }

    private fun updateStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            val jsonObject = JsonObject()
            jsonObject.addProperty("name", firstName + " " + lastName)
            jsonObject.addProperty("course", course)
            jsonObject.addProperty("score", score.toInt())

            apiService
                .updateStudent(firstName + " " + lastName, jsonObject)
                .enqueue(object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                    }
                })
            Toast.makeText(this@MainActivity2, "update success", Toast.LENGTH_SHORT).show()
            onBackPressed()

        }else{
            Toast.makeText(this@MainActivity2, "Fill the blank!", Toast.LENGTH_SHORT).show()
        }


    }

    private fun insertNewStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            val jsonObject = JsonObject()
            jsonObject.addProperty("name", firstName + " " + lastName)
            jsonObject.addProperty("course", course)
            jsonObject.addProperty("score", score.toInt())

            apiService.insertStudent(jsonObject).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    Toast.makeText(
                        this@MainActivity2,
                        "Student Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.v("apiInsertLog", t.message!!)
                }

            })
            Toast.makeText(this@MainActivity2, "added success", Toast.LENGTH_SHORT).show()
            onBackPressed()


        } else {
            Toast.makeText(this@MainActivity2, "Fill the blank!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}