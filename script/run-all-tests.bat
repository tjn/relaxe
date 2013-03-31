
@CALL run-test.bat fi.tnie.db.meta.impl.pg.pagila.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat fi.tnie.db.meta.impl.mysql.sakila.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat fi.tnie.db.paging.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%

@CALL run-test.bat fi.tnie.db.rpc.AllTests
@IF ERRORLEVEL 1 exit /B %ERRORLEVEL%




