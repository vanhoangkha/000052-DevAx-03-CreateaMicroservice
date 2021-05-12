+++
title = "Tải lên và kiểm tra hàm Lambda trong AWS Lambda"
weight = 2
chapter = false
pre = "<b>2.2. </b>"
+++

#### Tải và kiểm tra hàm Lambda trong AWS Lambda

1. Chúng ta sẽ triển khai hàm Lambda và kiểm tra chúng. Nhấp chuột phải vào **LambdaFunctionHandler.java** class trong *src/main/java/idevelop.lambda.s3handler*. Từ menu Amazon Web Services, chọn **Run function on AWS Lambda…**
![UpdateLambda](../../../images/2/8.png?width=90pc)
2. Chọn **Upload now**
![UpdateLambda](../../../images/2/9.png?width=90pc)
3. Chọn region đang thực hiện bài thực hành và chọn **Create a new Lambda function**. Nhập tên hàm Lambda là **TestLambda**
4. Chọn **Next**
![UpdateLambda](../../../images/2/10.png?width=90pc)
5. Tại mục description, nhập **Test AWS Lambda function triggered by S3 upload**
6. Tại mục **IAM role** chọn **LambdaRole** được tạo tự động trong phần cấu hình bài thực hành.
7. Tại mục **S3 bucket** chọn S3 bucket chúng ta đã tạo từ trước để chứ mã nguồn Lambda (idevelop-sourcecode-yourinitials).
8. Chọn **Finish** để tải hàm Lambda lên AWS Account. 
![UpdateLambda](../../../images/2/11.png?width=90pc)
9.  Việc tải lên sẽ tốn một vài phút. Khi việc tải lên hoàn tất, mở AWS Lambda Console và tìm hàm **TestLambda** vừa được tải lên.
![TestTestLambda](../../../images/2/12.png?width=90pc)
10.  Tìm **Add Triggers** trong thẻ **Designer**. Chọn S3.
![UpdateLambda](../../../images/2/13.png?width=90pc)
11. Tại mục **Bucket** chọn bucket mà bạn đã tạo để chứa hình ảnh tải lên (idevelop-imagemanager-yourinitials)
12. Tại mục **Event type** chọn **All Object create events**
13. Tại mục **Prefix** nhập **uploads/**. Không đặt giá trị **Filter pattern**
14. Đảm bảo rằng **Enable trigger** checkbox được chọn
15. Xác nhận tại mục **Recursive invocation** và chọn **Add**
![UpdateLambda](../../../images/2/14.png?width=90pc)
16. Bây giờ chúng ta sẽ cùng kiểm tra hàm Lambda. Truy cập **S3 console** và mở thư mục chứa hình ảnh được tải lên (idevelop-imagemanager-initials).
17. Tạo một thư mục tên **uploads**. Mở thư mục **uploads** vừa tạo và chọn **Upload** để tải một tập tin lên thư mục vừa tạo.
{{%attachments /%}}
![Createfolder](../../../images/2/15.png?width=90pc)
![Uploadimage](../../../images/2/16.png?width=90pc)
18. Khi việc tải lên được hoàn tất, trở lại **AWS Lambda**, chọn tab **Monitoring** của hàm **TestLambda**. Bạn có thể điểu chỉnh khung thời gian để xem dễ dàng hơn.
![Viewlog](../../../images/2/17.png?width=90pc)
19. Bạn sẽ thấy 2 lệnh gọi và thời lượng của chúng trong biểu đồ. Bạn thấy 2 lệnh gọi - một là tạo thư mục **uploads** và một là tải lên hình ảnh **Puppy.jpg**
20. Chọn **View logs in CloudWatch** để mở CloudWatch logs của hàm Lambda này. 
![Viewlog](../../../images/2/18.png?width=90pc)
Lưu ý rằng bạn sẽ thấy **CONTENT TYPE** output khác nhau ở 2 sự kiện này. Trong sự kiện đầu tiên, CONTENT TYPE là *application/x-directory* và ở sự kiện thứ 2 là *image/jpeg*. Trong mã nguồn mẫu của hàm Lambda đã cung cấp, chỉ ghi logs CONTENT TYPE của tập tin mà không thực hiện hành động nào khác. Chúng ta sẽ thay đổi đoạn code này trong các phần tiếp theo.
21.  Tải một tập tin bất kỳ không phải là hình ảnh lên S3 bucket và quan sát log.
![Uploadfile](../../../images/2/19.png?width=90pc)
![Uploadfile](../../../images/2/20.png?width=90pc)