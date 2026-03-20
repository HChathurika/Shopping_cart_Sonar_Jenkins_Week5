FROM ubuntu:latest
LABEL authors="silva"

ENTRYPOINT ["top", "-b"]