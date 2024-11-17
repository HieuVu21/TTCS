package com.map.vuminhhieu.btl_ttcs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScoreActivity : ComponentActivity() {
    private lateinit var authApi: AuthApi
    private lateinit var currentGpaTextView: TextView
    private lateinit var predictedGpaTextView: TextView
    private lateinit var selectedSemesterGpaTextView: TextView
    private lateinit var semesterSpinner: Spinner
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var viewChartButton: Button
    private var semesters: List<Semester> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Initialize views
        currentGpaTextView = findViewById(R.id.currentGpaTextView)
        predictedGpaTextView = findViewById(R.id.predictedGpaTextView)
        selectedSemesterGpaTextView = findViewById(R.id.selectedSemesterGpaTextView)
        semesterSpinner = findViewById(R.id.semesterSpinner)
        courseRecyclerView = findViewById(R.id.courseRecyclerView)
        viewChartButton = findViewById(R.id.viewChartButton)

        // Setup RecyclerView
        courseRecyclerView.layoutManager = LinearLayoutManager(this)

        // Handle "View GPA Chart" button click
        viewChartButton.setOnClickListener {
            if (semesters.isNotEmpty()) {
                val semesterGpas = semesters.map { it.gpa.toFloat() }.toFloatArray()
                val intent = Intent(this, GpaChartActivity::class.java).apply {
                    putExtra("semester_gpas", semesterGpas)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "No GPA data available to display", Toast.LENGTH_SHORT).show()
            }
        }

        // Get the token from Intent
        val token = intent.getStringExtra("token") ?: ""

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        fetchGpaDetails(token)
    }

    private fun setupSpinner(semesters: List<Semester>) {
        // Create spinner items
        val spinnerItems = semesters.map {
            "Semester ${it.semester_number} (${it.academic_year})"
        }

        // Create and set adapter for spinner
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerItems
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        semesterSpinner.adapter = spinnerAdapter

        // Handle spinner item selection
        semesterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSemester = semesters[position]
                updateSemesterDetails(selectedSemester)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional: Handle case when nothing is selected
            }
        }
    }

    private fun updateSemesterDetails(semester: Semester) {
        // Update semester GPA
        selectedSemesterGpaTextView.text = "Semester GPA: ${semester.gpa}"

        // Update course list
        val courseAdapter = CourseResultAdapter(semester.course_results)
        courseRecyclerView.adapter = courseAdapter
    }

    private fun fetchGpaDetails(token: String) {
        authApi.getGpaDetail("Bearer $token").enqueue(object : Callback<GpaResponse> {
            override fun onResponse(call: Call<GpaResponse>, response: Response<GpaResponse>) {
                if (response.isSuccessful) {
                    val gpaResponse = response.body()
                    gpaResponse?.let {
                        // Display overall GPA details
                        currentGpaTextView.text = "Current GPA: ${it.current_gpa}"
                        predictedGpaTextView.text = "Predicted GPA: ${it.predicted_next_semester_gpa}"

                        // Store semesters and setup spinner
                        semesters = it.semesters
                        setupSpinner(semesters)

                        // Select first semester by default
                        if (semesters.isNotEmpty()) {
                            updateSemesterDetails(semesters[0])
                        }
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
