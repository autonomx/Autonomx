
FROM node:8.16-alpine

USER root

RUN apk update

# install jdk
RUN apk fetch openjdk8
RUN apk add openjdk8
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV PATH="$JAVA_HOME/bin:${PATH}"
RUN java -version
RUN javac -version

# install dependencies
RUN apk update && apk add --no-cache bash \
        alsa-lib \
        at-spi2-atk \
        atk \
        cairo \
        cups-libs \
        dbus-libs \
        eudev-libs \
        expat \
        flac \
        gdk-pixbuf \
        glib \
        libgcc \
        libjpeg-turbo \
        libpng \
        libwebp \
        libx11 \
        libxcomposite \
        libxdamage \
        libxext \
        libxfixes \
        tzdata \
        libexif \
        udev \
        xvfb \
        zlib-dev \
        chromium \
        chromium-chromedriver \
        curl \
        maven

# copy test files
COPY automation /automation

# copy driver to local directory
RUN cp -r /usr/bin/chromedriver /automation/automation/chromedriver

# run tests
WORKDIR /automation/automation
RUN mvn clean compile test -DsuiteXmlFile=suites/serviceTests.xml -Dweb.driver.manual.path=chromedriver