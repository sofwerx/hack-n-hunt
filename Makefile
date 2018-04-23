TRAVIS_REPO_SLUG:=$$TRAVIS_REPO_SLUG
TRAVIS_TAG:=$$TRAVIS_TAG

all:
	git submodule update --init --recursive
	docker pull $(TRAVIS_REPO_SLUG):latest || true
	docker build --cache-from $(TRAVIS_REPO_SLUG):latest -t $(TRAVIS_REPO_SLUG):$(TRAVIS_TAG) .
	docker run --rm -v $(CWD)/outputs:/outputs $(TRAVIS_REPO_SLUG):$(TRAVIS_TAG)

push:
	docker push $(TRAVIS_REPO_SLUG):latest

