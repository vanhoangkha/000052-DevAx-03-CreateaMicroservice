+++
title = "Tự động hóa Serverless Microservice"
weight = 2
chapter = false
pre = "<b>3.2. </b>"
+++

Trong bài tập trước, bạn đã sử dụng IDE Eclipse để tạo và cập nhật một hàm Lambda bằng Bộ công cụ AWS cho Eclipse. Điều này cho phép bạn khởi tạo việc tự động tải lên hàm Lambda của mình theo cách thủ công. Tuy nhiên, cơ chế này có thể không thuận tiện cho việc tự động hóa các bước triển khai cho các hàm hoặc phối hợp triển khai và cập nhật cho các phần tử khác của ứng dụng serverless, chẳng hạn như event sources và downstream resources. Ví dụ: IDE Eclipse không cung cấp cho bạn khả năng triển khai và cập nhật S3 bucket và kết nối S3 PUT OBJECT trigger, cùng với hàm Lambda của bạn như một đơn vị triển khai.

Bạn có thể sử dụng **AWS CloudFormation** để dễ dàng chỉ định, triển khai và định cấu hình các ứng dụng serverless. AWS CloudFormation là một dịch vụ giúp bạn lập mô hình và thiết lập các tài nguyên Amazon Web Services để bạn có thể dành ít thời gian hơn cho việc quản lý các tài nguyên đó và nhiều thời gian hơn để tập trung vào các ứng dụng chạy trong AWS của bạn. Bạn tạo một mẫu mô tả tất cả các tài nguyên AWS mà bạn muốn (như các hàm Lambda và nhóm S3) và AWS CloudFormation sẽ đảm nhận việc cung cấp và cấu hình các tài nguyên đó cho bạn.

Ngoài ra, bạn có thể sử dụng **AWS Serverless Application Model (SAM)** để thể hiện các tài nguyên bao gồm ứng dụng serverless. Các loại tài nguyên này, chẳng hạn như các hàm và API của AWS Lambda, được AWS CloudFormation hỗ trợ đầy đủ và giúp bạn xác định và triển khai ứng dụng serverless của mình dễ dàng hơn.

Trong bài tập này, bạn sẽ sử dụng **AWS CLI** và **AWS CloudFormation/SAM** để đóng gói ứngd dụng và triển khai nó từ đầu mà không cần phải tạo hoặc định cấu hình bất kỳ phần phụ thuộc nào theo cách thủ công.

1. Tạo một tập tin SAM/CloudFormation YAML có tên **template.yaml** trong thư mục chứa tập tin **pom.xml** của hàm TestLambda ở bài tập trước. Nội dung của tập tin **template.yaml** như sau:
```
AWSTemplateFormatVersion: '2010-09-09'
Transform: 'AWS::Serverless-2016-10-31'
Description: Testing lambda and S3
Resources:
  TestLambda:
    Type: 'AWS::Serverless::Function'
    Properties:
      Handler: idevelop.lambda.s3handler.LambdaFunctionHandler
      Runtime: java8
      CodeUri: target/s3handler-1.0.0.jar
      Description: Testing lambda and S3
      MemorySize: 512
      Timeout: 15
      Role: !Sub arn:aws:iam::${AWS::AccountId}:role/LambdaRole
      Events:
        CreateThumbnailEvent:
          Type: S3
          Properties:
            Bucket:
              Ref: ImageManagerSrcBucket
            Events:
              - 's3:ObjectCreated:Put'
            Filter:
              S3Key:
                Rules:
                  - Name: prefix
                    Value: uploads/

  ImageManagerSrcBucket:
    Type: AWS::S3::Bucket
    Properties:
      BucketName: !Sub idevelop-imagemanager-${AWS::AccountId}
```
![Creattemplate](../../../images/3/15.png?width=90pc)
Mẫu này biểu diễn tả một S3 bucket sẽ kích hoạt hàm Lambda của chúng ta bất cứ khi nào có một tập tin được tải lên thư mục **uploads** - tương tự như S3 bucket mà chúng ta đã tạo ở phần trước.\
2. Tạo một artifact triển khai cho hàm Lambda - một tập tin **JAR** chứa hàm Lambda và tất cả các phụ thuộc của nó. Chúng ta có thể sử dụng commandline để làm điều này.
```bash
mvn package shade:shade -DskipTests=true
```
{{%notice tip%}}
Nếu bị thiếu maven, bạn có thể download và giải nén file chứa maven dưới đây.
Sau đó tiến hành cấu hình biến môi trường MVN_HOME tới **đường dẫn đã giải nén\bin** ví dụ : **C:\apache-maven-3.8.1\bin**
{{%/notice%}}

**File Apache Maven:**
{{%attachments /%}}

![CreateJarfile](../../../images/3/16.png?width=90pc)
Kết quả trả về một tập tin có tên **s3handler-1.0.0.jar** trong thư mục **target**
![UploadJarfile](../../../images/3/17.png?width=90pc)
3. Tiếp theo, chúng ta sẽ sử dụng AWS CLI để đẩy tập tin này lên S3 bucket nơi mà nó có thể được triển khai. Chúng ta sẽ sử dụng lệnh **aws cloudformation package**
```bash
aws cloudformation package --template-file template.yaml --s3-bucket <YOUR_CODE_BUCKET_NAME> --output-template deploy-template.yaml --profile aws-lab-env
```
![PushfiletoS3](../../../images/3/18.png?width=90pc)
**Note:** Nếu bạn thấy một thông báo lỗi **‘NoneType’ object has no attribute ‘items’** hãy kiểm tra lại format của tập tin YAML.

Câu lệnh **aws cloudformation package** sẽ lấy mẫu AWS SAM được cung cấp và viết lại nó trong định nghĩa của artifact được tự động tải lên S3 bucket. Trong trường hợp này,**deploy-template.yaml** được tạo và chứa giá trị **CodeUri**trỏ đến tập tin zip triển khai trong Amazon S3. Mẫu này đại diện cho ứng dụng serverless của bạn.\
1. Bây giờ bạn đã sẵn sàng triển khai tập tin JAR dưới dạng một hàm Lambda và kết nối S3 trigger vào một S3 bucket mới. Bạn sẽ nhận thấy kết quả từ lệnh trước đó hướng dẫn chúng ta những gì cần chạy để triển khai mẫu đóng gói. Trong cửa sổ dòng lệnh, sao chép lệnh và dán lại vào dòng lệnh. Thay đổi giá trị <YOUR_STACK_NAME> thành ImageManagerDemo và thêm vào tùy chọn --profile aws-lab-env để cho phép CloudFormation tạo vai trò IAM thay mặt bạn. Lệnh của bạn sẽ giống như sau:
```bash
aws cloudformation deploy --template-file deploy-template.yaml --stack-name ImageManagerDemo --profile aws-lab-env
```
![DeployCF](../../../images/3/19.png?width=90pc)
Khi bạn chạy lệnh **aws cloudformation deploy** nó sẽ tạo một **AWS CloudFormation ChangeSet** và triển khai chúng, đây là danh sách các thay đổi đối với AWS CloudFormation stack. Một vài mẫu stack có thể bao gồm các tài nguyên ảnh hưởng đến quyền trong tài khoản AWS, chẳng hạn như bằng cách tạo một AWS Identity and Access Management (IAM) user mới. Đối với các stack đó, bạn phải công nhận các khả năng của nó bằng các chỉ định tham số -capabilities. Để biết thêm thông tin, xem CreateChangeSet trong AWS CloudFormation API Reference.
{{% notice tip %}}
Bạn có thể xem quá trình tạo tài nguyên trực tiếp trong bảng điều khiển CloudFormation
{{% /notice %}}
Để kiểm tra kết quả, mở AWS CloudFormation console để xem stack mới nhất được tạo và vào Lambda console để xem hàm của bạn.
CloudFormation template tạo một S3 Bucket có tên **idevelop-imagemanager-<YOUR_ACCOUNT_ID>** trong đó YOUR_ACCOUNT_ID là AWS account ID được sử dụng cho môi trường bài thực hành này. 
![Createstack](../../../images/3/20.png?width=90pc)
Template cũng tạo một hàm Lambda mới có tên là  ImageManagerDemo-TestLambda-XXXXXX trong đó XXXXXX là một mã định danh ngẫu nhiên được CloudFormation phân bổ để đảm bảo tính duy nhất của tên hàm.\
5. Khi CloudFormation stack triển khai thành công, bạn đã sẵn sàng để kiểm tra hàm của mình. Sử dụng AWS S3 console, tạo một thư mục tên là **uploads** trong S3 bucket **idevelop-imagemanager-<YOUR_ACCOUNT_ID>**
![Createfolder](../../../images/3/21.png?width=90pc)
6. Tải một tập tin lên thư mục này. Bạn có thể sử dụng tập tin **Puppy.jpg** hoặc bất kỳ tập tin hình ảnh khác mà bạn có. Hoặc bạn có thể tải một tập tin không phải hình ảnh lên để kiểm tra cả hai trường hợp. 
Nếu bạn tải tập tin không phải hình ảnh lên, tập tin sẽ bị xóa.
![Uploadfile](../../../images/3/22.png?width=90pc)
7. Nếu bạn tải một hình ảnh lên, kiểm tra thu mục **processed** trong S3 bucket. Bạn sẽ thấy một hình thu nhỏ của tập tin JPG trong thư mục này.
![UploadImage](../../../images/3/23.png?width=90pc)
8. Xem log trong CloudWatch logs của hàm **ImageManagerDemo-TestLambda-XXXXXX**.
![Viewlog](../../../images/3/24.png?width=90pc)