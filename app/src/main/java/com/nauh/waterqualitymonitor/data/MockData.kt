package com.nauh.waterqualitymonitor.data

// Notification Data Class
data class Notification(val id: Int, val title: String, val description: String, val turbidity: Int)

// Mock Notification Data
val mockNotifications = listOf(
    Notification(1, "Ngắt nước do độ đục cao", "Hệ thống đã ngắt nước do độ đục vượt quá ngưỡng cho phép.", 10),
    Notification(2, "Cảnh báo nhiệt độ", "Nhiệt độ nước đã vượt ngưỡng an toàn.", 0),
    Notification(3, "Ngắt nước do độ dẫn cao", "Hệ thống đã ngắt nước do độ dẫn điện vượt ngưỡng cho phép.", 0),
    Notification(4, "Cảnh báo hóa đơn", "Hóa đơn nước sắp đến hạn thanh toán.", 0),
    Notification(5, "Cập nhật chỉ số nước", "Chỉ số nước tháng này đã được cập nhật.", 0),
    // Thêm nhiều thông báo khác
    Notification(6, "Ngắt nước khẩn cấp", "Sự cố hệ thống, nước đã bị ngắt khẩn cấp.", 0),
    Notification(7, "Khôi phục dịch vụ", "Nước đã được khôi phục sau sự cố.", 0),
    Notification(8, "Cảnh báo chất lượng nước", "Chất lượng nước không đảm bảo, xin kiểm tra.", 0)
)

// Water Bill Data Class
data class WaterBill(val id: Int, val customerName: String, val address: String, val oldWaterIndex: Int, val currentWaterIndex: Int, val costPerCubicMeter: Double)

// Mock Water Bill Data
val mockWaterBills = listOf(
    WaterBill(1, "Nguyễn Văn A", "Hà Nội", 230, 245, 28000.0),
    WaterBill(2, "Trần Thị B", "Hồ Chí Minh", 210, 220, 30000.0),
    WaterBill(3, "Phạm Văn C", "Đà Nẵng", 180, 190, 25000.0),
    WaterBill(4, "Lê Văn D", "Huế", 270, 280, 27000.0),
    WaterBill(5, "Nguyễn Thị E", "Thái Bình", 300, 315, 32000.0),
    // Thêm nhiều hóa đơn khác
    WaterBill(6, "Đỗ Văn F", "Ninh Bình", 340, 355, 28000.0),
    WaterBill(7, "Phạm Văn G", "Quảng Ninh", 400, 420, 26000.0),
    WaterBill(8, "Hoàng Thị H", "Hải Phòng", 220, 230, 29000.0)
)

// Chart Data Class (for Statistics page)
data class WaterStatistics(val month: String, val turbidity: Float, val conductivity: Float, val temperature: Float, val waterBill: Float)

// Mock Statistics Data (for the charts)
val mockStatistics = listOf(
    WaterStatistics("Tháng 1", turbidity = 5f, conductivity = 400f, temperature = 28f, waterBill = 500000f),
    WaterStatistics("Tháng 2", turbidity = 6f, conductivity = 450f, temperature = 29f, waterBill = 520000f),
    WaterStatistics("Tháng 3", turbidity = 7f, conductivity = 500f, temperature = 27f, waterBill = 530000f),
    WaterStatistics("Tháng 4", turbidity = 8f, conductivity = 470f, temperature = 30f, waterBill = 550000f),
    WaterStatistics("Tháng 5", turbidity = 6.5f, conductivity = 480f, temperature = 26f, waterBill = 540000f),
    // Thêm dữ liệu cho các tháng khác
    WaterStatistics("Tháng 6", turbidity = 9f, conductivity = 490f, temperature = 31f, waterBill = 570000f),
    WaterStatistics("Tháng 7", turbidity = 5.5f, conductivity = 420f, temperature = 25f, waterBill = 500000f)
)

// Account Info Data Class (for Account Info page)
data class AccountInfo(val username: String, val fullName: String, val email: String, val phoneNumber: String, val address: String)

// Mock Account Data
val mockAccount = AccountInfo(
    username = "nauh",
    fullName = "Nguyễn Văn Huân",
    email = "huan.nguyen@example.com",
    phoneNumber = "0123456789",
    address = "Thụy Ninh, Thái Thụy, Thái Bình"
)
