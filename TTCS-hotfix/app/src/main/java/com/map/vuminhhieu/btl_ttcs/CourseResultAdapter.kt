package com.map.vuminhhieu.btl_ttcs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseResultAdapter(private val courseResults: List<CourseResult>) :
    RecyclerView.Adapter<CourseResultAdapter.CourseResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_result, parent, false)
        return CourseResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseResultViewHolder, position: Int) {
        val courseResult = courseResults[position]
        holder.bind(courseResult)
    }

    override fun getItemCount(): Int = courseResults.size

    class CourseResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseNameTextView: TextView = itemView.findViewById(R.id.courseNameTextView)
        private val courseCreditsTextView: TextView = itemView.findViewById(R.id.courseCreditsTextView)
        private val courseGpa4TextView: TextView = itemView.findViewById(R.id.courseGpa4TextView)
        private val courseGpa10TextView: TextView = itemView.findViewById(R.id.courseGpa10TextView)
        private val componentsContainer: LinearLayout = itemView.findViewById(R.id.componentsContainer)

        fun bind(courseResult: CourseResult) {
            courseNameTextView.text = courseResult.name
            courseCreditsTextView.text = "Credits: ${courseResult.number_of_credits}"
            courseGpa4TextView.text = "GPA (4 Scale): ${courseResult.gpa_4_scale}"
            courseGpa10TextView.text = "GPA (10 Scale): ${courseResult.gpa_10_scale}"

            // Clear previous components to avoid duplication
            componentsContainer.removeAllViews()

            // Add each component score dynamically
            courseResult.component_score?.forEach { component ->
                val componentTextView = TextView(itemView.context).apply {
                    text = "${component.name}: Weight ${component.score_weight}, Score ${component.score}"
                    textSize = 14f
                    setPadding(0, 4, 0, 4)
                }
                componentsContainer.addView(componentTextView)
            }
        }
    }
}
