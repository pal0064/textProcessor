NAME := text-processor
REMOTE_IMAGE_NAME:= pal0064/$(NAME)
VERSION := 0.0.1

.PHONY: build-docker run-docker run test compile clean publish run-remote-image stop-remote-image-container generate-changelogs

build-docker:
	docker build --rm -f Dockerfile -t $(NAME):$(VERSION) .

run-docker:
	docker run --name $(NAME) -d -p 9000:9000 -it $(NAME):$(VERSION)
	@echo "Run http://localhost:9000 in browser"

stop-docker:
	docker stop $(NAME)
	docker rm $(NAME)

run:
	sbt run

test:
	sbt test

clean:
	sbt clean

compile:
	sbt compile

publish:
	 docker build --rm -f Dockerfile -t pal0064/$(NAME):$(VERSION) .
 	 docker push -t $(REMOTE_IMAGE_NAME):$(VERSION)

run-remote-image:
	docker run --name remote-$(NAME) -d -p 9000:9000 -it $(REMOTE_IMAGE_NAME):$(VERSION)
	@echo "Run http://localhost:9000 in browser"

stop-remote-image-container:
	@docker stop remote-$(NAME)
	@docker rm remote-$(NAME)

generate-changelogs:
	git-chglog -o CHANGELOG.md

