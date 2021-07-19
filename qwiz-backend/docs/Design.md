# Design Document

## Considerations

### System Environment

The system will either run on single (cheap) VPS server or on the admin's personal computer.

## Architecture

### Tech Stack

- Spring Boot
- MongoDB
- Angular
- Discord APIs with Javacord Library

### Sequence Diagrams

![Quiz Sequence](./images/quizsequence.png)

### Data Model

The JSON Schema of the initial MonoDB data model is given here. This is subject to change based on the functional requirements and the data acess patterns; this schema may not reflect the latest model.

```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "name": {
      "type": "string"
    },
    "date": {
      "type": "string"
    },
    "qmName": {
      "type": "string"
    },
    "maxPeoplePerTeam": {
      "type": "integer",
      "minimum": 0,
      "maximum": 10
    },
    "noOfTeams": {
      "type": "integer",
      "minimum": 0,
      "maximum": 10
    },
    "deck": {
      "type": "object",
      "properties": {
        "introSlides": {
          "type": "array",
          "items": {
            "type": "integer"
          }
        },
        "rounds": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "name": {
                "type": "string"
              },
              "clockwise": {
                "type": "boolean"
              },
              "minus": {
                "type": "integer"
              },
              "plus": {
                "type": "integer"
              },
              "bounce": {
                "type": "integer"
              },
              "questions": {
                "type": "array",
                "items": {
                  "type": "object",
                  "properties": {
                    "qSlides": {
                      "type": "array",
                      "items": {
                        "type": "integer"
                      }
                    },
                    "hSlides": {
                      "type": "array",
                      "items": {
                        "type": "integer"
                      }
                    },
                    "aSlides": {
                      "type": "array",
                      "items": {
                        "type": "integer"
                      }
                    }
                  },
                  "required": [
                    "qSlides",
                    "hSlides",
                    "aSlides"
                  ]
                }
              }
            },
            "required": [
              "name",
              "clockwise",
              "questions"
            ]
          }
        }
      },
      "required": [
        "rounds"
      ]
    }
  },
  "required": [
    "name",
    "date",
    "deck"
  ]
}

```



### Testing Strategy

TODO