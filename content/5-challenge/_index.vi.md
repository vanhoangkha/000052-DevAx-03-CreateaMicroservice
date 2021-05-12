+++
title = "Thử thách - Expose microservice API"
date = 2021
weight = 5
chapter = false
pre = "<b>5. </b>"
+++

Trong phần này, chúng ta sẽ thực hiện các tác vụ tương tự phần 3 khi bạn expose FlightSpecials microservice, nhưng lần này là dịch vụ HotelSpecials microservice.
Không có chia sẽ dữ liệu giữa FlightSpecials và HotelSpecials services nên cả hai đủ điều kiện cho microservices riêng lẻ với kho dữ liệu riêng biệt. Trong bài thực hành này, để tiết kiệm thời gian, dữ liệu của HotelSpecials microservice có thể được cung cấp từ cùng một RDS database với FlightSpecials, tuy nhiên lưu ý rằng trong môi trường thực tế, bạn cần tách dữ liệu ra thành những kho lưu trữ khác nhau.
Expose cả 2 dịch vụ FlightSpecials và HotelSpecials dưới dạng các API riêng biệt thông qua Amazon API Gateway, bằng cách sửa đổi định nghĩa API swagger.yaml và định nghĩa CloudFormation/SAM template.yml
1. Sao chép tập tin **HotelSpecial.java** vào đường dẫn **.../src/main/java/devlounge/model**
![Challenge](../../../images/5/1.png?width=90pc)
2. Thêm đường dẫn API **/hotelspecials** vào dòng code thứ 10 trong tập tin swagger.yml
![Challenge](../../../images/5/2.png?width=90pc)
3. Mở tập tin template.yml và thêm các resource. Bạn có thể tham khảo tập tin template.yml sau:
{{%attachments /%}}
![Challenge](../../../images/5/3.png?width=90pc)
![Challenge](../../../images/5/4.png?width=90pc)
4. Tiến hành deploy lại và kiểm tra kết quả
![Challenge](../../../images/5/5.png?width=90pc)

#### Bài tập nâng cao tùy chọn

5. Sửa đổi mã nguồn từ phần 3 để phát hiện các loại file khác nhau và xử lý chúng theo cách tùy chọn (ví dụ, chặn và di chuyển chúng tới một thư mục khác)
6. Tạo một hàm Lambda mới thực hiện các task mỗi phút một lần. Task có thể đơn giản như ghi kết quả đầu ra vao CloudWatch Logs.