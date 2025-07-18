SHELL		= /bin/sh
NAME		= Swingy

# The target JAR file name
TARGET	= swingy-1.0-SNAPSHOT.jar

all: package run

compile	: clean
	mvn compile

package	: clean
	mvn package

run			:
	java -jar target/$(TARGET) console

clean		:
	mvn clean

re			: clean all

.PHONY	: all compile package run clean re
.DEFAULT_GOAL := all
