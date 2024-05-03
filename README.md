### delete-cloud-front-distribution
Disables and deletes a cloudfront distribution with the given `distributionId`. Complete with eventbridge event trigger that will fire when a specified cloudwatch alarm is triggered.
This app uses the [serverless](https://www.serverless.com/framework/docs/getting-started) framework to deploy the required aws resources to your account and will need to be installed before use.
<br>Note: Ensure an alarm is setup in cloudwatch that is tied to the cloudfront distribution and is named `TooManyRequests`, set the threshold appropriately for your needs.
### Build (Standard gradle build)
```bash
$ ./gradlew build
```

### Deploy (Deploys jar under build/libs)
```bash
$ sls deploy
```

### Remove
```bash
$ sls remove
```

### Invoke (Optional: Manually run the func, replace <distributionId>)
```bash
$ sls invoke --function delete-cloud-front-disribution --data '{"queryStringParameters": {"distributionId": "<distributionId>"}}'
```