# Tear down
docker compose down

# Build everything
mvn install
docker compose build

# Start Docker containers
docker compose up -d postgresql mongo rabbitmq
