#!/bin/sh
# Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
# Monas Bhar, 001232781, bhar.m@husky.neu.edu
# Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
# Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu

TEMPLATE_FILE=$1

TEMPLATE_FILE_REALPATH=`realpath $TEMPLATE_FILE`

echo "Please enter Stack name"
read STACK_NAME

ImgId="ami-cd0f5cb6"

InstType="t2.micro"

echo "Please enter the volume size"
read volumeSize

echo "Please enter the volume type"
read volumeType

echo "Please enter the public key name"
read keyName

securityGroupName=csye6225-fall2017-$STACK_NAME-webapp

echo "Please enter the hosted Zone Id"
read hostedZoneId

echo "Please enter the dns name"
read originaldnsName
dnsName=$STACK_NAME.$originaldnsName
csye='csye.com'
bucketName=$originaldnsName$csye

originalDomain=$originaldnsName

echo "Please enter the RecordSet Type"
read recordSetType

echo "Please enter the RecordSeT Time to Live"
read recordSetTTL

echo "Please enter the Certificate ARN number"
read certificateArn

VpcId=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

# Use the VPC ID to get subnet id
export SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VpcId" --query "Subnets[0].SubnetId" --output text)
echo $SUBNET_ID

export SUBNET_ID_1=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VpcId" --query "Subnets[1].SubnetId" --output text)
echo $SUBNET_ID_1

echo $bucketName

aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://$TEMPLATE_FILE_REALPATH --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM --parameters ParameterKey=ImgId,ParameterValue=$ImgId ParameterKey=VpcId,ParameterValue=$VpcId ParameterKey=InstType,ParameterValue=$InstType ParameterKey=volumeSize,ParameterValue=$volumeSize ParameterKey=volumeType,ParameterValue=$volumeType ParameterKey=keyName,ParameterValue=$keyName ParameterKey=securityGroupName,ParameterValue=$securityGroupName ParameterKey=hostedZoneId,ParameterValue=$hostedZoneId ParameterKey=dnsName,ParameterValue=$dnsName ParameterKey=recordSetType,ParameterValue=$recordSetType ParameterKey=recordSetTTL,ParameterValue=$recordSetTTL ParameterKey=bucketName,ParameterValue=$bucketName ParameterKey=subnetID,ParameterValue=$SUBNET_ID ParameterKey=subnetID1,ParameterValue=$SUBNET_ID_1 ParameterKey=originalDomain,ParameterValue=$originalDomain ParameterKey=CertificateArnNumber,ParameterValue=$certificateArn
