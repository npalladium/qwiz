# QWiz

Web App and Discord Bot to conduct college quizzes online.

[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)


## Getting Started

These instructions will give you a copy of the project up and running on your local machine.

### Prerequisites

- Recent NodeJS version
- MongoDB (running locally on port 27017, without Auth)
- Java with Spring Boot and Gradle
- Docker
- Docker-compose

### Environment Variables

To run this project, you will need to add the following environment variables to your .env file:

`TOKEN=${discord_api_key}`

### Run Locally
Run `setup.sh`.


## FAQ

#### Why can't I just do docker-compose up?

This project uses Spring Boot's Docker buildpack and does not have an explicit Dockerfile. You need to build the Spring image first. 


## Documentation
- Check out the docs folder.
- Check out the git commit messages
- API Docs can be found @ `http://localhost:8080/openapi-docs` when the server is running.
- There is limited HATEOS support in the REST API.

## Tech Stack

**Client:** AngularJS, Angular Material Components

**Server:** Spring Boot, MongoDB

**Deployment:** Docker, Docker-compose


## Lessons Learned

- Server Sent Events with Spring Boot
- Dynamically generating forms with Angular's `FormArray`.

## TODO
Refer project board and commit messages


## Authors

- [Nikhil Reddy](https://www.github.com/npalladium)


## License

[MIT](https://choosealicense.com/licenses/mit/)
