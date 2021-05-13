+++
title = "Triển khai lại thông qua CI/CD pipeline"
weight = 2
chapter = false
pre = "<b>4.2. </b>"
+++

Trong bài tập này, bạn sẽ cập nhật mã nguồn trong Eclipse, ghi đè lên mẫu Hello World! microservice và thay thế nó bằng mã TravelBuddy FlightService. Sau đó, bạn sẽ commit thay đổi mã vào CodeCommit để kích hoạt triển khai vào môi trường AWS Lambda của bạn và cập nhật cấu hình Amazon API Gateway để phù hợp.

#### Thêm code microservice vào nhánh mới

1. Chuyển đường dẫn vào thư mục gốc của CodeStar Project chúng ta vừa import. Dùng câu lệnh sau để tạo nhánh mới:
```bash
git checkout -b "new-implementation"
```
![Createbranch](../../../images/4/10.png?width=90pc)
2. Tải mã nguồn chương trình **FlightSpecials** và giải nén.\
{{%attachments /%}}
3. Trong Eclipse IDE, nhấp chuột phải vào project. chọn **Show In** và chọn **System Explorer**.\
4. Sao chép nội dung trong thư mục FlightSpecials vào thư mục vừa mở.
Bạn cần xóa nội dung trong hai thư mục **/src** và **/target** của project sample trước khi copy đè lên.  
{{%notice warning%}}
Nếu bạn không xóa nội dung hai thư mục **/src** và **/target** trước khi copy code mới vào thì quá trình build sẽ bị lỗi vì chúng ta không cấu hình cho **HelloWorldController / Handler**.
{{%/notice%}}
5. Nhấp chuột phải vào thư mục root của dự án, chọn **Maven | Update Project**, chọn **OK**.\
![Redeploy](../../../images/4/11.png?width=90pc)
Sau khi hoàn tất, bạn sẽ thấy cấu trúc thư mục như sau:
![Redeploy](../../../images/4/12.png?width=90pc)
6. Là một phần của cài đặt microservice, chúng ta sử dụng VPC và gán một IAM Role mới cho hàm Lambda để cho phép nó thực hiện các tác vụ khác nhau. Khi CodeStar tạo project, nó tạo một IAM Role cấp đủ quyền cho CloudFormation để triển khai dịch vụ HelloWorld. Các quyền này không đủ cho các yêu cầu cao hơn, do đó, chúng ta cần thay đổi policy để gán quyền cho CloudFormation để mở rộng các quyền.\
7. Mở AWS IAM Console và chọn **Roles**.\
8. Trong box **Filter**, nhập **CodeStarWorker-dev-flight-svc-CloudFormation** để tìm đúng IAM Role.\
9.  Mở IAM role và chọn **Add inline policy**.\
![Addpolicy](../../../images/4/13.png?width=90pc)
10. Chuyển đến tab JSON và dán đoạn mã sau đây vào:
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "iam:GetRole",
        "iam:CreateRole",
        "iam:DeleteRole",
        "iam:PassRole",
        "iam:PutRolePolicy",
        "iam:DeleteRolePolicy",
        "lambda:ListTags",
        "lambda:TagResource",
        "lambda:UntagResource",
        "lambda:AddPermission",
        "ec2:DescribeSecurityGroups",
        "ec2:DescribeSubnets",
        "ec2:DescribeVpcs",
        "ec2:CreateNetworkInterface",
        "ec2:AttachNetworkInterface",
        "ec2:DescribeNetworkInterfaces"
      ],
      "Resource": "*",
      "Effect": "Allow"
    }
  ]
}
```
![Addpolicy](../../../images/4/14.png?width=90pc)
11. Nhập tên policy **idevelopCodeStarCloudFormationPolicy** và chọn **Create policy**.
![Addpolicy](../../../images/4/15.png?width=90pc)
Khi thêm vai trò **CodeStarWorker-dev-flight-svc-CloudFormation**, các quyền trong **idevelopCodeStarCloudFormationPolicy** policy cho phép CLoudFormation hành động thay mặc bạn khi các triển khai thay đổi trong môi trường, bao gồm quyền cho phép hàm Lambda gắn với VPC nơi RDS instance lưu trữ trang web **TravelBuddy** được triển khai. Chúng cũng cho phép CloudFormation tạo một IAM Role mới mà hàm Lambda sử dụng để thực thi.\
12.   Tập tin CloudFormation template template.yml được cung cấp trong *FlightSpecials.zip* có một vài giá trị cần cập nhật trước khi tiến hành triển khai. Những giá trị này bao gồm Subnet Ids, Security Group Ids and the RDS Instance Endpoint.\
13.   Mở tập tin template.yml và thay thế giá trị **sg-<REPLACE>** bởi Security Group Id của **DBSecurityGroup**.\
![DBSecurityGroup](../../../images/4/16.png?width=90pc)
14. Thay thế giá trị **subnet-<REPLACE>** bằng 2 privateSubnet ID có tên **Module3/DevAxNetworkVPC/privateSubnet**
![SubnetID](../../../images/4/17.png?width=90pc)
15. Thay thế giá trị **<RDSEndpoint>** bằng **RDSEndpoint** được cung cấp trong mục OUTPUT của ClouFormation hoặc Endpoit trong RDS instance
![RDSEndpoint](../../../images/4/18.png?width=90pc)
16. Khi hoàn tất, bạn sẽ có VPCConfig section như sau:
![VPCConfig](../../../images/4/19.png?width=90pc)
17. Thêm đoạn code sau vào tập tin pom.yml để khai báo dependency bị thiếu
```
<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-api</artifactId>
			<version>5.5.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-web</artifactId>
	        <version>1.3.2.RELEASE</version>
		</dependency>
```