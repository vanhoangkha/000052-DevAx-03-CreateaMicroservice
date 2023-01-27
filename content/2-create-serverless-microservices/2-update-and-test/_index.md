+++
title = "Upload and test Lambda function in AWS Lambda"
weight = 2
chapter = false
pre = "<b>2.2. </b>"
+++

#### Upload and test Lambda function in AWS Lambda

1. We will deploy Lambda functions and test them.

- Right-click inside the window containing the source code of the **LambdaFunctionHandler.java** class in src/main/java/idevelop.lambda.s3handler.
- From **AWS Lambda** menu, select **Run function on AWS Lambda**…

![UpdateLambda](/images/2/8.png?width=90pc)

2. Select **Upload now**.

![UpdateLambda](/images/2/9.png?width=90pc)

3. Select the region where the lab is being performed and select **Create a new Lambda function**. Enter Lambda function name as `TestLambda`.
4. Select **Next**

![UpdateLambda](/images/2/10.png?width=90pc)

5. In description section, enter `Test AWS Lambda function triggered by S3 upload`
6. In **IAM role** section, select **LambdaRole** automatically generated in the exercise configuration.
7. In **S3 bucket** section, select the S3 bucket we created earlier to store the Lambda source code (**idevelop-sourcecode-yourinitials**).
8. Select **Finish** to upload Lambda function to AWS Account. 

![UpdateLambda](/images/2/11.png?width=90pc)

9. Uploading will take a few minutes. Once the upload is complete, open the AWS Lambda Console and find the **TestLambda** function that was just uploaded.

![TestTestLambda](/images/2/12.png?width=90pc)

10. Find **Add Triggers** in **Function overview** section.

![UpdateLambda](/images/2/13.png?width=90pc)

11. Select S3.
12. In **Bucket** section, select the bucket that you created to contain the uploaded image (idevelop-imagemanager-yourinitials)
13. In **Event type** section, select **All Object create events**
14. In **Prefix** section, enter `uploads/`. Don't set value for **Suffix**.
15. Check at **Recursive invocation** and select **Add**

![UpdateLambda](/images/2/14.png?width=90pc)

16. Now let's test the Lambda function. Go to **S3 console** and open the folder containing the uploaded images (idevelop-imagemanager-initials).

17. Create a **uploads** folder. Open **uploads** folder just created and click **Upload** to upload files to it.

{{%attachments /%}}

![Createfolder](/images/2/15.png?width=90pc)

- Click **Add files**
- Select downloaded files and click **Upload**

![Uploadimage](/images/2/16.png?width=90pc)

18. Once the upload is complete, back in **AWS Lambda console**, select the **Monitoring** tab of the **TestLambda** function. You can adjust the time frame for easier viewing.

![Viewlog](/images/2/17.png?width=90pc)

19. You will see 2 calls and their duration in the graph. You see 2 calls - one to create the **uploads** folder and the other to upload an image **Puppy.jpg**
20. Click **View logs in CloudWatch** to open the CloudWatch logs of this Lambda function. 

![Viewlog](/images/2/18.png?width=90pc)

Note: you will see a different **CONTENT TYPE** output on these two events. In the first event the CONTENT TYPE is *application/x-directory* and in the second event it is *image/jpeg*. In the sample source code of the provided Lambda function, only logs the CONTENT TYPE of the file, and no other action is taken. We will change this code in the next sections.

20.  Upload any file that is not an image to the S3 bucket and observe the log.

![Uploadfile](/images/2/19.png?width=90pc)

![Uploadfile](/images/2/20.png?width=90pc)