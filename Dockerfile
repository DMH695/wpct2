FROM java:8

COPY *.jar /wpct.jar

EXPOSE 82

ENTRYPOINT ["java","-jar","/wpct.jar"]
