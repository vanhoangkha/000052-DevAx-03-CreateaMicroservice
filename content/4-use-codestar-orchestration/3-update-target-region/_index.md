+++
title = "update target region of API"
weight = 3
chapter = false
pre = "<b>4.3. </b>"
+++

The **swagger.yml** file defines the API to be used by the microservice through the Amazon API Gateway. They need to be updated with AWS Account ID details and AWS Region target before deploying the microservice.

1. In Eclipse IDE, open **swagger.yml** template.
2. Find and replace **<REGION>** with region being used in the lab.
3. Find and replace **<ACCOUNTID>** with your AWS Account Id.

![UpdateSwaggerFile](/images/4/20.png?width=90pc)

4. Save file.
5. Use command line to add changes to **new-implementation** git branch and commit.

Before that you will need to configure email and username for git.
```
C:\Users\Administrator\git\dev-flight-svc>git config --global user.email "youremail"
C:\Users\Administrator\git\dev-flight-svc>git config --global user.name "awsstudent"
```

![Gitcommand](/images/4/21.png?width=90pc)

6. To push changes to CodeCommit, in Eclipse, right-click the project root and select **Teams | Push to origin…**

![PushCode](/images/4/22.png?width=90pc)

You need to push from Eclipse because git credentials are embedded in the Eclipse environment. You can also use the command line, but in this lab, we will not use this method.
It will take some time to push the code and start deploying. Take some time to look at the structure of the project in the Eclipse IDE, especially the buildspec.yml, swagger.yml, and template.yml files that define the Amazon API Gateway, the AWS Lambda Function, and the build/deployment process. through AWS CodeBuild.
You can check **status** of the deployment via **Pipeline** in the CodeStar project dashboard.
{{% notice note %}}
Since our Lambda function uses **DBSecurityGroup**, we need to allow **DBSecurityGroup** to be able to connect to the RDS Endpoint by further configuration as below:
![ConfigureSG](/images/4/23.png?width=90pc)
{{% /notice %}}

7. When the deployment is complete, access the web application by selecting **View application**.

![ViewApplication](/images/4/24.png?width=90pc)

When the page is opened, you will see an error message **{"message":"Missing Authentication Token"}**. This happens because you are trying to access the root of the API, rather than a specific microservice. Edit the URL, add **flightspecials** to the end of the URL, we get a new URL of the form **https://xxxxxxx.execute-api.us-east-1.amazonaws.com/Prod/flightspecials**.
Press Enter, we get the following result:

![ViewApplication](/images/4/25.png?width=90pc)
