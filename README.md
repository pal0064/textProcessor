# to do
- Embellishments:  Documentation website, auto format, code style, commit msg checker
- Show errors 
- - Validation - If you have experience with web development then write some basic JavaScript to check the validity of the input (e.g., that there's something there, that it has at least one sentence, etc.).

- [Overview](#overview)
- [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [Executing Instructions](#executing-instructions)
- [Changelog](#changelog)
- [Authors](#authors)
--------------

## Overview
This project can be used to create summary stats from a text or a text file. It provides a 
webform for inputs. Outputs can be published on a UI or in a csv file.

![alt text](docs/images/TextProcessor.drawio.png?raw=true)

### Requirements
- java 11
- sbt
- Makefile
- docker

### Executing Instructions
- There are multiple ways to execute this application
  - Simplest ways is to pull the image from repository and run. It currently supports platform linux/arm64.
    - ```make run-remote-image```
    - to stop ```make stop-remote-image-container```
  - To build and run the local image  
    - ```
      make build-docker
      make run-docker
      ```
    - to stop ```make stop-docker```
  - To run without docker 
    - ```make run```
- ```make compile``` for compiling
- ```make test``` for running test cases. #todo Adding test cases for controllers and other classes
- ```make publish``` for publishing image to the remote repository

# TODO 
- Deployment on Kubernetes (Need a cloud account)

## Changelog

See the [CHANGELOG](CHANGELOG.md) for all changes since project inception

## Authors

* Ankit Pal ([@pal0064](http://www.github.com/pal0064)) 

