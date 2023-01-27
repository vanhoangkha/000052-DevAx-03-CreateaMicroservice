+++
title = "Create and test Lambda function locally"
weight = 1
chapter = false
pre = "<b>2.1. </b>"
+++

#### Create and test Lambda function locally

1. Open **Eclipse IDE** and select **AWS toolkit** symbol, select **New AWS Lambda Java Project…**

![OpenIDE](/images/2/1.png?width=90pc)

2. A **New AWS Lambda Maven Project** dialog box will appear. Set Project name to `TestLambda`, Group ID is `idevelop.lambda` and Artifact ID là `s3handler`. Click **Finish** to create project.

![NewLambdaProject](/images/2/2.png?width=90pc)

3. Chúng ta cần cập nhật tập tin **pom.xml** mà Maven sử dụng lên phiên bản Mockito mới hơn.
We need to update **pom.xml** file that Maven use to a newer version of Mockito.
![UpdatePomfile](/images/2/3.png?width=90pc)

After updating the project dependencies through Maven, run **JUnit Test** by selecting **Run As | JUnit Test**.

![RunJUnitTest](/images/2/4.png?width=90pc)

You will see outpur result of Lambda function, as if it were triggered by a file uploaded to S3. The parameters for the test are provided in the test resource, in the JSON payload form similar to the payload that the Amazon environment will send to Lambda function, when S3 bucket associated with this Lambda function receives the uploaded file.

![RunJUnitTest](/images/2/5.png?width=90pc)

{{% notice tip %}}
You may see some warnings related to the profile name. \
You can ignore this warning in this exercise.
To view the output of the JUnit test, select the JUnit tab
{{% /notice %}}
4. Check S3-event.put.json file. The S3-event.put.json file contains schemas and values that we will use for this lab.

{{% notice tip %}}
You will probably see a warning regarding the missing node. You can safely ignore this warning.
![RunJUnitTest](/images/2/6.png?width=90pc)
{{% /notice %}}

#### Update provided code to handle URL encoded keys

The provided source code doesn't take care of the encoding applied to the key name that is provided in S3 event when it is sent to Lambda function, so if you upload a file to test, and the file contains spaces or punctuation, this string needs to be decoded before use.\
You can solve this problem with the following code.\
5. Add the following code after line 28 in the **LambdaFunctionHandler.java** class in *src/main/java/idevelop.lambda.s3handler*:
```java
try
{
  key = java.net.URLDecoder.decode(key, "UTF-8");
}
catch(Exception ex)
{
  context.getLogger().log("Could not decode URL for keyname... continuing...");
}
```

![CreatePrject](/images/2/7.png?width=90pc)