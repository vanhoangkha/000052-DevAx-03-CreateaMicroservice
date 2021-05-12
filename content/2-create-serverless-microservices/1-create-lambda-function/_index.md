+++
title = "Tạo và kiểm tra hàm Lambda tại cục bộ"
weight = 1
chapter = false
pre = "<b>2.1. </b>"
+++

#### Tạo và kiểm tra hàm Lambda tại cục bộ

1. Mở **Eclipse IDE** và chọn biểu tượng **AWS toolkit**, chọn **New AWS Lambda Java Project…**
![OpenIDE](../../../images/2/1.png?width=90pc)
2. Một hộp thoại **New AWS Lambda Maven Project** sẽ xuất hiện. Đặt Project name là **TestLambda**, Group ID là **idevelop.lambda** và Artifact ID là **s3handler**. Chọn **Finish** để tạo project.
![NewLambdaProject](../../../images/2/2.png?width=90pc)
3. Chúng ta cần cập nhật tập tin pom.xml mà Maven sử dụng lên phiên bản Mockito mới hơn.
![UpdatePomfile](../../../images/2/3.png?width=90pc)
Sau khi cập nhật các phụ thuộc của project thông qua Maven, chạy **JUnit Test** bằng chạy chọn **Run As…|JUnit Test**.
![RunJUnitTest](../../../images/2/4.png?width=90pc)
Bạn sẽ thấy kết quả đầu ra từ hàm lambda, như thể nó được kích hoạt bởi một tập tin được tải lên S3. Các tham số cho bài test được cung cấp trong test resource, ở dạng JSON payload giống với payload mà môi trường Amazon sẽ gửi đến hàm Lambda, khi S3 bucket được liên kết với hàm Lambda này nhận được tập tin được tải lên.
![RunJUnitTest](../../../images/2/5.png?width=90pc)
{{% notice tip %}}
Bạn có thể sẽ thấy một vài ảnh báo liên quan tới profile name. Bạn có thể bỏ qua cảnh báo này trong bài thực hành này.
Để xem kết quả đầu ra của phần JUnit test, chọn tab JUnit
{{% /notice %}}
4.  Kiểm tra tập tin S3-event.put.json. Tập tin S3-event.put.json chứa những schema và giá trị mà chúng ta sẽ sử dụng cho bài thực hành này.  
{{% notice tip %}}
Bạn có thể sẽ thấy một cảnh báo liên quan tới việc thiếu node. Bạn có thể bảo qua cảnh báo này một cách an toàn.
![RunJUnitTest](../../../images/2/6.png?width=90pc)
{{% /notice %}}

#### Cậo nhật code được cung cấp để xử lý các URL encoded keys

Mã nguồn được cung cấp không quan tâm tới việc mã hóa được áp dụng cho key name được cung cấp trong S3 event khi nó được gửi tới hàm Lambda, do đó nếu bạn tải một tập tin để kiểm tra, và tập tin chứa khoảng trắng hoặc dấu cấu thì chuỗi này cần được decode trước khi sử dụng.\
Bạn có thể giải quyết vấn đề này bằng đoạn code sau.\
5.  Thêm đoạn code sau vào sau dòng 28 trong lớp **LambdaFunctionHandler.java** trong *src/main/java/idevelop.lambda.s3handler*:
```java
try
{
  key = java.net.URLDecoder.decode(key, "UTF-8");
}
catch(Exception ex)
{
  context.getLogger().log("Could not decode URL for keyname... continuing...");
}
```
![CreatePrject](../../../images/2/7.png?width=90pc)