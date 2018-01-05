::依赖mongdb安装目录bin文件夹环境变量
::依赖"MONGODB_DATA_HOME"环境变量
@echo off
SETLOCAL 
SET CUR_DIR="%cd%"
SET CONFIG_FILE_DIR=mongod.cfg
SET INIT_JS_DIR=create_user.js

::停止mongod进程
tasklist /nh | find /i "mongod.exe" && taskkill /im mongod.exe /f && echo kill mongod succeed!
::等待进程结束2秒
ping -n 2 127.0>nul

::创建目录，创建日志文件
if "%MONGODB_DATA_HOME%"=="" SET MONGODB_DATA_HOME=C:\mongodb_data_home
if not exist "%MONGODB_DATA_HOME%" md "%MONGODB_DATA_HOME%"
if exist "%MONGODB_DATA_HOME%\log" rd /S /Q "%MONGODB_DATA_HOME%\log"
md "%MONGODB_DATA_HOME%\log"
if exist "%MONGODB_DATA_HOME%\data\db" rd /S /Q "%MONGODB_DATA_HOME%\data\db"
md "%MONGODB_DATA_HOME%\data\db"
if exist "%MONGODB_DATA_HOME%\log\mongod.log" del %MONGODB_DATA_HOME%\log\mongod.log
type nul>%MONGODB_DATA_HOME%\log\mongod.log

::生成配置文件
if exist "%CONFIG_FILE_DIR%" del "%CONFIG_FILE_DIR%"
type nul>"%CONFIG_FILE_DIR%"
echo systemLog: >> "%CONFIG_FILE_DIR%"
echo     destination: file>> "%CONFIG_FILE_DIR%"
echo     path: %MONGODB_DATA_HOME%\log\mongod.log>> "%CONFIG_FILE_DIR%"
echo storage:>> "%CONFIG_FILE_DIR%"
echo     dbPath: %MONGODB_DATA_HOME%\data\db>> "%CONFIG_FILE_DIR%"
	
::mongodb启动
start mongod -f "%CONFIG_FILE_DIR%"

::mongodb导出数据
mongoexport --db dba --collection collectiona --out collectiona.json --host 192.168.1.1:27017
mongoexport --db dba --collection collectionb --out collectionb.json --host 192.168.1.1:27017

::mongodb导入数据
cd ../../
cd qa/mongdb_data
mongoimport --db dba --collection collectiona --file collectiona.json
mongoimport --db dba --collection collectionb --file collectionb.json
cd "%CUR_DIR%"

::清除数据文件
del collectiona.json
del collectionb.json

::创建用户
mongo --host localhost:27017 "%INIT_JS_DIR%"
ENDLOCAL

if %errorlevel%==1 (
	echo "build failed!"
)else (
	echo "build succeed!"
)
pause
