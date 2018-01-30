export VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)
GROUP_NAME="csye6225-fall2017-webapp"
GROUP_DESC="Assignment4_security_group"
aws ec2 create-security-group --group-name $GROUP_NAME --description $GROUP_DESC --vpc-id $VPC_ID
aws ec2 authorize-security-group-ingress --group-name $GROUP_NAME --protocol tcp --port 22 --cidr 172.31.0.0/16
aws ec2 authorize-security-group-ingress --group-name $GROUP_NAME --protocol tcp --port 80 --cidr 172.31.0.0/16
aws ec2 authorize-security-group-ingress --group-name $GROUP_NAME --protocol tcp --port 443 --cidr 172.31.0.0/16

AMI_ID="ami-cd0f5cb6"
KEY_PAIR="id_rsa"
export SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)


INSTANCE_ID=$(aws ec2 run-instances --image-id $AMI_ID --count 1 --instance-type t2.micro --disable-api-termination --key-name $KEY_PAIR --security-groups $GROUP_NAME --block-device-mappings '[{"DeviceName":"/dev/sda1","Ebs":{"VolumeSize":16,"DeleteOnTermination":true,"VolumeType":"gp2"}}]' --query 'Instances[0].InstanceId' --output text)

aws ec2 wait instance-running --instance-ids $INSTANCE_ID
aws ec2 describe-instance-status --instance-ids $INSTANCE_ID

PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID --query "Reservations[0].Instances[0].PublicIpAddress" --output text)
DOMAIN_NAME="ec2.csye6225-fall2017-harshea.me."

aws route53 change-resource-record-sets --hosted-zone-id Z3UNR4ODL4PQ5F --change-batch "{\"Comment\": \"DNS name for my instance.\", \"Changes\":[{\"Action\": \"UPSERT\", \"ResourceRecordSet\": { \"Name\": \""$DOMAIN_NAME"\", \"Type\": \"A\", \"TTL\": 60, \"ResourceRecords\": [{\"Value\": \""$PUBLIC_IP"\"}]}}]}"