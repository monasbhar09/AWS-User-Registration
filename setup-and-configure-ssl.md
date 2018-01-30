# csye6225-fall2017
Team Members Information:
Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
Monas Bhar, 001232781, bhar.m@husky.neu.edu
Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu

# Steps to create and configure SSL certificate
## There are many ways you can configure SSl for thesecurity of your website
- NameCheap
- Lets Encrypt
- AWS Certificate manager

## we have used AWS CErtificate Manager to get our SSL Certificate 

## Steps to follow
- Go to AWS Certificate Manager
- Request a certificate
- Add domain name
- Select a validation method
- use DNS validation method
- Create the record set with type CNAME in your Route53 with the value provided by AWS
- Review
- Validate

## After creation
- Use the ARN provided by the Certificate Manager in the cloud formation template
