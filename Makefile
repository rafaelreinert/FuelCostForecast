.PHONY: build service test clean prune

build:
	mvn clean install -DskipTests
	docker-compose build --no-cache app

up:
	docker-compose up

run: build up

clean:
	mvn clean

prune:
	docker system prune -af

test:
	 mvn test