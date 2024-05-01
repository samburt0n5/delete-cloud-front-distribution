import aws.sdk.kotlin.services.cloudfront.CloudFrontClient
import aws.sdk.kotlin.services.cloudfront.model.DeleteDistributionRequest
import aws.sdk.kotlin.services.cloudfront.model.GetDistributionRequest
import aws.sdk.kotlin.services.cloudfront.model.UpdateDistributionRequest
import aws.sdk.kotlin.services.cloudfront.waiters.waitUntilDistributionDeployed
import aws.smithy.kotlin.runtime.retries.getOrThrow
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import kotlinx.coroutines.runBlocking

fun handleRequest(request: APIGatewayV2HTTPEvent, context: Context) {
    val distributionId = request.queryStringParameters["distributionId"]!!
    println("distributionId: $distributionId")
    deleteCloudFrontDistribution(distributionId)
}

fun deleteCloudFrontDistribution(distributionId: String) {
    CloudFrontConnector().getCloudFrontClient().use { cloudFrontClient ->
        val response = runBlocking {
            cloudFrontClient.getDistribution(GetDistributionRequest { id = distributionId })
        }

        runBlocking {
            cloudFrontClient.updateDistribution(
                UpdateDistributionRequest {
                    id = distributionId
                    distributionConfig = response.distribution?.distributionConfig?.copy { enabled = false }
                    ifMatch = response.eTag
                }
            )
        }

        println("Distribution $distributionId is DISABLED, waiting for deployment before deleting ...")

        val updatedDistributionResponse = runBlocking {
            cloudFrontClient.waitUntilDistributionDeployed(
                GetDistributionRequest {
                    id = distributionId
                }
            ).getOrThrow()
        }

        println("Distribution $distributionId is deployed")

        runBlocking {
            cloudFrontClient
                .deleteDistribution(
                    DeleteDistributionRequest {
                        id = distributionId
                        ifMatch = updatedDistributionResponse.eTag
                    }
                )
        }
        println("Distribution $distributionId DELETED")
    }
}

class CloudFrontConnector {
    fun getCloudFrontClient(): CloudFrontClient {
        val region1 = "us-east-1"
        //println("access key is $accessKeyID \n")
        return runBlocking {
            CloudFrontClient.fromEnvironment {
                region = region1
            }
        }
    }
}