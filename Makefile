.PHONY: build service test clean prune

build:
	mvn clean install -DskipTests
	docker-compose build --no-cache app

up:
	docker-compose up

run: build up

clean:
	# remove created images
	mvn clean

prune:
	# clean all that is not actively used
	docker system prune -af

test:
	# here it is useful to add your own customised tests
	 mvn test