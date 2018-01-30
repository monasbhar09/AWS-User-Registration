gcloud sql instances create instance-rds-1 --tier=db-g1-medium --region=us-east1
while true; do
InstanceStatus=`gcloud sql instances describe instance-rds-2 | grep RUNNABLE`
echo $InstanceStatus
if [[ "$InstanceStatus" == "state: RUNNABLE" ]];
then
break
fi
done
gcloud sql databases create mySchema --instance=instance-rds-1 --assign-ip
cloud sql users set-password root % --instance instance-rds-1 --password=root_access
gcloud sql users create user1 % --instance instance-rds-1 --password=rootPassword

