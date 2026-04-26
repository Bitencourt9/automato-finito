@echo off
REM Script de compilação e execução do Analisador Léxico

setlocal enabledelayedexpansion

REM Define cores para output
set "GREEN=[92m"
set "YELLOW=[93m"
set "RED=[91m"
set "RESET=[0m"

echo.
echo ===================================================
echo   Analisador Léxico - AFD Java Swing
echo ===================================================
echo.

REM Verifica se o Java está instalado
java -version >nul 2>&1
if errorlevel 1 (
    echo %RED%[ERRO] Java não foi encontrado no PATH%RESET%
    echo Por favor, instale o JDK ou adicione ao PATH do sistema.
    pause
    exit /b 1
)

REM Cria diretório bin se não existir
if not exist "bin" (
    mkdir bin
    echo %GREEN%[OK] Diretório 'bin' criado%RESET%
)

REM Compila os arquivos Java
echo %YELLOW%[*] Compilando arquivos Java...%RESET%
javac -d bin src\*.java

if errorlevel 1 (
    echo %RED%[ERRO] Falha na compilação%RESET%
    pause
    exit /b 1
)

echo %GREEN%[OK] Compilação concluída com sucesso%RESET%
echo.

REM Executa o programa
echo %YELLOW%[*] Iniciando Analisador Léxico...%RESET%
echo.

java -cp bin InterfaceAnalisador

if errorlevel 1 (
    echo %RED%[ERRO] Erro na execução%RESET%
    pause
    exit /b 1
)

endlocal
