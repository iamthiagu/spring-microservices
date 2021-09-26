Docker commands:

1.docker run -p {hostport}:{container port} -d(run in background or detached mode) in28min/todo-rest-api-h2:1.0.0.RELEASE
2.docker run -p 5000:5000 -d in28min/todo-rest-api-h2:1.0.0.RELEASE
          Internally above command will execute like
3.docker container run -p 5000:5000 -d in28min/todo-rest-api-h2:1.0.0.RELEASE

4.docker run -p 5000:5000 -d --restart=always  -m 512m -cpu-quota 5000 in28min/todo-rest-api-h2:1.0.0.RELEASE
          When docker desktop is restarted it will start the container always.
          cpu quota total is 100000
          Here 5 % of it 5000 is assigned which is very low so docker will barely do any work. Can check the stats using stats cmd


5.docker logs {containerId}
6.docker logs -f {containerId}

7.docker container ls (Shows only active)
8.docker container ls -a (show all (active + exited))

9.docker container stop {containerId}
10.docker stop $(docker ps -q) - to stop all running containers
11.docker container kill {containerId}
12.docker container pause {containerId}
13.docker container unpause {containerId}
14.docker container inspect {containerId} - Shows current status when it was created etc
15.docker container prune - Removes all the stopped containers

16.docker container top {cid} - shows the top process running for the container
17.docker stats -- gives the stats for the each of the container running
18.docker containser stats {cid} 

19.docker images
20.docker pull image repository:tag
21.docker tag repository:tag repository:tagtarget

22.docker events  - Shows the log of all the containers(wt is happening background) 
                    whenever any change in container like stop ,start etc.
23.docker system df
TYPE            TOTAL     ACTIVE    SIZE      RECLAIMABLE
Images          22        14        5.625GB   1.531GB (27%)
Containers      15        12        1.151GB   73.34kB (0%)
Local Volumes   14        14        2.511GB   0B (0%)
Build Cache     0         0         0B        0B


Build image : mvn spring-boot:build-image -DskipTests

