package ir.hm.studentmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import ir.hm.studentmanager.databinding.ActivityMainBinding
import ir.hm.studentmanager.net.ApiService
import ir.hm.studentmanager.recycler.StudendEvents
import ir.hm.studentmanager.recycler.Student
import ir.hm.studentmanager.recycler.StudentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://172.20.10.6:8080"

class MainActivity : AppCompatActivity(), StudendEvents {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)


        binding.fabAddStudent.setOnClickListener {

            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)

        }
    }

    private fun setDataToRecycler(data: List<Student>) {

        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)


    }

    override fun onItemClicked(student: Student, position: Int) {

        updateDataInServer(student, position)

    }

    override fun onItemLongClicked(student: Student, position: Int) {

        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Delete this item?"
        dialog.confirmText = "Confirm"
        dialog.cancelText = "Cancel"

        dialog.setCancelClickListener {
            dialog.dismiss()
        }

        dialog.setConfirmClickListener {
            deleteDataFromServer(student,position)
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun deleteDataFromServer(student: Student, position: Int) {

        apiService.deleteStudent(student.name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                myAdapter.removeItem(student, position)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        })
    }


    private fun updateDataInServer(student: Student, position: Int) {

        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("student", student)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        getDataFromApi()
    }

    private fun getDataFromApi() {

        apiService.getAllStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                val dataFromServer = response.body()!!
                setDataToRecycler(dataFromServer)
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Log.v("ApiTest", t.message!!)
            }

        })

    }
}