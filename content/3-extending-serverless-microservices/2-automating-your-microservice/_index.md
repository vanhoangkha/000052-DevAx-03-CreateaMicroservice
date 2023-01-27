+++
title = "Serverless Microservices Automation"
weight = 2
chapter = false
pre = "<b>3.2. </b>"
+++

In the previous section, you used the IDE Eclipse to create and update a Lambda function using AWS Toolkit for Eclipse. This allow you manually initiate the auto-upload of your Lambda function. However, this mechanism may not be convenient for automating deployment steps for functions or coordinating deployment and updates for other elements of a serverless application, such as event sources and downstream resources. For example, the Eclipse IDE does not give you the ability to deploy and update an S3 bucket and connect an S3 PUT OBJECT trigger, along with your Lambda function as a deployment unit. 

You can use **AWS CloudFormation** to easily specify, deploy, and configure serverless applications. AWS CloudFormation is a service that helps you model and set up Amazon Web Services resources so you can spend less time managing them and more time focusing on running applications in your AWS. You create a template (**Template**) that describes all the AWS resources you want (like Lambda functions and S3 buckets) and AWS CloudFormation takes care of provisioning and configuring those resources for you.

Alternatively, you can use **AWS Serverless Application Model (SAM)** to expose resources including serverless applications. These resource types, such as AWS Lambda functions and APIs, are fully supported by AWS CloudFormation and make it easier for you to define and deploy your serverless application.

In this exercise, you will use **AWS CLI** and **AWS CloudFormation/SAM** to package the application and deploy it from scratch without having to create or configure any dependencies. manually.

1. Right click the **TestLambda** project in panel Project Explorer. Select **Create New File**. Name the File Name as **template.yaml**. This file will be at the same level as the **pom.xml** file of the **TestLambda** function in the previous section. Then we edit the content of the file **template.yaml** as below.

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

![Creattemplate](/images/3/15.png?width=90pc)

This template describes an S3 bucket that will fire our Lambda function whenever a file is uploaded to the **uploads** folder - similar to the S3 bucket we created in the previous section.

{{%notice tip%}}
We will need to install maven , you can download and extract the file containing maven below.
Then proceed to configure the environment variable MVN_HOME to **unpacked path\bin**. Example: **C:\apache-maven-3.8.1\bin**
{{%/notice%}}

**File Apache Maven:**
{{%attachments /%}}

![MavenInstall](/images/3/maven.png?width=90pc)

2. Create an implementation artifact for the Lambda function - a **JAR** file containing the Lambda function and all its dependencies. We can use the command line to do this.
```bash
mvn package shade:shade -DskipTests=true
```

![CreateJarfile](/images/3/16.png?width=90pc)

The result returns a file named **s3handler-1.0.0.jar** in the directory **target**

![UploadJarfile](/images/3/17.png?width=90pc)

3. Next, we'll use the AWS CLI to push this file to the S3 bucket where it can be deployed. We will use the command **aws cloudformation package**
```bash
aws cloudformation package --template-file template.yaml --s3-bucket <YOUR_CODE_BUCKET_NAME> --output-template deploy-template.yaml --profile aws-lab-env
```

![PushfiletoS3](/images/3/18.png?width=90pc)

**Note:** Nếu bạn thấy một thông báo lỗi **‘NoneType’ object has no attribute ‘items’** hãy kiểm tra lại format của tập tin YAML.

The **aws cloudformation package** command will take the provisioned AWS SAM sample and rewrite it in the definition of the artifact that is automatically uploaded to the S3 bucket. In this case,**deploy-template.yaml** is generated and contains the value **CodeUri** which points to the deployment zip file in Amazon S3. This sample represents your serverless application.

4. You are now ready to deploy the JAR file as a Lambda function and connect the S3 trigger to a new S3 bucket. You will notice the output from the previous command that instructs us on what to run to deploy the encapsulation pattern. In the command line window, copy the command and paste it back into the command line. Change the <YOUR_STACK_NAME> value to ImageManagerDemo and add the --profile aws-lab-env option to allow CloudFormation to create the IAM role on your behalf. Your command will look like this:
```bash
aws cloudformation deploy --template-file deploy-template.yaml --stack-name ImageManagerDemo --profile aws-lab-env
```

![DeployCF](/images/3/19.png?width=90pc)

When you run the **aws cloudformation deploy** command it creates an **AWS CloudFormation ChangeSet** and deploys them, here is a list of changes to the AWS CloudFormation stack. Some stack templates can include resources that affect permissions in an AWS account, such as by creating a new AWS Identity and Access Management (IAM) user. For such stacks, you must acknowledge its capabilities by specifying the -capabilities parameter. For more information, see CreateChangeSet in the AWS CloudFormation API Reference.

{{% notice tip %}}
You can view the resource creation process directly in the CloudFormation console
{{% /notice %}}

To check the results, open the AWS CloudFormation console to see the latest stack created and go to the Lambda console to view your function. The CloudFormation template creates an S3 Bucket named **idevelop-imagemanager-<YOUR_ACCOUNT_ID>** where YOUR_ACCOUNT_ID is the AWS account ID used for this exercise environment.

![Createstack](/images/3/20.png?width=90pc)

The template also creates a new Lambda function named ImageManagerDemo-TestLambda-XXXXXX where XXXXXX is a random identifier allocated by CloudFormation to ensure the uniqueness of the function name.

5. Once the CloudFormation stack deploys successfully, you're ready to test your function. Using the AWS S3 console, create a folder named **uploads** in the S3 bucket **idevelop-imagemanager-<YOUR_ACCOUNT_ID>**

![Createfolder](/images/3/21.png?width=90pc)

6. Upload a file to this folder. You can use **puppy.jpg** or any other image file you have. Or you can upload a non-image file to test both cases.
If you upload a file other than an image, the file will be deleted.

![Uploadfile](/images/3/22.png?width=90pc)

7. If you uploaded an image, check the **processed** folder in the S3 bucket. You will see a thumbnail of the JPG file in this folde

![UploadImage](/images/3/23.png?width=90pc)

8. View the log in CloudWatch logs of **ImageManagerDemo-TestLambda-XXXXXX** function.

![Viewlog](/images/3/24.png?width=90pc)