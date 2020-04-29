# SE2_Spring2020_Project
A small web project to track the spread of CoViD-19 disease.

## Team members
- Đỗ Hải Bình (leader)
- Trương Ngọc Quỳnh
- Nguyễn Công Quang
- Quản Trọng Tú

## Overview
This project is one of the metrics of student performance in Hanoi University's Software Engineering II course.
In our take on the project, we have built our web application as a 2-tier application, with Frontend server and Backend server separated. What connects the two servers is the API, whose specification is available in YAML format in /docs/api.
The backend is constructed as a Web API using Java EE Servlet technology, working over HTTP protocol. The frontend is a NodeJS HTML server with EJS template engine.

## Deployment
Currently, we have only provided support for deploying both on the same machine. The backend can be built as a standalone JAR file, and the frontend can be built as a node module. To operate the project, just run both servers concurrently. Currently, neither server can rely on external server-related configurations - this will be taken into consideration in subsequent patches.
