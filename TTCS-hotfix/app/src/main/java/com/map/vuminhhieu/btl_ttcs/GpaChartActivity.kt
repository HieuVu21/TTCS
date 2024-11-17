package com.map.vuminhhieu.btl_ttcs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class GpaChartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpa_chart)

        val semesterGpas = intent.getFloatArrayExtra("semester_gpas")?.toList() ?: listOf()
        Log.d("GpaChartActivity", "Received Semester GPAs: $semesterGpas")

        val lineChart = findViewById<LineChart>(R.id.lineChart)
        displayGpaChart(lineChart, semesterGpas)
    }

    private fun displayGpaChart(lineChart: LineChart, semesterGpas: List<Float>) {
        if (semesterGpas.isEmpty()) {
            Log.e("GpaChartActivity", "No GPA data available to display.")
            return
        }

        val entries = ArrayList<Entry>()
        semesterGpas.forEachIndexed { index, gpa ->
            entries.add(Entry(index.toFloat(), gpa))
        }

        val lineDataSet = LineDataSet(entries, "GPA Over Semesters").apply {
            color = resources.getColor(R.color.red, theme)
            valueTextColor = resources.getColor(R.color.black, theme)
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 5f
            setCircleColor(resources.getColor(R.color.red, theme))
            setDrawFilled(true)
            fillColor = resources.getColor(R.color.red, theme)
            fillAlpha = 50
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
            valueFormatter = XAxisFormatter()
        }

        lineChart.axisLeft.apply {
            axisMinimum = 2.5f
            axisMaximum = 3.2f
            setDrawGridLines(true)
        }
        lineChart.axisRight.isEnabled = false

        lineChart.description = Description().apply { text = "" }
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.animateX(1000)
    }

    class XAxisFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return "HK${(value + 1).toInt()}"
        }
    }
}
