
@CALL run-all-tests > ..\log\test-summary.log
@IF ERRORLEVEL 1 goto :fail

@echo PASS
@goto :exit

:fail
@echo FAIL

:exit



