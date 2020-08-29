## About
This is UI application for maintaining multiple docker images in an organization.
Wiremock usually write the mappings to disk at the end of recording.
As disk can't be accessed when deployed to cloud, we can use this UI application to handle those recorded mappings.
 
#### Key features -
 * One stop shop for handling multiple Wiremock instances in an enterprise
 * Start/Stop recording
 * Import/Export mappings to Mongo database
 * Import/Export mappings to local file system
 * Mapping files can be organized in folders before uploading to Wiremock

### Build Docker Image
`docker build -t ravikalla/wiremock-backup:1.0 .`

### Prerequisite
##### Create a bridge between Wiremock and MongoDB and attach both the containers to it

Create Network:
```
docker network create --driver bridge wiremock_docker_bridge
```

Delete Network:
```
docker network rm  wiremock_docker_bridge
```

##### Run MongoDB in Docker

Create MongoDB:
`docker run --name wiremockui-mongo -p 27017:27017 --network="wiremock_docker_bridge" -d mongo`

Delete MongoDB:
```
docker stop wiremockui-mongo
docker rm wiremockui-mongo
docker rmi mongo
```

### Run Docker Container
##### From Local Image
`docker run --name wiremockui -p 8080:8080 --network="wiremock_docker_bridge" -d ravikalla/wiremock-backup:1.0`

##### From Dockerhub Image
`docker run --name wiremockui -p 8080:8080 --network="wiremock_docker_bridge" -d ravikalla/wiremock-backup`

##### From Swarm
###### Start Application:
`docker-compose up`
###### Stop Application:
`docker-compose stop && docker-compose rm -f`

### Wiremock UI
 * [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Note:
 * We can also uncomment below element in "pom.xml" file to enable embeded MongoDB
```
		<dependency>
			<groupId>de.flapdoodle.embed</groupId>
			<artifactId>de.flapdoodle.embed.mongo</artifactId>
		</dependency>
```