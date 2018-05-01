# User task recorder
Team Members Information:
Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
Monas Bhar, 001232781, bhar.m@husky.neu.edu
Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu

# Deployment
- [x] Build the war Gradle file in the application
- [] Configure the Tomcat Server ny adding the generated Gradle war file
-[] Run the application

# Make Unauthenticated HTTP Request

Execute following command on your bash shell
``` bash
$ curl http://localhost:8080
```

## Expected Response:
```
{"message":"you are not logged in!!!"}
```

# Authenticate for HTTP Request

Execute following command on your bash shell
``` bash
$ curl -u user:password http://localhost:8080
```

where *user* is the username and *password* is the password.

## Expected Response:
 ```
 {"message":"you are logged in. current time is Tue Sep 19 20:03:49 EDT 2017"}

## Register new user
 ```Execute following command on your bash shell
``` bash
$ curl -u user:password http://localhost:8080/user/register
Enter the new username and password

## Expected Response
```
Checks if user already exists for the new username provided

{"message":"User already exists."}

{"message":User added successfully"}




