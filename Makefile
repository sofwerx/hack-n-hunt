TRAVIS_REPO_SLUG:=$$TRAVIS_REPO_SLUG
TRAVIS_TAG:=$$TRAVIS_TAG

all:
	git submodule update --init --recursive
	docker pull sofwerx/$(TRAVIS_REPO_SLUG):latest || true
	docker build --cache-from sofwerx/$(TRAVIS_REPO_SLUG):latest -t sofwerx/$(TRAVIS_REPO_SLUG):$(TRAVIS_TAG) .
	docker run --rm -v $(PWD)/outputs:/outputs sofwerx/$(TRAVIS_REPO_SLUG):$(TRAVIS_TAG)

push:
	docker push sofwerx/$(TRAVIS_REPO_SLUG):latest

