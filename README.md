# Matrix Network Project (TCP & UDP Multi-Protocol)

Dự án thực hiện các phép toán ma trận thông qua hai giao thức **TCP** và **UDP**, sử dụng kiến trúc **Maven Multi-module** và đóng gói bằng **Docker**. Hệ thống hỗ trợ tính toán đa luồng (Concurrent), cho phép nhiều Client kết nối và xử lý đồng thời.

## 1. Cấu trúc Project

```Plaintext
matrix-network-project/
├── pom.xml (Parent)         # Quản lý version, dependencies (Gson, v.v.)
├── common/                  # Module chứa logic Core (Matrix, Request, Response)
├── server/                  # Module Server (Xử lý đa giao thức, Multi-threading)
└── client/                  # Module Client (Giao diện chọn giao thức, Socket Client)
```

## 2. Yêu cầu hệ thống

- **Java**: JDK 21.
- **Maven**: 3.8+.
- **Docker & Docker Compose** (Nếu chạy bằng container).

## 3. Hướng dẫn Cài đặt & Biên dịch

Trước khi chạy bất kỳ module nào, **BẮT BUỘC** phải cài đặt module `common` vào kho lưu trữ địa phương để các module khác nhận diện được logic toán học.

Mở Terminal tại thư mục gốc (`matrix-network-project`) và chạy:

```PowerShell
mvn clean install
```

## 4. Chạy bằng Terminal (Máy thật)

Hệ thống đã tích hợp **Unified Launcher**, cho phép khởi động đa giao thức mà không cần đổi class thủ công.

**Cửa sổ 1: Chạy Server (Lắng nghe đồng thời cả TCP & UDP)**

```PowerShell
cd server
mvn exec:java "-Dexec.mainClass=com.network.server.ServerMain"
```

**Cửa sổ 2: Chạy Client (Menu chọn giao thức và tính toán)**

```PowerShell
cd client
mvn exec:java "-Dexec.mainClass=com.network.client.ClientMain"
```

Lưu ý: Sau khi khởi động Client, hệ thống sẽ hỏi bạn muốn dùng giao thức nào (1 cho TCP, 2 cho UDP) trước khi vào menu chính.

## 5. Triển khai bằng Docker (Khuyên dùng)

Hệ thống đã được tối ưu hóa bằng **Biến môi trường (Environment Variables)** để tự động nhận diện Host mạng.

**Bước 1: Đóng gói Fat JAR**
```PowerShell
mvn clean package -DskipTests
```

**Bước 2: Khởi động hệ thống Containers**
```PowerShell
docker-compose up --build -d
```

**Bước 3: Tương tác với Client**

```PowerShell
docker-compose run --rm matrix-client
```

Dùng lệnh `run` để đảm bảo bộ đệm nhập liệu (STDIN) hoạt động ổn định nhất.

## 6. Cơ chế Kỹ thuật & Chức năng

- **Tính năng (10 phép toán)**: Cộng, Trừ, Nhân (2 ma trận), Nhân vô hướng, Chuyển vị, Trace, Đối xứng, Định thức, Nghịch đảo, Hạng (Rank).

- **Đa giao thức**: Server chạy song song TCP (Port 65432) và UDP (Port 65433).

- **Đồng thời (Concurrency)**: Xử lý đa luồng (Thread-per-client) cho TCP.

- **Tin cậy**: UDP được cấu hình `Timeout 5s` để chống treo khi mất gói tin.

## 7. Dữ liệu Test mẫu

| **Phép toán** | **Ma trận nhập vào** | **Kết quả mong đợi** | 
| :--- | :--- | :--- |
| **CỘNG (1)** | **A**: `[1,2][3,4]` <br> **B**: `[5,6][7,8]` | `[6,8][10,12]` |
| **ĐỊNH THỨC (8)** | **A**: `[4,7][2,6]` | `10.0` | 
| **HẠNG (10)** | **A**: `[1,2,3][2,4,6][0,1,1]` | `2` | 

### Thông tin thành viên
- **Họ và tên**: Đặng Xuân Nam
- **MSSV**: 2200106