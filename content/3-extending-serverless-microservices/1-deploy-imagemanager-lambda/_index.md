+++
title = "Implement Lambda ImageManager function"
weight = 1
chapter = false
pre = "<b>3.1. </b>"
+++

**Content**
- [Implement Lambda ImageManager function](#implement-lambda-imagemanager-function)
- [Fix error](#fix-error)
- [Updated permissions to be able to access uploaded files](#update-policy)

#### Implement Lambda ImageManager function

You will edit the code in **handleRequest** in the file **LambdaFunctionHandler.java** so that files with **contentType** other than **image/jpeg** will be deleted by the Lambda function. This simulates a situation where if a file is uploaded with the wrong content type, the file will be deleted and not processed. If the file is **image/jpeg**, the Lambda function will shrink the images and pass the thumbnails to a target bucket so the application can use them.

1. First, find the following code line in **LambdaFunctionHandler.java** file.
``` java
context.getLogger().log("CONTENT TYPE: " + contentType);
```
2. After this code line, add following code to call 2 handlers - one for image files and one for non-image files.
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

![ChangeCode](/images/3/1.png?width=90pc)

You will need additional implementations of these 2 handlers:
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

![ChangeCode](/images/3/2.png?width=90pc)

These two handles should be added to the *LambdaFunctionHandler* class as private method. You should add these codes to the import section at the top of the **LambdaFunctionHandler.java** file.
You need to add the following imports:
```java
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
```

![ChangeCode](/images/3/3.png?width=90pc)

{{%notice tip%}}
If you have difficulty, you can refer to the sample **LambdaFunctionHandler.java** file below.
{{%/notice%}}

{{%attachments /%}}

3. Nhấp chuột phải bên trong cửa sổ chứa mã nguồn của **LambdaFunctionHandler.java** class trong src/main/java/idevelop.lambda.s3handler. Từ menu **AWS Lambda**, chọn **Upload function to AWS Lambda**. Chọn **Choose an existing Lambda function**

![Redeploy](/images/3/4.png?width=90pc)

- Click **Next**. Click **Finish**.

![Redeploy](/images/3/5.png?width=90pc)

{{%notice warning%}}
If you encounter an error at this step, you can refer to the fixing error section below.
{{%/notice%}}

4. Once the upload is successful, you'll be ready to check the files that are uploaded to the S3 bucket. First, upload **Puppy.jpg** to **uploads** folder and confirm that the thumbnail image is created and stored in the **processed/** folder. The **processed/** folder will be created automatically by the Lambda function as it generates the thumbnail images. 

![TestDeploy](/images/3/6.png?width=90pc)

- Check CloudWatch logs to see how the above process works.

![Viewlogs](/images/3/7.png?width=90pc)

5. Upload a non-image file to **uploads/** folder.

![Uploadfile](/images/3/8.png?width=90pc)

- We get that the uploaded file was not processed and deleted.

![TestDeploy](/images/3/9.png?width=90pc)

- View the CloudWatch log to see how it's doing.

![Viewlogs](/images/3/10.png?width=90pc)

#### Fix error

1. If you get the following error when uploading the code to the Lambda function, press **OK**.

![UploadError](/images/3/30.png?width=90pc)

2. We will manually update the code file from S3 for the Lambda function through the console.

3. Open the AWS Lambda console and select **TestLambda** function.
- In **Code** tab, click **Upload from**, then select **Amazon S3 location**.

![UploadError](/images/3/28.png?width=90pc)

4. Get the path of the **TestLambda.zip** file in the S3 console.

![UploadError](/images/3/29.png?width=90pc)

5. Paste in the following dialog, then click **Save**

![UploadError](/images/3/31.png?width=60pc)

#### Updated permissions to be able to access uploaded files

1. The uploaded image in S3 bucket, if you access an image in the **processed/** folder and try to open the file, you will get the message **Access Denied**.

![AccessDenied](/images/3/11.png?width=90pc)

The reason is that the S3 bucket policy does not yet allow anonymous users to have read and write access to files in the bucket.

2. To fix this problem, select the **processed/** folder, select the **Acctions** menu, select **Make public using ACL**.

![UpdatePermission](/images/3/12.png?width=90pc)

- Click **Make public**

![UpdatePermission](/images/3/13.png?width=90pc)

- Access the file again in the web browser. Now we can see the contents of the file.

![UpdatePermission](/images/3/14.png?width=90pc)