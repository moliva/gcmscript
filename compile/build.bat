@echo off

if NOT DEFINED JAVA_HOME goto javahome
if "%JAVA_HOME%" == "" goto javahome
if NOT DEFINED PROACTIVE_HOME goto proactivehome
if "%PROACTIVE_HOME%" == "" goto proactivehome
if "%1" == "" goto projecthelp

:build
SETLOCAL
set COMPILEDIR=%PROACTIVE_HOME%\compile\lib\ant-launcher.jar;%CLASSPATH%
set CLASSPATH=%PROACTIVE_HOME%\compile\lib\ant-launcher.jar;%CLASSPATH%
echo %CLASSPATH%
cd ..
"%JAVA_HOME%\bin\java" "-Dant.home=%COMPILEDIR%" "-Dant.library.dir=%COMPILEDIR%/lib" "-Dproactive.dir=%PROACTIVE_HOME%" -Xmx512000000 org.apache.tools.ant.launch.Launcher -buildfile compile/build.xml %* %WHEN_NO_ARGS%
ENDLOCAL
goto end

:projecthelp
set WHEN_NO_ARGS="-projecthelp"
goto build

:javahome
echo.
echo The enviroment variable JAVA_HOME must be set to the current jdk 
echo distribution installed on your computer.
echo Use 
echo    set JAVA_HOME="<the directory where is the JDK>"
goto end

:proactivehome
echo.
echo The enviroment variable PROACTIVE_HOME must be set to the current ProActive source folder
echo distribution installed on your computer.
echo Use 
echo    set PROACTIVE_HOME="<the directory where is ProActive Programming>"
goto end

:end
set WHEN_NO_ARGS=
