FROM openjdk:23
LABEL authors="MoritzSchwarz"

COPY target/c7experiment*.jar /demoC7.jar

CMD ["java", "-jar", "/demoC7.jar"]