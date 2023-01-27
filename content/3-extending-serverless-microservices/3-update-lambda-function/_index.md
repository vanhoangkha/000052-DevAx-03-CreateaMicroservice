+++
title = "(Optional) Update Lambda permission"
weight = 3
chapter = false
pre = "<b>3.3. </b>"
+++

If you look at the trigger in the Lambda function, you'll see that the S3 trigger doesn't appear even though the function is running as expected. Because the Lambda function doesn't know about the S3 bucket trigger - S3 knows about the Lambda function that it fires when an event occurs but not otherwise.

![Trigger](/images/3/25.png?width=90pc)

1. To get the S3 trigger to appear in AWS Lambda, use the following command:
```bash
aws lambda add-permission --function-name <REPLACE_LAMBDA_FUNCTION_NAME> --region <REPLACE_REGION> --statement-id PolicyDocument --action "lambda:InvokeFunction" --principal s3.amazonaws.com --source-arn arn:aws:s3:::<REPLACE_S3_BUCKET_NAME> --source-account <REPLACE_AWS_ACCOUNT_ID> --profile aws-lab-env
```

![Trigger](/images/3/26.png?width=90pc)

Make sure you have replaced the items in brackets with information from the practice environment.
Go back to the AWS Lambda console, you should see the S3 trigger appear.

![Trigger](/images/3/27.png?width=90pc)