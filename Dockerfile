FROM gradle:8.5.0-jdk21
WORKDIR /app
COPY /app .
RUN gradle installBootDist
CMD ./build/install/app-boot/bin/app