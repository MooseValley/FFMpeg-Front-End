REM-----------------------------
REM FFMpegFrontEnd
REM-----------------------------
echo off
cls
REM


REM echo %JAVA_HOME%
REM echo %CLASS_PATH%

REM SET "dirlocation=%JAVA_HOME%\bin\"
GOTO STARTCOMPILE

echo To determine your Java JDK folder location, what machine are you using:
echo  1.  HP Z220 Desktop
echo  2.  HP Elitebook Laptop
echo  3.  CQUni Computer Labs:
echo  4.  CQUni Computer Labs #2:
echo  5.  Centacare HP laptop
echo  6.  Centacare BriTS3
echo  7.  Quit
CHOICE /C:1234567
echo %ERRORLEVEL%


REM NOTE: These IF tests *must* be done in DESCENDING ORDER of ERRORLEVEL.
if ERRORLEVEL 7 GOTO END
if ERRORLEVEL 6 GOTO Centacare_BriTS3
if ERRORLEVEL 5 GOTO Centacare_HP_laptop
if ERRORLEVEL 4 GOTO CQUni_Computer_Labs2
if ERRORLEVEL 3 GOTO CQUni_Computer_Labs
if ERRORLEVEL 2 GOTO HP_Elitebook_Laptop
if ERRORLEVEL 1 GOTO HP_Z220_Desktop


:HP_Z220_Desktop
SET "dirlocation=C:\Program Files (x86)\Java\jdk1.8.0_112\bin\"
GOTO STARTCOMPILE

:HP_Elitebook_Laptop
SET "dirlocation=C:\Program Files\Java\jdk1.8.0_121\bin\"
GOTO STARTCOMPILE

:CQUni_Computer_Labs
REM 29-Jul-2016  (still using a very OLD version of Java)
SET "dirlocation=C:\Program Files (x86)\Java\jdk1.7.0_67\bin\"
GOTO STARTCOMPILE

:CQUni_Computer_Labs2
REM 29-Jul-2016  (still using a very OLD version of Java)
SET "dirlocation=C:\Program Files (x86)\Java\jdk1.8.0_66\bin\"
GOTO STARTCOMPILE

:Centacare_HP_laptop
SET "dirlocation=C:\Program Files\Java\jdk1.8.0_121\bin\"
GOTO STARTCOMPILE

:Centacare_BriTS3
SET "dirlocation=C:\Program Files\Java\jdk1.8.0_121\bin\"
GOTO STARTCOMPILE


:STARTCOMPILE
echo "%dirlocation%"
del *.class
echo Create the Manifest file:
echo Main-Class: FFMpegFrontEnd >MANIFEST.MF
echo .

echo Compile the Java code:
"%dirlocation%javac.exe" ..\00__common_code\Benchmark.java
"%dirlocation%javac.exe" ..\00__common_code\ClassUtils.java
"%dirlocation%javac.exe" ..\00__common_code\Network.java
copy ..\00__common_code\*.class .
"%dirlocation%javac.exe" ..\00__common_code\Moose_Utils.java
copy ..\00__common_code\*.class .
"%dirlocation%javac.exe" ..\00__common_code\Checksum.java
"%dirlocation%javac.exe" ..\00__common_code\SaveLoadApplicationSettings.java
"%dirlocation%javac.exe" ..\00__common_code\StringAndCounter.java
"%dirlocation%javac.exe" ..\00__common_code\ZipAndJar.java
copy ..\00__common_code\*.class .
"%dirlocation%javac.exe" ..\00__common_code\ParentDescriptionCodeLookup.java
"%dirlocation%javac.exe" ..\00__common_code\ImageProcessingPngJpgGif.java
"%dirlocation%javac.exe" ..\00__common_code\ButtonRepeatWhenPressed.java
copy ..\00__common_code\*.class .
"%dirlocation%javac.exe" ..\00__common_code\Icons.java
"%dirlocation%javac.exe" ..\00__common_code\FontScalerMouseWheelListener.java
"%dirlocation%javac.exe" ..\00__common_code\DateLocalDateUtils.java
copy ..\00__common_code\*.class .
"%dirlocation%javac.exe" ..\00__common_code\DateSelector__Moose.java
"%dirlocation%javac.exe" ..\00__common_code\WindowsCommandLine.java
copy ..\00__common_code\*.class .

REM Need the above classes in this local DIR so that the SLK class can access their methods.
"%dirlocation%javac.exe" ..\00__common_code\SLK.java
copy ..\00__common_code\SLK.class .
"%dirlocation%javac.exe" ..\00__common_code\SQLDatabase.java
copy ..\00__common_code\SQLDatabase*.class .


REM "%dirlocation%javac.exe" -Xlint "FFMpegFrontEnd.java"
"%dirlocation%javac.exe" "FFMpegFrontEnd.java"

echo .
echo Build the JAR file:
"%dirlocation%jar.exe" cfm "FFMpegFrontEnd.jar" MANIFEST.MF *.class ..\icons\*.gif ..\icons\*.png  "FFMpegFrontEnd*.txt"

REM
del *.class
del ..\00__common_code\*.class
REM del *.gif

:END
echo .
echo Finished!
pause