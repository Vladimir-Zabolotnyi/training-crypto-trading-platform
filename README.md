## Crypto trading platform

The API and small UI for cryptocurrency trading.

### Setup prerequisites:

- java 8
- maven
- docker (optional)

### Build steps:

##### Bare metal solution:

- mvn clean spring-boot:run

##### Virtualized solution:

- docker compose build --no-cache
- docker compose up -d

### Branch management
##### Naming:

- Branches are started with tracker tag and have brief feature description (_trnctp-815_user_api_extension_)
- Commit messages are started with tracker tag (_[trnctp-815]_ _Added ability to change user's name_)