@echo off
REM Stop and remove all running containers
docker-compose down

REM Define the services to build and run
set SERVICES=frontend offer_write offer_read kafka event_store postgres notification eureka gateway payment order

REM Remove all Docker volumes except projekt_postgres_data
for /F "delims=" %%i in ('docker volume ls -q') do (
    if "%%i" neq "projekt_postgres_data" docker volume rm %%i
)

REM Remove all Docker images
docker rmi -f $(docker images -aq)

REM Build
docker-compose build %SERVICES%

REM Run the containers
docker-compose up -d %SERVICES%

echo Script execution completed.
pause
