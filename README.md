# TextProcessor


- [Overview](#overview)
- [Features](#features)
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

## Features
- Most of the code is documented
- MakeFile provides abstraction for CICD (backend tools can be changed without breaking CICD files) 
- Docker Support for platform independence and kubernetes deployment
- Commit Message checking via commitlint
- Auto code formatting while compiling
- Test cases of NLP Service
- Automated Changelogs using git-chglog

### Requirements
- Java 11
- Sbt
- Makefile
- Docker
- [Changelogs](https://github.com/git-chglog/git-chglog)
- [Commitlint](https://github.com/conventional-changelog/commitlint)

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
- Visualizations of NLP Summary Stats
- Embellishments: Documentation website
- Show errors on the form
- Validation : Input text should hav at least one sentence etc.
- Deployment on Kubernetes (Need a cloud account)

## Changelog

See the [CHANGELOG](CHANGELOG.md) for all changes since project inception

## Authors

* Ankit Pal ([@pal0064](http://www.github.com/pal0064)) 

