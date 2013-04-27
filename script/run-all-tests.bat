
@CALL run-test.bat com.appspot.relaxe.pg.pagila.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat com.appspot.relaxe.mysql.sakila.AllTests                   
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat com.appspot.relaxe.paging.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat com.appspot.relaxe.rpc.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%




