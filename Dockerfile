FROM amazoncorretto:17-alpine

# Default payara ports to expose
EXPOSE 6900 8080

# Configure environment variables
ENV PAYARA_HOME=/opt/payara\
    DEPLOY_DIR=/opt/payara/deployments\
    OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317/

# Create and set the Payara user and working directory owned by the new user
RUN addgroup payara && \
    adduser -D -h ${PAYARA_HOME} -H -s /bin/bash payara -G payara && \
    echo payara:payara | chpasswd && \
    mkdir -p ${DEPLOY_DIR} && \
    chown -R payara:payara ${PAYARA_HOME}
USER payara
WORKDIR ${PAYARA_HOME}

# Install and configure opentelemetry agent
RUN wget --no-verbose -O ${PAYARA_HOME}/opentelemetry-agent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
# RUN wget --no-verbose -O ${PAYARA_HOME}/opentelemetry-agent.jar https://github.com/aws-observability/aws-otel-java-instrumentation/releases/latest/download/aws-opentelemetry-agent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:${PAYARA_HOME}/opentelemetry-agent.jar

# Download specific version of Payara
ARG PAYARA_VERSION="6.2024.2"
ENV PAYARA_VERSION="$PAYARA_VERSION"
RUN wget --no-verbose -O ${PAYARA_HOME}/payara-micro.jar https://repo1.maven.org/maven2/fish/payara/extras/payara-micro/${PAYARA_VERSION}/payara-micro-${PAYARA_VERSION}.jar

# Copy in the war
COPY target/experiments.war ${DEPLOY_DIR}/

# Default command to run
ENV JAVA_OPTS=""
# Setting this on seems to stop the instrumentation, or at least
# sending the spans from working
# ENV PAYARA_OPTS="--noHazelcast --enablerequesttracing"
# ENV PAYARA_OPTS="--noHazelcast" 

# Default command to run
# ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "payara-micro.jar", "--deploymentDir", "/opt/payara/deployments"]
ENTRYPOINT java -XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0 ${JAVA_OPTS} -jar payara-micro.jar --disablephonehome --deploymentDir "/opt/payara/deployments" ${PAYARA_OPTS}