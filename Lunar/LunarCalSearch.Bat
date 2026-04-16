@echo off
rem chcp 65001 > nul
rem echo ==========================================
rem echo Java 컴파일 및 실행 배치 파일
rem echo ==========================================
rem echo.

cd /d "%~dp0"
set "BAT_DIR=%CD%"
cd  ..
set "ROOT=%CD%"
set CLASS_NAME=LunarCalSearch

REM 컴파일할 파일명 설정
set "SOURCE_DIR=%ROOT%\javaSRC"
set "SOURCE_FILE=%SOURCE_DIR%\%CLASS_NAME%.java"
set "ERROR_FILE=%BAT_DIR%\CompileERROR\ERROR_%CLASS_NAME%.txt"
set "RUN_ERROR_FILE=%BAT_DIR%\CompileERROR\RUN_ERROR_%CLASS_NAME%.txt"
set "OUT=%ROOT%\class"
set "CLASS_DIR=%ROOT%\class"
set "LIB_PATH=%ROOT%\Lib"
set "LIB_PATH2=%ROOT%\Lib\my_import"
set "LIB_PATH3=%ROOT%\Lib\my_import\commons-io-2.15.0"

rem echo [1단계] 컴파일 중...
echo 컴파일 , 소스: %SOURCE_FILE%
rem echo.

REM 에러 파일 초기화
if exist %ERROR_FILE% del %ERROR_FILE%

REM 컴파일 (에러를 파일로 저장)
javac -d %CLASS_DIR%   -encoding UTF-8 ^
  --class-path "%CLASS_DIR%;%LIB_PATH%\*;%LIB_PATH2%\*;%LIB_PATH3%\*" ^
  -sourcepath "%SOURCE_DIR%" ^
  "%SOURCE_FILE%" 2> "%ERROR_FILE%"

REM 컴파일 결과 확인 (errorlevel 체크)
if %errorlevel% neq 0 (
    echo [실패] 컴파일 에러가 발생했습니다!
    echo 에러 내용:
    echo ==========================================
    type %ERROR_FILE%
    echo ==========================================
    echo.
    echo 에러 로그가 %ERROR_FILE%에 저장되었습니다.
    start "" "C:\Program Files\Notepad++\notepad++.exe" "%ERROR_FILE%"  "%SOURCE_FILE%"
    goto :end
)

REM 에러 파일이 비어있는지 확인 (추가 안전장치)
for %%A in (%ERROR_FILE%) do set FILE_SIZE=%%~zA
if %FILE_SIZE% gtr 0 (
    echo [경고] 컴파일 경고가 있습니다:
    type %ERROR_FILE%
    echo.
)

echo [성공] 컴파일 완료!
echo.

rem echo [2단계] 프로그램 실행 중...
rem echo ==========================================
java -cp "%CLASS_DIR%;%LIB_PATH%\*;%LIB_PATH2%\*;%LIB_PATH3%\*" %CLASS_NAME% "2026" "04"  > %RUN_ERROR_FILE%
pause
java -cp "%CLASS_DIR%;%LIB_PATH%\*;%LIB_PATH2%\*;%LIB_PATH3%\*" %CLASS_NAME% "2026" "05"  >> %RUN_ERROR_FILE%
pause

rem echo ==========================================
echo.

rem echo [완료] 프로그램 실행이 종료되었습니다.

:end
rem echo.
rem pause