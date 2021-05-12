+++
title = "Triển khai hàm Lambda ImageManager"
weight = 1
chapter = false
pre = "<b>3.1. </b>"
+++

Bạn sẽ chỉnh sửa đoạn code trong **handleRequest** trong tập tin **LambdaFunctionHandler.java** để những tập tin có **contentType** không phải là **image/jpeg** sẽ bị xóa bởi hàm Lambda. Điều này mô phỏng một tình huống, trong đó nếu có một tập tin tải lên sai content type, tập tin này sẽ bị loại bỏ và không xử lý. Nếu tập tin đó là **image/jpeg**, hàm Lambda sẽ thu nhỏ hình ảnh và chuyển hình thu nhỏ tới một target bucket để ứng dụng có thể sử dụng chúng.

1. Đầu tiên, tìm dòng code sau trong tập tin **LambdaFunctionHandler.java**
``` java
context.getLogger().log("CONTENT TYPE: " + contentType);
```
2. Sau dòng code này, thêm đoạn code sau để gọi 2 xử lý - một cho tập tin hình ảnh và một cho các tập tin không phải hình ảnh
``` java
switch ( contentType )
{
	case "application/x-directory":
		System.out.println("application/x-directory detected - ignoring");
		break;

	case "image/jpeg":

		System.out.println("image/jpeg detected");
		InputStream objectData = null;
		objectData = response.getObjectContent();
		handleJPEG(bucket, key, objectData);
		break;

	default:
		handleAllOtherContentTypes(bucket, key);
		break;
}
```
![ChangeCode](../../../images/3/1.png?width=90pc)
Bạn sẽ cần thêm các triển khai của 2 xử lý này. Ví dụ:
``` java
private void handleJPEG(String bucketName, String key, InputStream imageStream) {

	final int THUMBNAIL_WIDTH = 100;
	final int THUMBNAIL_HEIGHT= 100;

	try
	{
		System.out.println("Starting resize process...");

		System.out.println(String.format(
				"Starting resize process for %s/%s of type image/jpg", bucketName, key));

		// Resize the image
		System.out.println("     Reading image stream from S3");
		BufferedImage image = ImageIO.read(imageStream);
		System.out.println("          done");

		final BufferedImage thumbnailImage = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = thumbnailImage.createGraphics();
		graphics2D.setComposite(AlphaComposite.Src);

		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		System.out.println("     Drawing image...");
		graphics2D.drawImage(image, 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);
		System.out.println("          done");
		graphics2D.dispose();

		System.out.println("     Opening output file...");
		File fileThumbnail = new File("/tmp/thumbnail.jpg");
		System.out.println("          done");
		System.out.println("     Writing output file...");
		ImageIO.write(thumbnailImage, "jpg", fileThumbnail);
		System.out.println("          done");

		//
		// Now put the finished object into the /processed subfolder
		// Note that the filename manipulation here is not meant to be
		// production-ready and robust! It will break if files without extensions
		// are uploaded!
		//
		String fileName = key.substring( key.lastIndexOf('/') + 1, key.length() );
		String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
		System.out.println("     Pushing output file to processed folder...");
		s3.putObject(bucketName, "processed/" + fileNameWithoutExtn + ".thumb.jpg", fileThumbnail);
		System.out.println("          done");
	}
	catch(Exception e)
	{
		System.out.println(String.format(
				"Error processing JPEG image from stream for %s/%s", bucketName, key));
		System.out.println(e.getMessage());
	}
	finally
	{
		System.out.println("Ended resize");
	}
}

private void handleAllOtherContentTypes(String bucketName, String key) {

	System.out.println(String.format(
			"%s/%s is an unsupported file type. It will be deleted.", bucketName, key));

	s3.deleteObject(bucketName, key);
	System.out.println("     Done!");
}
```
![ChangeCode](../../../images/3/2.png?width=90pc)
Hai xử lý này nên được thêm vào lớp *LambdaFunctionHandler* như là private method. Bạn nên thêm những đoạn code này vào phần import ở đầu file **LambdaFunctionHandler.java**
Bạn cần thêm vào các import sau:
```java
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
```
![ChangeCode](../../../images/3/3.png?width=90pc)
Hoặc sử dụng Eclipse IDE helper **Source | Organise Imports** để sửa lỗi thiếu class.\
3. Tải lại hàm Lambda sử dụng IDE. Nhấp chuột phải vào project **TestLambda** , chọn **Upload function to AWS Lambda…**, chọn **Choose an existing Lambda function**
![Redeploy](../../../images/3/4.png?width=90pc)
Chọn **Next**
![Redeploy](../../../images/3/5.png?width=90pc)
4. Khi quá trình tải lên thành công, bạn sẽ sẵn sàng để kiểm tra lại các tập tin được tải lên S3 bucket. Đầy tiên, tải ảnh **Puppy.jpg** lên thư mục **uploads** và xác nhận rằng hình ảnh thu nhỏ được tạo và lưu trữ tại thư mục **processed/**. Thư mục **processed/** sẽ được tạo tự động bởi hàm Lambda khi nó tạo các hình ảnh thu nhỏ. 
![TestDeploy](../../../images/3/6.png?width=90pc)
Kiểm tra CloudWatch logs để xem quá trình trên diễn ra như thế nào
![Viewlogs](../../../images/3/7.png?width=90pc)
5. Tải lên một tập tin không phải hình ảnh vào thư mục **uploads/**
![Uploadfile](../../../images/3/8.png?width=90pc)
Ta nhận rằng tập tin vừa tải lên không được xử lý và bị xóa. 
![TestDeploy](../../../images/3/9.png?width=90pc)
Xem CloudWatch log để xem quá trình thực hiện như thế nào.
![Viewlogs](../../../images/3/10.png?width=90pc)

#### Cập nhật quyền để có thể truy cập tập tin đã tải lên

6. Trong S3 bucket đã tải lên, nếu truy cập vào một hình ảnh trong thư mục **processed/** và thử mở tập tin này, bạn sẽ nhận được thông báo **Access Denied**.
![AccessDenied](../../../images/3/11.png?width=90pc)
Lý do là vì S3 bucket policy chưa cho phép người dùng ẩn danh có thể có truyền đọc các tập tin trong bucket.
7. Để khắc phục vấn đề này, chọn thư mục **processed/**, chọn menu **Acctions**, chọn **Make Public**. 
![UpdatePermission](../../../images/3/12.png?width=90pc)
![UpdatePermission](../../../images/3/13.png?width=90pc)
Truy cập lại vào tập tin trên trình duyệt web. Bây giờ ta đã có thể xem được nội dung của tập tin.
![UpdatePermission](../../../images/3/14.png?width=90pc)