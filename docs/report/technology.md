# Two-Tier MVC Web Application - Technologies used
### The project will be separated into 2 servers:
- Backend server: Java, [Servlet] (https://www.oracle.com/technetwork/java/index-jsp-135475.html)
- Frontend server: [Node.js](https://nodejs.org/en/about/)/[EJS](https://ejs.co/)

### Why using 2 separate servers?
1. The backend now only acts as a web server and only has to return data, which is much more lightweight than returning full HTML pages. The frontend can now appear to be more **_responsive_** than using HTML navigation and is as fast as the client machine is.
2. The frontend can be developed as a Single-Page Application, with or without reactive web frameworks being applied.
3. Separation of concerns. The frontend can act without knowing anything about what the backend does behind-the-scene. Traditional Web application require a HTML page to be rendered from a controller, so the Controller has to know at least the name of the View it returns.

### Why Node.js/EJS for front-end?
Because the group has more or less worked with Node.js in web development in prior courses and found the platform to be both fast and lightweight. EJS was chosen for its flat learning curve.