@echo off
REM Stop and remove all running containers
docker-compose down

REM Define the services to build and run
set SERVICES=notification eureka gateway offer_read offer_write payment order activemq tour_operator frontend preference

REM Remove all Docker volumes except projekt_postgres_data
for /F "delims=" %%i in ('docker volume ls -q') do (
    if "%%i" neq "projekt_postgres_data" docker volume rm %%i
)

REM Run the containers
docker-compose up -d %SERVICES%

echo Script execution completed.
pause
