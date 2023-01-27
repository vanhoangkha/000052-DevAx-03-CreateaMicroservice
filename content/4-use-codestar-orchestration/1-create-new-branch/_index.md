+++
title = "Create new branch"
weight = 1
chapter = false
pre = "<b>4.1. </b>"
+++

Now that you have hands-on experience creating and deploying AWS Lambda functions, it's time to go back to our monolithic TravelBuddy application and deploy a microservice on AWS Lambda managed through a CI/CD pipeline. created with AWS CodeStar.

1. Open AWS Code Star console and click **Create Project**.

![CreateCodeStarproject](/images/4/1.png?width=90pc)

2. We will need to create a service role for the code star the first time we use it. Click **Create service role**. You will see many template tags.

3. Because the program source code is written in Java, we will choose the following options:
- AWS Lambda
- Web services
- Java

Select Java Spring and click **Next**

![CreateCodeStarproject](/images/4/2.png?width=90pc)

4. In Project name section, enter `dev-flight-svc`.
5. Select Code Commit , click **Next** and click **Create Project**.

![CreateCodeStarproject](/images/4/3.png?width=90pc)

6. Select **Team**, then click **Add team member** 

![CreateCodeStarproject](/images/4/4.png?width=90pc)

- Select **awsstudent**.
- Enter email if first time add.
- Select **Owner** permission.
- Select **Remote access**.
- Then click **Add team member**.

![CreateCodeStarproject](/images/4/4a.png?width=90pc)

7. Open Eclipse IDE, find **AWS** symbol and select it to open menu.
8. Select **Import AWS CodeStar Project** 

![ImportProject](/images/4/5.png?width=90pc)

9.  Select **dev-flight-svc** in list and select **dev-flight-svc** in repository.
10. Enter Git credentials. Information about Git credentials can be viewed in the Output section of the AWS Cloudformation of the first stack we created in the preparation phase. Click **Next**.

![ImportProject](/images/4/6.png?width=90pc)

11. Select **master** branch and click **Next**. If an error message appears, click **OK** to close.

![ImportProject](/images/4/7.png?width=90pc)

12. Click **Finish**

![ImportProject](/images/4/8.png?width=90pc)

The IDE will now download the files from the CodeCommit repository.

13. Take a moment to look at the project structure before continuing. The HelloWorld microservice represents a simple handler that returns the Hello World web page when called.
