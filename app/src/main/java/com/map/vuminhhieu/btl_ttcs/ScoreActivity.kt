package com.map.vuminhhieu.btl_ttcs
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ScoreActivity.kt

class ScoreActivity : ComponentActivity() {
    private lateinit var authApi: AuthApi
    private lateinit var currentGpaTextView: TextView
    private lateinit var predictedGpaTextView: TextView
    private lateinit var semesterRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Initialize views
        currentGpaTextView = findViewById(R.id.currentGpaTextView)
        predictedGpaTextView = findViewById(R.id.predictedGpaTextView)
        semesterRecyclerView = findViewById(R.id.semesterRecyclerView)

        // Get the token from Intent
        val token = intent.getStringExtra("token") ?: ""

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Replace with actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create API instance
        authApi = retrofit.create(AuthApi::class.java)

        // Fetch and display GPA details
        fetchGpaDetails(token)
    }

    private fun fetchGpaDetails(token: String) {
        authApi.getGpaDetail("Bearer $token").enqueue(object : Callback<GpaResponse> {
            override fun onResponse(call: Call<GpaResponse>, response: Response<GpaResponse>) {
                if (response.isSuccessful) {
                    val gpaResponse = response.body()
                    gpaResponse?.let {
                        // Display GPA details
                        currentGpaTextView.text = "Current GPA: ${it.current_gpa}"
                        predictedGpaTextView.text = "Predicted GPA: ${it.predicted_next_semester_gpa}"

                        // Initialize the SemesterAdapter with the semesters data
                        val semesterAdapter = SemesterAdapter(it.semesters)
                        semesterRecyclerView.layoutManager = LinearLayoutManager(this@ScoreActivity)
                        semesterRecyclerView.adapter = semesterAdapter
                    }
                } else {
                    Toast.makeText(this@ScoreActivity, "Failed to retrieve GPA details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GpaResponse>, t: Throwable) {
                Toast.makeText(this@ScoreActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





