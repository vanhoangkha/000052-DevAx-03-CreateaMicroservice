+++
title = "(Optional) Cập nhật quyền của hàm Lambda"
weight = 3
chapter = false
pre = "<b>3.3. </b>"
+++

Nếu bạn xem trigger trong Lambda function, bạn sẽ thấy rằng S3 trigger không xuất hiện mặc dù hàm vẫn chạy như mong muốn. Bởi vì hàm Lambda không biết về S3 bucket trigger - S3 biết về hàm Lambda mà nó sẽ kích hoạt khi một sự kiện diễn ra nhưng ngược lại thì không.
![Trigger](../../../images/3/25.png?width=90pc)
1. Để S3 trigger xuất hiện trong AWS Lambda, sử dụng lệnh sau:
```bash
aws lambda add-permission --function-name <REPLACE_LAMBDA_FUNCTION_NAME> --region <REPLACE_REGION> --statement-id PolicyDocument --action "lambda:InvokeFunction" --principal s3.amazonaws.com --source-arn arn:aws:s3:::<REPLACE_S3_BUCKET_NAME> --source-account <REPLACE_AWS_ACCOUNT_ID> --profile aws-lab-env
```
![Trigger](../../../images/3/26.png?width=90pc)
Đảm bảo rằng bạn đã thay thế các mục trong dấu ngoặc bằng các thông tin từ môi trường thực hành.
Truy cập vào lại AWS Lambda console, bạn sẽ thấy S3 trigger xuất hiện.
![Trigger](../../../images/3/27.png?width=90pc)