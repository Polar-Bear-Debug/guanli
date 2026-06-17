@echo off
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
java -jar "D:\JeecgBoot-main\jeecg-boot\jeecg-module-system\jeecg-system-start\target\jeecg-system-start-3.9.2.jar" --spring.profiles.active=dev > "D:\JeecgBoot-main\backend.log" 2>&1
