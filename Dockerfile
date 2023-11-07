# Use the official Tomcat image as the base image
FROM tomcat:9-jre11

# Copy the WAR file to the container
COPY target/Customer_API_Dairy_MGMT_Provider-1-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# Expose the port that your application will listen on
EXPOSE 8082

# The Tomcat image's entry point already starts Tomcat, so no need to specify CMD

