package idevelop.lambda.s3handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public LambdaFunctionHandler() {}

    // Test purpose only.
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        try
        {
          key = java.net.URLDecoder.decode(key, "UTF-8");
        }
        catch(Exception ex)
        {
          context.getLogger().log("Could not decode URL for keyname... continuing...");
        }
        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();
            context.getLogger().log("CONTENT TYPE: " + contentType);
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
            return contentType;
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        }
    }
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

}