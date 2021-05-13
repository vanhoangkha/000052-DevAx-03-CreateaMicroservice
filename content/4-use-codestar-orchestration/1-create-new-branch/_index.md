+++
title = "Tạo nhanh mới"
weight = 1
chapter = false
pre = "<b>4.1. </b>"
+++

Bây giờ bạn đã có kinh nghiệm thực tế về việc tạo và triển khai các hàm AWS Lambda, đã đến lúc quay lại ứng dụng TravelBuddy monolithic của chúng ta và triển khai một microservice trên AWS Lambda được quản lý thông qua CI/CD pipeline được tạo bằng AWS CodeStar.
1. Mở AWS Code Star console và chọn **Create Project**.Chúng ta sẽ cần tạo service role cho code star trong lần đầu tiên sử dụng. Click "Create service role"
![CreateCodeStarproject](../../../images/4/1.png?width=90pc)
2. Bạn sẽ thấy nhiều thẻ template
3. Bởi vì mã nguồn chương trình được viết bằng Java, do đó, chúng ta sẽ chọn những tùy chọn sau:
- AWS Lambda
- Web service
- Java
![CreateCodeStarproject](../../../images/4/2.png?width=90pc)
Đánh dấu chọn Java Spring và chọn **Next**
4. Tại mục Project name, nhập **dev-flight-svc**
5. Chọn Code Commit , click **Next** và click **Create Project**.
![CreateCodeStarproject](../../../images/4/3.png?width=90pc)
6. Chọn **Team**, chọn **Add team member** và thêm user **awsstudent** với quyền owner
![CreateCodeStarproject](../../../images/4/4.png?width=90pc)
7. Mở Eclipse IDE, tìm biểu tượng **AWS** và chọn để mở menu
8. Chọn **Import AWS CodeStar Project** 
![ImportProject](../../../images/4/5.png?width=90pc)
9.  Chọn **dev-flight-svc** trong danh sách và chọn **dev-flight-svc** trong repository.
10. Nhập Git credentials. Thông tin về Git credentials có thể xem ở mục Output của AWS Cloudformation của stack đầu tiên chúng ta tạo trong giai đoạn chuẩn bị . Chọn **Next**
![ImportProject](../../../images/4/6.png?width=90pc)
11. Chọn nhánh **master** và chọn **Next**. Nếu có thông báo lỗi xuất hiện, chọn **OK** để đóng
![ImportProject](../../../images/4/7.png?width=90pc)
12. Chọn **Finish**
![ImportProject](../../../images/4/8.png?width=90pc)
Bây giờ, IDE sẽ tải các tập tin từ CodeCommit repository xuống.
13.  Hãy dành một ít thời gian đêr xem cấu trúc project trước khi tiếp tục. Microservice HelloWorld biểu diễn một xử lý đơn giản, trả về trang web Hello World khi được gọi.
![CodeStarProject](../../../images/4/9.png?width=90pc)