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

## 5. Chạy bằng Docker (Khuyên dùng)

Hệ thống đã được cấu hình sẵn Docker Compose để triển khai nhanh.

**Bước 1: Đóng gói file JAR**
```PowerShell
mvn clean package -DskipTests
```

**Bước 2: Khởi động Containers***

```PowerShell
docker-compose up --build -d
```

**Bước 3: Tương tác với Client**

Vì Client chạy trong container, dùng lệnh sau để vào giao diện nhập liệu:

```PowerShell
docker attach matrix-client
```

## 6. Các tính năng hỗ trợ

Hệ thống hỗ trợ 10 phép toán ma trận từ cơ bản đến nâng cao:

- **Nhóm cơ bản:** Cộng, Trừ, Nhân (2 ma trận), Nhân vô hướng (Scalar).

- **Nhóm đặc trưng:** Chuyển vị, Tính vết (Trace), Kiểm tra đối xứng.

- **Nhóm nâng cao:** Tính định thức (Determinant), Nghịch đảo (Inverse), Tính hạng (Rank).

## 7. Dữ liệu Test mẫu (Dành cho nhóm)

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