docker build ./EurekaServer -t k444mczi/rsww_180066_eurekaserver
docker build ./Frontend -t k444mczi/rsww_180066_frontend
docker build ./Gateway -t k444mczi/rsww_180066_gateway
docker build . -f ./Notifications/Dockerfile -t k444mczi/rsww_180066_notification
docker build . -f ./OfferRead/Dockerfile -t k444mczi/rsww_180066_offerread
docker build . -f ./OfferWrite/Dockerfile -t k444mczi/rsww_180066_offerwrite
docker build . -f ./Order/Dockerfile -t k444mczi/rsww_180066_order
docker build . -f ./Payment/Dockerfile -t k444mczi/rsww_180066_payment
docker build . -f ./TourOperator/Dockerfile -t k444mczi/rsww_180066_touroperator