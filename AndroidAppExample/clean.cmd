for /d /r . %%d in (bin,gen,build) do @if exist "%%d" rd /s/q "%%d"
pause