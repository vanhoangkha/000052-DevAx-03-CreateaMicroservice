+++
title = "Create a Microservice"
date = 2021
weight = 1
chapter = false
+++

# Create a Microservice

In this section, you will learn how to create a AWS Lambda function that trigger when a file is uploaded to the Amazon S3 Bucket. You would upload the source code to the Lambda function through the Eclipse IDE, then manually connect to an S3 event trigger to call the function and view the output log. Then, you will edit code and add functions that allow it to handle different file types - namely generating thumbnails for JPG images and deleting all other file types. Finally, you will create a deployment package and automate the deployment of functions and associated triggers and S3 buckets, using the AWS CLI.

![info](/images/1/info.png?width=50pc)

The final version of AWS Lambda function does the following tasks:
- First, upload a file to S3 bucket.
- Amazon S3 triggers the AWS Lambda function associated with the PutObject action and provides metadata to describe the file.
- The Lambda function check content type of the file and if it isn't a **image/jpeg** file, the file is deleted. If the file is a **image/jpeg** file, the code will generate an image thumbnail and save the thumbnail in another folder in the same bucket.
Then, you will back to *TravelBuddy* web application and deploy the monolithic codebase as standalone microservices, using AWS CodeStar to create CI/CD pipeline.

![Monolithcallingmicro](/images/1/monolithcallingmicro.png?width=90pc)

#### Content

After completing this workshop, you can:
- Use Eclipse IDE to create and deploy an **AWS Lambda function**.
- Edit provided Java Lambda function source code to **resize** images as thumbnails or delete non-image files and connect triggers to S3 buckets for feature testing.
- Use **AWS Serverless Application Model (SAM)** and **AWS CloudFormation** to create template to automate the deployment of a Lambda function, an S3 trigger, and an S3 bucket.
- Defie **microservice candidate** in the monolithic codebase and create an **AWS CodeStar** project to manage the CI/CD pipeline for that microservice hosted in AWS Lambda.

#### Technical knowledge requirements

To complete this workshop, you should be familiar with the AWS Management Console, which uses a text editor to edit commands. 

#### Environment
The following diagram describes the resources deployed in this workshop.

![Diagram](/images/1/0.png?width=50pc)
