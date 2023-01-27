+++
title = "Challenge - Expose microservice API"
date = 2023
weight = 5
chapter = false
pre = "<b>5. </b>"
+++

In this section, we will perform the same tasks as part 3 when you expose FlightSpecials microservice, but this time with HotelSpecials microservice.
There is no data sharing between FlightSpecials and HotelSpecials services so both qualify for individual microservices with separate data warehouses.  In this lab, to save time, HotelSpecials microservice data can be served from the same RDS database as FlightSpecials, however note that in a real environment you need to separate the data into different repositories.
Expose both FlightSpecials and HotelSpecials services as separate APIs through Amazon API Gateway, by modifying the swagger.yaml API definition and the CloudFormation/SAM template.yml definition

1. Copy **HotelSpecial.java** file to **.../src/main/java/devlounge/model**
{{%attachments /%}}

![Challenge](/images/5/1.png?width=90pc)

2. Copy from line 10 to line 66 of the swagger.yaml file you downloaded into the swagger.yaml file in your project.
- Update your region and account id for uri.

![Challenge](/images/5/2.png?width=90pc)


3. Open the template.yml file and add the following resources from the template.yml file you downloaded.
- In the **Parameters** section, copy the existing parameters.
- In the **Resources | . section RestAPI**, copy the line **LambdaHotelSpecials: !Ref GetHotelSpecials**
- Copy line 48 to line 76.
- Copy line 159 to the end.

![Challenge](/images/5/3.png?width=90pc)

![Challenge](/images/5/4.png?width=90pc)

4. Re-deploy and check the results

![Challenge](/images/5/5.png?width=90pc)

#### Optional Advanced Exercise

5. Modify the source code from part 3 to detect different file types and process them optionally (for example, block and move them to another directory)
6. Create a new Lambda function that executes tasks once a minute. Tasks can be as simple as logging output to CloudWatch Logs.