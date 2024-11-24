package com.nauh.waterqualitymonitor.charts

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun TurbidityChart() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context: Context -> LineChart(context) },  // Tạo đối tượng LineChart từ context
        update = { chart ->
            // Dữ liệu mẫu cho độ đục (Turbidity)
            val entries = listOf(
                Entry(0f, 5f),  // (time, turbidity_value)
                Entry(1f, 7f),
                Entry(2f, 3f),
                Entry(3f, 8f),
                Entry(4f, 6f)
            )

            val dataSet = LineDataSet(entries, "Turbidity (NTU)")  // Tạo dataset
            dataSet.color = Color.BLUE  // Màu của đường biểu đồ
            dataSet.valueTextColor = Color.BLACK  // Màu của giá trị hiển thị

            val lineData = LineData(dataSet)  // Tạo LineData từ dataset
            chart.data = lineData

            // Cấu hình trục X và Y
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.isEnabled = false

            chart.description.isEnabled = false  // Tắt mô tả biểu đồ
            chart.invalidate()  // Làm mới biểu đồ sau khi cập nhật dữ liệu
        }
    )
}
