REM -----------------------------
REM FFMpegFrontEnd
REM -----------------------------
echo off
cls
REM


REM echo %JAVA_HOME%
REM echo %CLASS_PATH%

REM SET "dirlocation=%JAVA_HOME%\bin\"
GOTO STARTCOMPILE


:STARTCOMPILE
echo "%dirlocation%"
del /q *.class
echo Create the Manifest file:
echo Main-Class: FFMpegFrontEnd >MANIFEST.MF
echo .

echo Compile the Java code:
"%dirlocation%javac.exe" ..\00__common_code\*.java
copy ..\00__common_code\*.class .



REM "%dirlocation%javac.exe" -Xlint "FFMpegFrontEnd.java"
"%dirlocation%javac.exe" "FFMpegFrontEnd.java"

echo .
echo Build the JAR file:
"%dirlocation%jar.exe" cfm "FFMpegFrontEnd.jar" MANIFEST.MF *.class ..\icons\*.gif ..\icons\*.png  "FFMpegFrontEnd*.txt"

REM
del /q *.class
del /q ..\00__common_code\*.class
REM del *.gif

:END
echo .
echo Finished!
pause