+++
title = "Chuẩn bị môi trường"
weight = 1
chapter = false
pre = "<b>1.1. </b>"
+++

#### Chuẩn bị môi trường
Trong bài thực hành này, chúng ta sẽ sử dụng CloudFormation template đã cung cấp và cài đặt sẵn các tài nguyên cần thiết.

1. Trong bài thực hành này, chúng ta sẽ sử dụng lại Keypair **KPforDevAxInstances.PEM** đã tạo ở bài thực hành trước cho các máy ảo. Hoặc bạn có thể xem lại bài thực hành trước để xem cách tạo một keypair mới.
{{%notice tip%}}
Nếu bạn tạo keypair mới hãy down file dạng .pem để có thể giải mã password của DevAx Windows host nhé.
{{%/notice%}}

2. Tải tập tin CloudFormation template để cài đặt các thành phần cần thiết cho bài thực hành

{{%attachments /%}}

3. Truy cập **AWS CloudFormation**
4. Chọn **Create stack**, chọn **With new resources (standard)**

![CloudFormation](/images/1/1.png?width=90pc)

5. Tại mục **Prerequisite - Prepare template** chọn **Template is ready**
6. Tại mục **Template source**, chọn **Upload a template file**, nhấp chọn **Choose file** và trỏ tới file template đã tải xuống. Chọn **Next**

![CloudFormation](/images/1/2.png?width=90pc)

7. Nhập tên stack tại mục **Stack name**, ví dụ: `aws-stack-for-DevAX-lab03`.
8. Chọn keypair **KPforDevAxInstances** cho mục **EEKeyPair** và chọn **Next**

![CloudFormation](/images/1/3.png?width=90pc)

9. Chọn **Next** tại trang **Configure stack options**

![CloudFormation](/images/1/4.png?width=90pc)

10.  Đánh dấu chọn **I acknowledge that AWS CloudFormation might create IAM resources with customer names**.
- Chọn **Create stack**.

![CloudFormation](/images/1/5.png?width=90pc)

11. Chúng ta cần chờ một vài phút để các tài nguyên được khởi tạo và cấu hình.

![Diagram](/images/1/0.png?width=60pc)
