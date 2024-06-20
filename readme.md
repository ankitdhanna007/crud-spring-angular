**Description:**

A simple CRUD application was created along with authentication and authorization functionality. An simple example of car leasing is taken where users can login to the system and create car entities and manage them. For demonstration purposes, only ADMIN and EDITOR roles are activated. ADMIN user has the permission to perform all the CRUD operations but EDITOR user can only view and edit the resources.


**Technology stack used:**

For backend, java based framework, spring-boot 3.30 is used. For authentication and authorization, JWT tokens are used and hence spring security and jjwt libraries are used.
For client side, angular-18 is used with angular-materials library for styling. H2 database is used but can be changed in the spring configuration.
Version of java used - 17
Version of node used - v20


**How can I run the project:**

In the root folder, there should be backend and frontend folders, containing spring and angular code respectively. Both backend and frontend apps are containerised by docker. 
Spring app will be served at http://localhost:8005/
Angular app will be served at http://localhost:4200/


**Using docker:**
1) Go to backend folder and build the project so that we have a jar in the target folder.    
   ```sh
   mvn clean install
   ```
3) Run docker compose in the root folder, that should start both spring and angular apps.
   ```sh
   docker-compose --file docker-compose.yml up
   ```
   
**Without docker:**
1) Go to backend folder and build the project and then start spring boot:.   
   ```sh
   mvn clean install && spring-boot:run
   ```
3) Go to frontend folder and run angular app. (Make sure the running node version is at-least 18)
   ```sh
   npm install	     
   npm install -g @angular/cli
   ng serve 
    ```

**Working of the app:**

Spring app starts some REST APIs which can be explored through the swagger UI http://localhost:8005/swagger-ui/index.html. 
By default, 2 users are already seeded into the database, on with role ADMIN and other with role EDITOR.
All endpoints except login and signup routes are protected with jwt token security. PS- signup endpoint is just for verbosity and is not used in the angular app.
When login is done successfully, a jwt token is returned with some expiry and the client app store the token in their local data store so that it can be sent along with further requests to the backend.
Angular app provides basic screens like login, car listing, and car modification. Following are the credentials for the login:
Admin user - admin/admin
Editor user - editor/editor
Routes are protected through guards for role based exploitation and http-interceptors are used for adding jwt token on the request headers and also used to logout when response returns with expired token.
Users with editor role cant see the ‘Add’ and ‘Delete’ car feature, and can only be done by admin user.


**Known issues and improvement:**

Look and feel of angular app is not the best, did not devote dedicated time to make a good UX.
There are some issues with spring rest controller tests. Mockmvc is not behaving correctly in conjunction with spring security and jwt.
Pagination and filtering of the resources are not implemented due to time constraints.
Current JWT tokens provide a stateless communication with our web server. That means when a token cannot be invalidated unless it expires. Further improvement can be done by various approaches, one could be blacklisting the token that are marked by logout requests. 
