package com.map.vuminhhieu.btl_ttcs

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Define data classes for request and response
data class LoginRequest(
    val student_id: String,
    val password: String
)
data class GpaResponse(
    val current_gpa: Double,
    val predicted_next_semester_gpa: Double,
    val semesters: List<Semester>
)

data class Semester(
    val semester_number: String,
    val academic_year: String,
    val gpa: Double,
    val total_number_of_credits: Int,
    val course_results: List<CourseResult>
)

data class CourseResult(
    val name: String,
    val number_of_credits: Int,
    val gpa_4_scale: Double,
    val gpa_10_scale: Double,
    val component_score: List<ComponentScore>?
)

data class ComponentScore(
    val name: String,
    val score_weight: Double,
    val score: Int
)



data class LoginResponse(val token: String)  // Adjust fields as per API response

// Define the API interface
interface AuthApi {
    @POST("/auth/login") // replace with your actual endpoint if different
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/gpa/detail")
    fun getGpaDetail(@Header("Authorization") token: String): Call<GpaResponse>


}

