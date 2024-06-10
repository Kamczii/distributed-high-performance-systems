docker build ./Frontend  --no-cache -t k444mczi/rsww_180066_frontend:2.0
docker build ./Gateway  --no-cache -t k444mczi/rsww_180066_gateway:2.0
docker build .  --no-cache -f ./Notifications/Dockerfile -t k444mczi/rsww_180066_notification:2.0
docker build .  --no-cache -f ./OfferRead/Dockerfile -t k444mczi/rsww_180066_offerread:2.0
docker build .  --no-cache -f ./OfferWrite/Dockerfile -t k444mczi/rsww_180066_offerwrite:2.0
docker build .  --no-cache -f ./Order/Dockerfile -t k444mczi/rsww_180066_order:2.0
docker build .  --no-cache -f ./Payment/Dockerfile -t k444mczi/rsww_180066_payment:2.0
docker build .  --no-cache -f ./TourOperator/Dockerfile -t k444mczi/rsww_180066_touroperator:2.0