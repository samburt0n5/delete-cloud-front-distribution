### delete-cloud-front-distribution
Disables and deletes a cloudfront distribution with the given `distributionId`. Complete with eventbridge event trigger that will fire when a specified cloudwatch alarm is triggered.
This app uses the [serverless](https://www.serverless.com/framework/docs/getting-started) framework to deploy the required aws resources to your account and will need to be installed before use.
<br>Note: Ensure an alarm is setup in cloudwatch that is tied to the cloudfront distribution and is named `TooManyRequests`, set the threshold appropriately for your needs.
### Build (Standard gradle build)
```bash
$ ./gradlew build
```

### Deploy (Builds image and deploys to aws)
Note: If running for the first time you may need to build docker image first with `docker build . -t delete-cloud-front-distribution`
```bash
$ sls deploy
```

### Remove
```bash
$ sls remove
```