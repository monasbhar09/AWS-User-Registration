#!/bin/bash

# gcloud  instance template
#gcloud compute instance-templates create instance-template-1 --machine-type=f1-micro --region=us-east1 --image=ubuntu-1604-xenial-v20171208 --image-project=ubuntu-os-cloud --boot-disk-size=16 --boot-disk-type=pd-standard --boot-disk-device-name=instance-1 --tags=sec-web

#Create managed group of instances
#gcloud compute instance-groups managed create managed-instance-group-1 --base-instance-name=instance-1 --size 3 --project=csye-6225-webapp --template=instance-template-1 --description "sudo apt-get install python -y\u000asudo apt-get install tomcat8 -y"

# Wait condition till instances are up and running
#gcloud compute instance-groups managed wait-until-stable managed-instance-group-1 --region=us-east1

#Create a security group
#gcloud compute --project=csye-6225-webapp firewall-rules create webserver-sec --allow tcp:80 --target-tags=sec-web

# Get instance list from managed instance group
#gcloud compute instance-groups list-instances managed-instance-group-1 --region=us-east1

# Creating first instance
gcloud compute instances create instance1 --image-family ubuntu-1604-lts --image-project ubuntu-os-cloud --zone us-east1-b --tags instance-tag --metadata startup-script="#! /bin/bash
sudo apt-get update
sudo apt-get install apache2 -y
sudo service apache2 restart
echo '<!doctype html><html><body><h1>instance1</h1></body></html>' | tee /var/www/html/index.html
EOF"

# Creating second instance
gcloud compute instances create instance2 --image-family ubuntu-1604-lts --image-project ubuntu-os-cloud --zone us-east1-b --tags instance-tag --metadata startup-script="#! /bin/bash
sudo apt-get update
sudo apt-get install apache2 -y
sudo service apache2 restart
echo '<!doctype html><html><body><h1>instance2</h1></body></html>' | tee /var/www/html/index.html
EOF"

# Creating third instance
gcloud compute instances create instance3 --image-family ubuntu-1604-lts --image-project ubuntu-os-cloud --zone us-east1-b --tags instance-tag --metadata startup-script="#! /bin/bash
sudo apt-get update
sudo apt-get install apache2 -y
sudo service apache2 restart
echo '<!doctype html><html><body><h1>instance3</h1></body></html>' | tee /var/www/html/index.html
EOF"

# Create Firewall rules
gcloud compute firewall-rules create myfirewall --target-tags instance-tag --allow tcp:80,tcp:8080,tcp:22,tcp:443

# Create static ip for load balancer
gcloud compute addresses create loadbalancer --region us-east1

# Set health checker for instance group
gcloud compute http-health-checks create basic-check

# Create target pool for managed instance group
gcloud compute target-pools create instance-pool --region us-east1 --http-health-check basic-check

# Append instance group to load balancer
gcloud compute target-pools add-instances instance-pool --instances instance1,instance2,instance3 --instances-zone us-east1-b

# Forwarding rule for lb
gcloud compute forwarding-rules create myrule --region us-east1 --ports 80 --address loadbalancer --target-pool instance-pool

#Create Bucket
gsutil mb -p "csye6225-final" -c "regional" -l "us-east1" gs://"csye6225-final-bucket"


#create bigtable
gcloud beta bigtable instances create instance-bigtable-1 --cluster=csye6225-fall2017-cluster --cluster-zone=us-east1-b --description=test --cluster-num-nodes=3

#create rds instance
gcloud sql instances create sql-instance-11 --tier db-f1-micro --region us-east1
while true; do
InstanceStatus=`gcloud sql instances describe sql-instance-11 | grep RUNNABLE`
echo $InstanceStatus
if [[ "$InstanceStatus" == "state: RUNNABLE" ]];
then
break
fi
done
gcloud sql databases create mySchema --instance sql-instance-11
gcloud sql users set-password root % --instance sql-instance-11 --password=root_access
gcloud sql users create user1 % --instance sql-instance-11 --password=rootPassword

#Getting Static IP for Load Balancer
TempStaticIP=`gcloud compute forwarding-rules describe myrule --region us-east1 | grep IPAddress`
endIndex=`expr ${#TempStaticIP} - 1`
StaticIP=`echo $TempStaticIP | cut -c 12-$endIndex`

#Create a MANAGED ZONE
gcloud dns managed-zones create csye6225zone --dns-name csye6225-fall2017-bharm.me. --description zonedescription

#Starting Transaction
gcloud dns record-sets transaction start -z=csye6225zone

#Create Resource Record Set
gcloud dns record-sets transaction add --zone csye6225zone --name=csye6225-fall2017-bharm.me. --ttl=60 --type=A $StaticIP

#Executing a transaction
gcloud dns record-sets transaction execute -z=csye6225zone


#Create SNS Topic
gcloud beta pubsub topics create csye6225

#Create Google Cloud Function
#gcloud beta functions deploy helloWorld --stage-bucket csye6225-fall2017-gcp-final --trigger-http --trigger-topic=csye6225
gcloud beta functions deploy helloWorld --stage-bucket csye6225-final-bucket --trigger-http
