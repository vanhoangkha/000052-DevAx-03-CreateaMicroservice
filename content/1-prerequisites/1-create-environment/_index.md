+++
title = "Preparation"
weight = 1
chapter = false
pre = "<b>1.1. </b>"
+++

#### Preparation
In this workshop, we will use CloudFormation template that provided and pre-install the necessary resource.

1. In this lab, we use **KPforDevAxInstances.PEM** Keypair again which created in the previous exercise for virtual machines. Or you can review the previous lab to see how to create a new keypair.

{{%notice tip%}}
If you create a new keypair, download the .pem file to be able to decrypt the password of the DevAx Windows host.
{{%/notice%}}

2. Download the CloudFormation template file to install the necessary resource for this lab.

{{%attachments /%}}

3. Access to **AWS CloudFormation**

![CloudFormation](/images/1/1.png?width=90pc)

4. Click **Create stack**, select **With new resources (standard)**
5. In **Prerequisite - Prepare template** section, select **Template is ready**
6. In **Template source** section, select **Upload a template file**, click **Choose file** and point to the downloaded template file. - Click **Next**.

![CloudFormation](/images/1/2.png?width=90pc)

7. Enter stack name in **Stack name** section.
8. Select **KPforDevAxInstances** keypair for **EEKeyPair** section and click **Next**

![CloudFormation](/images/1/3.png?width=90pc)

9. Click **Next** at **Configure stack options** page.

![CloudFormation](/images/1/4.png?width=90pc)

10. Check to **I acknowledge that AWS CloudFormation might create IAM resources with customer names.**.

- Click **Create stack**.

![CloudFormation](/images/1/5.png?width=90pc)

11. We need to wait a few minutes for the resources to be initialized and configured.

![Diagram](/images/1/0.png?width=90pc)
