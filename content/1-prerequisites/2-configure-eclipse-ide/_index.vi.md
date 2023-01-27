+++
title = "Cấu hình Eclipse IDE"
weight = 2
chapter = false
pre = "<b>1.2. </b>"
+++

**Nội dung**
- [Truy cập vào EC2](#truy-cập-vào-ec2)
- [Cấu hình Eclipse IDE](#cấu-hình-eclipse-ide)
- [Cài đặt S3 buckets](#cài-đặt-s3-buckets)

#### Truy cập vào EC2

1. Truy cập AWS EC2, và mở EC2 instance DevAxWindowsHost
2. Chọn **Connect**.

![RemoteInstances](/images/1/6.png?width=90pc)

3. Chọn **RDP** tab, sau đó ấn **Download remote desktop file** để tải tập tin RDP để truy cập từ xa.
4. Chọn **Get password**.

![RemoteInstances](/images/1/7.png?width=90pc)

5. Ấn **Upload private key file** and chọn keypair **KPforDevAxInstances** để giải mã mật khẩu cho máy ảo Windows.
6. Ấn **Decrypt password**.

![RemoteInstances](/images/1/8.png?width=90pc)

7. Sao chép mật khẩu và dùng để đăng nhập vào máy ảo Windows.

![RemoteInstances](/images/1/9.png?width=90pc)

#### Cấu hình Eclipse IDE
8. Mở command line và cấu hình profile cho bài thực hành

{{%notice tip%}}
Bạn hãy tạo access key và secret access key cho user **awsstudent** được tạo ra bởi Cloud Formation Template trong phần chuẩn bị và dùng nó để cấu hình cho awscli nhé.
Đồng thời hãy truy cập vào giao diện quản lý IAM , chọn **IAM User** awsstudent, chọn **Permissions**, **Add permissions**|**Attach existing policy directly** và chọn **AdministratorAccess**
{{%/notice%}}

```bash
aws configure set profile.aws-lab-env.region <your-region>
aws configure set profile.aws-lab-env.aws_access_key_id <your_access_key_id>
aws configure set profile.aws-lab-env.aws_secret_access_key <your_secret_access_key>
```

![EclipseIDE](/images/1/10.png?width=90pc)

9. Mở **Eclipse IDE**, trong cửa sổ Eclipse IDE Launcher , để tùy chọn mặc định và click **Launch**. Click **Finish** cho cửa sổ thu thập thông tin  của AWS Toolkit.

![EclipseIDE](/images/1/11.png?width=90pc)

10. Click nút **Restore**.

![EclipseIDE](/images/1/12.png?width=90pc)

11.  Click vào biểu tượng AWS và chọn dấu mũi tên, sau đó chọn **Preferences**.

![EclipseIDE](/images/1/13.png?width=90pc)

12. Đặt **Default Profile** là **aws-lab-env**.
13. Chọn **Apply and Close**.

![EclipseIDE](/images/1/14.png?width=90pc)

#### Cài đặt S3 buckets
Bạn cần cài đặt 2 buckets. Một bucket sẽ chứa hàm lambda được tải lên và một bucket được sử dụng để lưu trữ hình ảnh.

14. Tạo S3 bucket để lưu hàm Lambda. Bạn có thể sử dụng AWS Console hoặc dùng command line:
```bash
aws s3 mb s3://idevelop-sourcecode-<yourinitials> --region <region> --profile aws-lab-env
```

trong đó **<bucket-name>** là tên duy nhất cho bucket của bạn . Thay thế **<region>** với region phù hợp với bài thực hành của bạn.

15.  Tạo một bucket khác để chứa hình ảnh tải lên. Tương tự như bước trên, ta đặt tên cho S3 bucket là **idevelop-imagemanager-<yourinitials>**. Câu lệnh sẽ như sau:
```bash
aws s3 mb s3://idevelop-imagemanager-<yourinitials> --region <region> --profile aws-lab-env
```

![CreateS3bucket](/images/1/15.png?width=90pc)

Sau khi hoàn tất hai bước trên, bạn sẽ có 2 S3 bucket tương tự như hình dưới đây.

![CreateS3bucket](/images/1/16.png?width=90pc)
