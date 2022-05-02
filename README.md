# Links
azure link to webapp running container: https://businessinfoswiftly.azurewebsites.net/
## Endpoints
GET actuator/health : default health check for the pod
GET businessInfo/neighborhoodCount?neighborHood=<String> to see how many businesses reside in a neighborhood (Allowable to be empty)
  ex: https://businessinfoswiftly.azurewebsites.net/businessInfo/neighborhoodCount?neighborHood=Tenderloin
GET businessInfo/zipCodeCount?zipCode=<String> to see how many businesses reside in a zip code 
  ex: https://businessinfoswiftly.azurewebsites.net/businessInfo/zipCodeCount?zipCode=94105
POST businessInfo/addBusinessInfo too add business info (See documentation in the API section of the code

# Purpose
The purpose of this project was to demonstrate my ability to rapidly create functional code. Stand it up within a requested cloud environment (Azure as requested). And write code that is well documented and extensible into a few possible ways.

# Constraints And Notes
- I am chosing to allow myself no more than 8 hours to work on this project. This includes setting up the resources on Azure as well as testing functionality and performance and ci-cd
- I will be using a free azure account. If you want access to the resource group you are more than welcome
- I will try to demonstrate some unit tests but the application is not complex enough to merit me writing a large number of mockito tests
- To avoid using vault for times sake i will inject the environment variables into the container for things such as admin user and password for the SQL database
- It is assumed that DBA's or "Doing Business As" are unique to businesses as that would infringe on trademark and i saw no duplication in the data set
- It is assumed that some of the neighborhoods can not be marked. These will not be null... instead they will just be an empty string so you can see how many are empty by simply requesting an empty string for the neighborhood.
- Data will be loaded into the database on azure via an additional endpoint i created via a python script attached in the repo

# Points of improvement
- I am using an in memory cache that is loaded once on load, if this was a large applicaiton i would consider using redis instead.
- Because i have added an endpoint to change data and am currently favoring a cacheing approach we are limited to one pod running at a time unless redis was implemented. This should not matter for the size of data or rate at which it changes.
- The way spring handles requests is already on a thread pool so this implmentation would be able to perform well if the data was more volatile

# Deployment
Currently the deployment steps have not been formalized into a CI-CD but generally they would be incorporated as part of a gitlab-ci.yaml to run the following steps

- Run a mvn clean test and ensure that all tests pass
- Run a docker build -t tagging it to the latest image
- Configure docker to log in directly to the azure portal and then run docker push to the dev/stress environment
- Run whatever integration test suite was being built
- Run docker push to the production branch 
- Azure would be configured such that for both dev/stress and production any push of the latest tag to the container registry would automatically deploy

# Final thoughts
The script LoadBusinessInfo.py is highly optimized, so much to the point that i maxed out the available DTU's on the free azure account so i had to set the thread pool to a lower number to initially load it.... This script would be ideal to put onto a rundeck server and host on azure running nightly to update business info. If this were to be done there would need to be further logic in the code to remove any element that is not in the database but is present in the csv file that is the source of truth so that we remove stale records.

Further, a significant amount of time spent on this project was configuring azure resources and routes. Again if you would like access to the resource group feel free to ask. The code itself is very straightforward and simple but i believe the way i layed out the code structure makes it very easy to read, understand, and extend. 

When i refered to prototyping a project, this is the most work that would go into a functional prototype... This may be a little overkill in terms of setting up the infra, webhooks to auto deploy, health checks on the actuator and an optimized script that could be put on rundeck to maintain data integrity.
