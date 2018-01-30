#!/bin/bash
# Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
# Monas Bhar, 001232781, bhar.m@husky.neu.edu
# Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
# Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu

echo Please enter instance ID:
read INSTANCE_ID

aws ec2 modify-instance-attribute --instance-id $INSTANCE_ID --no-disable-api-termination

aws ec2 terminate-instances --instance-ids $INSTANCE_ID

aws ec2 wait instance-terminated --instance-ids $INSTANCE_ID

GROUP_NAME="csye6225-fall2017-webapp"
aws ec2 delete-security-group --group-name $GROUP_NAME
