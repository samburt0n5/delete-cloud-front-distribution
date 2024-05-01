FROM public.ecr.aws/lambda/java:17.2023.07.19.04

# Expose the application port
EXPOSE 8080

# Copy build files
COPY build/classes/kotlin/main ${LAMBDA_TASK_ROOT}
COPY build/dependency/* ${LAMBDA_TASK_ROOT}/lib/

# Run
CMD [ "MainKt::handleRequest" ]