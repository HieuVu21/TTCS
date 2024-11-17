package com.map.vuminhhieu.btl_ttcs
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var authApi: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Use 10.0.2.2 for the emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        // Create API instance
        authApi = retrofit.create(AuthApi::class.java)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
    }

    private fun setupClickListeners() {
        // Handle login button click
        findViewById<TextView>(R.id.loginBtn)?.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val studentId = usernameEditText.text.toString().trim() // Get student ID from the input
        val password = passwordEditText.text.toString().trim()

        if (studentId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a LoginRequest object with the correct fields
        val loginRequest = LoginRequest(student_id = studentId, password = password)

        authApi.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()


                        val token = loginResponse?.token
                        Toast.makeText(this@MainActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        Log.d("LoginSuccess", "Login successful with token: $token")

                    val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                    intent.putExtra("token", token) // Pass the token if needed
                    startActivity(intent)

                } else {
                    Log.e("LoginError", "Response code: ${response.code()}")
                    Log.e("LoginError", "Error body: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Có lỗi xảy ra: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("NetworkError", "Login API call failed: ${t.message}", t)
            }
        })
    }

}
