+++
title = "Redeployment via CI/CD pipeline"
weight = 2
chapter = false
pre = "<b>4.2. </b>"
+++

In this section, you will update source code in Eclipse, overwriting Hello World! microservice sample and replace it with the code of FlightService. Then, you will commit the code changes to CodeCommit to enable deployment to your AWS Lambda environment and update the Amazon API Gateway configuration to match.

#### Add microservice code to new branch

1. Move the path to the root of the CodeStar Project we just imported. Use the following command to create a new branch:
```bash
cd git\dev-flight-svc
git checkout -b "new-implementation"
```

![Createbranch](/images/4/10.png?width=90pc)

2. Download the program source code **FlightSpecials** and extract it.\
{{%attachments /%}}
3. In Eclipse IDE, right-click on the project. select **Show In** and select **System Explorer**.\
4. Copy the contents of the FlightSpecials folder to the opened folder.
You need to delete the content in the two folders **/src** and **/target** of the sample project before copying over it.
{{%notice warning%}}
If you do not delete the contents of the **/src** and **/target** directories before copying the new code, the build process will fail because we do not configure **HelloWorldController / Handler**.
{{%/notice%}}
5. Right-click on the project root folder, select **Maven | Update Project**, select **OK**.

![Redeploy](/images/4/11.png?width=90pc)

Once done, you will see the following directory structure:

![Redeploy](/images/4/12.png?width=90pc)

6. As part of the microservice implementation, we use VPC and assign a new IAM Role to the Lambda function to allow it to perform different tasks. When CodeStar creates the project, it creates an IAM Role that grants CloudFormation permission to deploy the HelloWorld service. These permissions are not enough for higher requests, so we need to change the policy to assign permissions to CloudFormation to extend the permissions.\
7. Open AWS IAM Console and select **Roles**.\
8. In **Filter** box, enter **CodeStarWorker-dev-flight-svc-CloudFormation** to find IAM Role.\
9. Open IAM role.

![Addpolicy](/images/4/13.png?width=90pc)

- Click **Add permission**, then select **Add inline policy**.

![Addpolicy](/images/4/13a.png?width=90pc)

10. Go to the JSON tab and paste the following code in:
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "iam:GetRole",
        "iam:CreateRole",
        "iam:DeleteRole",
        "iam:PassRole",
        "iam:PutRolePolicy",
        "iam:DeleteRolePolicy",
        "lambda:ListTags",
        "lambda:TagResource",
        "lambda:UntagResource",
        "lambda:AddPermission",
        "ec2:DescribeSecurityGroups",
        "ec2:DescribeSubnets",
        "ec2:DescribeVpcs",
        "ec2:CreateNetworkInterface",
        "ec2:AttachNetworkInterface",
        "ec2:DescribeNetworkInterfaces"
      ],
      "Resource": "*",
      "Effect": "Allow"
    }
  ]
}
```

![Addpolicy](/images/4/14.png?width=90pc)

11. Enter `idevelopCodeStarCloudFormationPolicy` for policy name and click **Create policy**.

![Addpolicy](/images/4/15.png?width=90pc)

When adding the **CodeStarWorker-dev-flight-svc-CloudFormation** role, the permissions in the **idevelopCodeStarCloudFormationPolicy** policy allow CLoudFormation to act on your behalf when implementations change in the environment, including permission permissions Lambda function attached to the VPC where the RDS instance hosting the **TravelBuddy** website is deployed. They also allow CloudFormation to create a new IAM Role that the Lambda function uses to execute.

12. The CloudFormation template template.yml file provided in *FlightSpecials.zip* has several values that need updating before proceeding with deployment. These values include Subnet Ids, Security Group Ids and the RDS Instance Endpoint.

13. Open template.yml file and replace **sg-<REPLACE>** value with Security Group Id of **DBSecurityGroup**.

![DBSecurityGroup](/images/4/16.png?width=90pc)

14. Replace value of **subnet-<REPLACE>** with 2 privateSubnet IDs named **Module3/DevAxNetworkVPC/privateSubnet**

![SubnetID](/images/4/17.png?width=90pc)

15. Replace value of **<RDSEndpoint>** with **RDSEndpoint** that provided in the OUTPUT section of the CloudFormation or the Endpoint in the RDS instance

![RDSEndpoint](/images/4/18.png?width=90pc)

16. When you're done, you'll have a VPCConfig section like this:

![VPCConfig](/images/4/19.png?width=90pc)
