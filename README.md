# Joke proxy
### Stack used

`
Java 17, Spring Boot, FeignClient, JUnit5, Cucumber, SwaggerUI
`

### Build the project
```
mvn clean package
```
### Run
```
 mvn spring-boot:start
```
### Dockerize
#### Build
```
docker build --build-arg JAR_FILE=target/*.jar -t jokes/point .
```
#### Run
```
docker run -p 8080:8080 jokes/point
```

## API
### SwaggerUI
```
http://localhost:8080
```
### Find random Chuck Norris joke:
```
curl --location 'localhost:8080/jokes/random'
```
#### sample output:
```
{
"categories": [],
"icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
"id": "2IQie5kdT1CEJrEQVSoxiA",
"url": "https://api.chucknorris.io/jokes/2IQie5kdT1CEJrEQVSoxiA",
"value": "When Chuck Norris was younger he was excited when he began growing chest hair. All the other boys in his 1st grade class were jealous."
}
```
### Find random Chuck Norris joke by category
```
curl --location 'localhost:8080/jokes/random/explicit'
```
#### sample output:
```
{
    "categories": [
        "explicit"
    ],
    "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
    "id": "LDlO5x0-Rbq28xj9KfMuLQ",
    "url": "https://api.chucknorris.io/jokes/LDlO5x0-Rbq28xj9KfMuLQ",
    "value": "Chuck Norris recently appeared in a special episode of \"Behind The Beard\" to talk about his life, acting career, his epic sexual virility, his boots, beer, his current kill count (rounded down to the nearest thousand), martial arts, death, whiskey and his penis."
}
```
### Search for Chuck Norris jokes by query:
```
curl --location 'localhost:8080/jokes/search?query=hospital'
```
#### sample output:
```
{
    "total": 28,
    "result": [
        {
            "categories": [],
            "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            "id": "DXwvK4XJT5uoQxT-E-kndg",
            "url": "https://api.chucknorris.io/jokes/DXwvK4XJT5uoQxT-E-kndg",
            "value": "Chuck Norris built the hospital where he was born."
        },
        {
            "categories": [],
            "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            "id": "RETYTBF4QEW9S75MNCtBLQ",
            "url": "https://api.chucknorris.io/jokes/RETYTBF4QEW9S75MNCtBLQ",
            "value": "When Chuck Norris was Born, the whole Hospital cried."
        },
        ...
        ...
        left omitted in the sample
```
## TODO
* Ability to limit/paginate jokes when querying
* Human-readable exceptions
* Extract only 'value' from response - exactly what end-user requires
* In case public norris jokes api is down/does not respond in time - run fallback
* Circuit breaker
* Authentication
* ...
