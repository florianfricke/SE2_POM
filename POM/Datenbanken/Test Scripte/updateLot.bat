SET lotid=%1
echo only for Product 2017SX
(echo UPDATE lot SET route='START',oper='0100',state='WAIT' WHERE lotid='%lotid%';)| "C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes 
pause
(echo UPDATE lot SET route='START',oper='0110' WHERE lotid='%lotid%';)| "C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='START',oper='0200' WHERE lotid='%lotid%';)| "C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='START',oper='0300' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0100' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0200' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0300' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0400' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0500' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0600' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='IMP2',oper='0700' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0100' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0200' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0300' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0400' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0500' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0600' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0700' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause
(echo UPDATE lot SET route='GT2',oper='0800',state='FINISHED' WHERE lotid='%lotid%';)|"C:\Program Files\PostgreSQL\9.6\bin\psql.exe" mes
pause