+++
title = "Cập nhật target region cho API"
weight = 3
chapter = false
pre = "<b>4.3. </b>"
+++

Tập tin **swagger.yml** định nghĩa API được dùng cho microservice thông qua Amazon API Gateway. Chúng cần được cập nhật chi tiết AWS Account ID và target AWS Region trước khi triển khai microservice.

1. Trong Eclipse IDE, mở tập tin **swagger.yml**
2. Tìm và thay thế **<REGION>** bằng region đang được sử dụng trong bài thực hành.
3. Tìm và thay thế **<ACCOUNTID>** bằng AWS Account Id.
![UpdateSwaggerFile](../../../images/4/20.png?width=90pc)
4. Lưu tập tin.
5. Sử dụng commandline để cập nhật thay đổi lên git branch **new-implementation** và commit thay đổi.

Trước đó bạn sẽ cần cấu hình email và username cho git.
```
C:\Users\Administrator\git\dev-flight-svc>git config --global user.email "youremail"
C:\Users\Administrator\git\dev-flight-svc>git config --global user.name "awsstudent"
```

![Gitcommand](../../../images/4/21.png?width=90pc)

6. Để push thay đổi lên CodeCommit, trong Eclipse, nhất chuột phải vào project root và chọn **Teams | Push to origin…**
![PushCode](../../../images/4/22.png?width=90pc)
Bạn cần push từ Eclipse vì git credentials được nhúng vào môi trường Eclipse. Bạn cũng có thể sử dụng command line, nhưng trong bài thực hành này, chúng ta sẽ không sử dụng phương pháp này.
Sẽ tốn một khoảng thời gian để push code và bắt đầu triển khai. Hãy dành một khoảng thời gian để xem cấu trúc của project trong Eclipse IDE, đặc biệt là các tập tin buildspec.yml, swagger.yml và template.yml định nghĩa Amazon API Gateway, AWS Lambda Function và quá trình xây dựng/triển khai thông qua AWS CodeBuild.
Bạn có thể kiểm tra **status** của quá trình triển khai thông qua **Pipeline** trong CodeStar project dashboard.
{{% notice note %}}
Do hàm Lambda của chúng ta sử dụng **DBSecurityGroup** nên chúng ta cần cho phép **DBSecurityGroup** có thể kết nối được RDS Endpoint bằng cách cấu hình thêm như dưới đây:
![ConfigureSG](../../../images/4/25.png?width=90pc)
{{% /notice %}}

7. Khi quá trình triển khai hoàn tất, truy cập ứng dụng web bằng cách chọn **View application**.
![ViewApplication](../../../images/4/23.png?width=90pc)
Khi trang được mở, bạn sẽ thấy một thông báo lỗi **{"message":"Missing Authentication Token"}**. Điều này xảy ra  vì bạn đang cố gắng truy cập vào gốc của API, thay vì một microservice cụ thể. Chỉnh sửa URL, thêm **flightspecials** vào cuối URL, ta được URL mới có dạng **https://xxxxxxx.execute-api.us-east-1.amazonaws.com/Prod/flightspecials**. 
Nhấn Enter, ta nhận được kết quá như sau:
![ViewApplication](../../../images/4/24.png?width=90pc)
