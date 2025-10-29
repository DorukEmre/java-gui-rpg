SHELL		= /bin/sh

NAME		= Swingy

# The target JAR file name
TARGET	= swingy-1.0-SNAPSHOT.jar

all			: package


compile	: clean
	mvn compile

package	: clean
	mvn package


console	:
	java -jar target/$(TARGET) console

gui			:
	java -jar target/$(TARGET) gui

run			: gui


clean		:
	mvn clean

re			: clean all


.PHONY	: all compile package run console gui clean re
.DEFAULT_GOAL := all
