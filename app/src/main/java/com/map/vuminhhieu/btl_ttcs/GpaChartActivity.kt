package com.map.vuminhhieu.btl_ttcs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.FloatEntry

class GpaChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val semesterGpas = intent.getFloatArrayExtra("semester_gpas")?.toList() ?: listOf()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GpaChartScreen(semesterGpas)
                }
            }
        }
    }
}

@Composable
fun GpaChartScreen(semesterGpas: List<Float>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tạo danh sách các điểm dữ liệu
        val entries = List(semesterGpas.size) { index ->
            FloatEntry(x = index.toFloat(), y = semesterGpas[index])
        }

        Chart(
            chart = columnChart(),
            model = entryModelOf(entries),
            startAxis = startAxis(
                title = "GPA",
                valueFormatter = { value, _ -> "%.1f".format(value) }
            ),
            bottomAxis = bottomAxis(
                title = "Semester",
                valueFormatter = { value, _ -> "S${(value + 1).toInt()}" }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}