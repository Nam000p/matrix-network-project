# Matrix Network Project (TCP Concurrent Server)

Dự án thực hiện các phép toán ma trận thông qua giao thức TCP, sử dụng kiến trúc **Maven Multi-module** và đóng gói bằng **Docker**. Hệ thống hỗ trợ tính toán đa luồng (Concurrent), cho phép nhiều Client kết nối và tính toán cùng lúc.

## 1. Cấu trúc Project

```Plaintext
matrix-network-project/
├── pom.xml (Parent)         # Quản lý version, dependencies (Gson, v.v.)
├── common/                  # Module chứa logic Core (Matrix, Request, Response)
├── server/                  # Module Server (Xử lý TCP, Multi-threading)
└── client/                  # Module Client (Giao diện Console, Socket Client)
```

## 2. Yêu cầu hệ thống

- Java: JDK 21.
- Maven: 3.8+.
- Docker & Docker Compose (Nếu chạy bằng container).

## 3. Hướng dẫn Cài đặt & Biên dịch

Trước khi chạy bất kỳ module nào, bạn BẮT BUỘC phải build module common để các module khác nhận diện được thư viện logic.

Mở Terminal tại thư mục gốc (matrix-network-project) và chạy:

```PowerShell
mvn clean install
```

**Lưu ý:** Phải thấy dòng **BUILD SUCCESS** cho cả 4 module mới có thể tiếp tục.

## 4. Chạy bằng Terminal (Máy thật)

Mở 2 cửa sổ Terminal/PowerShell riêng biệt:

**Cửa sổ 1: Chạy Server**

```PowerShell
cd server
mvn exec:java "-Dexec.mainClass=com.network.server.ServerTCP"
```

**Cửa sổ 2: Chạy Client**

```PowerShell
cd client
mvn exec:java "-Dexec.mainClass=com.network.client.ClientTCP"
```

## 5. Triển khai bằng Docker (Khuyên dùng)

Hệ thống đã được tối ưu hóa bằng **Biến môi trường (Environment Variables)** và **Unified Launcher**, giúp tự động cấu hình và hỗ trợ cả hai giao thức TCP/UDP cùng lúc.

**Bước 1: Đóng gói ứng dụng (Fat JAR)**

Tại thư mục gốc dự án, chạy lệnh để Maven gom tất cả thư viện (Gson) và logic vào file JAR:

```PowerShell
mvn clean package -DskipTests
```

**Bước 2: Khởi động hệ thống Containers**

Lệnh này sẽ build lại Image từ code mới nhất và chạy Server ở chế độ ngầm (Background). Server sẽ tự động mở cổng **65432 (TCP)** và **65433 (UDP)**.

```PowerShell
docker-compose up --build -d
```

**Bước 3: Tương tác với Client**

Thay vì dùng `attach` dễ gây lỗi bộ đệm nhập liệu, hãy sử dụng lệnh `run` để tạo một phiên làm việc tương tác (Interactive) sạch sẽ:

```PowerShell
docker-compose run --rm matrix-client
```

**Tại sao nên dùng lệnh này?**

- `--rm`: Tự động xóa container rác sau khi thoát menu.

- Hệ thống sẽ hiện Menu chọn Giao thức ngay khi khởi động. Ta chỉ cần chọn 1 (TCP) hoặc 2 (UDP) để bắt đầu tính toán.

## 6. Cơ chế Kỹ thuật nổi bật

- **Đa giao thức (Dual-Stack)**: Server sử dụng Multi-threading để lắng nghe đồng thời trên cả TCP và UDP.

- **Tự động cấu hình**: Client sử dụng biến môi trường `SERVER_HOST`.

    - Khi chạy Docker: Tự nhận diện server qua tên dịch vụ `matrix-server`.

    - Khi chạy máy thật: Tự động dùng `localhost`.

- **Độ tin cậy UDP**: Client UDP được cấu hình `Timeout 5s` để xử lý trường hợp mất gói tin đặc trưng của giao thức không kết nối.

## 7. Dữ liệu Test mẫu (Dành cho nhóm)

| Giao thức	| Lệnh chạy (Docker) | Kết quả kỳ vọng |
| :---  | :--- | :--- |
| TCP |	Chọn menu `1` -> Phép toán `8` (Định thức) | Trả về giá trị số thực chính xác |
| UDP |	Chọn menu `2` -> Phép toán `6` (Trace) |	Kết quả trả về tức thì (Low latency) |


| Phép toán | Ma trận nhập vào | Kết quả mong đợi |
| :--- | :---: | :---: |
| **CỘNG (1)** | **A**: `[1,2][3,4]` <br> **B**: `[5,6][7,8]` | `[6,8]` <br> `[10,12]` |
| **TRACE (6)** | **A**: `[4,7][2,6]` | `10.0` | 
| **ĐỊNH THỨC (8)** | **A**: `[4,7][2,6]` | `10.0` | 
| **HẠNG (10)** | **A**: `[1,2,3][2,4,6][0,1,1]` | `2` |

## 8. Sửa lỗi thường gặp

1. **Lỗi** `ClassNotFoundException`: Do chưa chạy `mvn clean install` ở thư mục gốc.

2. **Lỗi Port 65432 đã bị sử dụng**: Chạy lệnh `netstat -ano | findstr :65432` rồi kill process đang chiếm port.

3. **Lỗi Tiếng Việt trong Terminal**: Nếu gặp lỗi font, hãy chạy lệnh `chcp 65001` trước khi chạy Maven.

### Thông tin thành viên
- Họ và tên: Đặng Xuân Nam
- MSSV: 2200106