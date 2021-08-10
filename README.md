# GameEye Backend
Video game news tracker using web scraping from popular websites with scoring powered by machine learning.

This is the project for the GameEye backend. The project is composed of multiple subprojects:
* Main backend (primary backend of GameEye)
* CLI (command line interface for interacting with the backend)
* Common (common code library shared between subprojects)

## Requirements
* Java 11

## GameEye Architectural Diagram
<img src="https://raw.githubusercontent.com/aangelos28/GameEye-website/master/assets/img/deliverables/MFCDiagram.jpg" width="1000">

## Installation
NOTE: Configuration files with secret credentials are required to run the backend.
Please contact Angelos Angelopoulos (aangelos28@gmail.com) to get them.

Open the project with IntelliJ IDEA. If everything went smoothly, IntelliJ should have recognized
the subprojects as modules. You can check this under `File > Project Struture`.

You might need to add configurations to compile and run the projects. The configurations should be
`Spring Boot` and point to the main classes of the necessary subprojects.

In order to run the projects you need to put `secrets.json` under the `resources` folder in
the CLI and main backend subprojects.
