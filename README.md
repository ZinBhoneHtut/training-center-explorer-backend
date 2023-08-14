# Training Center Explorer Backend

The Training Center Explorer Backend is an ongoing Java application developed using the Spring Boot framework. It's designed to provide a robust backend foundation for a training center management system. The project includes powerful features such as RESTful web services, Spring Security integration with JWT token and refresh token functionality. Currently, the project offers user CRUD operations, dynamic pagination and sorting, as well as query criteria for retrieving user data using Spring Data JPA.

## Features

- **RESTful Web Services**: Utilize the comprehensive REST API structure to enable efficient communication.
- **Spring Security with JWT**: Ensure top-notch authentication and authorization using Spring Security with JWT tokens.
- **Refresh Token**: Enhance security with a refresh token mechanism for prolonged user sessions.
- **Dynamic Pagination and Sorting**: Leverage Spring Data JPA to enable flexible data retrieval.
- **Query Criteria**:  Utilize Spring Data JPA's specification for dynamic query criteria.

## Getting Started

1. Clone the repository: `https://github.com/ZinBhoneHtut/training-center-explorer-backend.git`
2. Navigate to the project directory: `cd training-center-explorer-backend`
3. Configure MySQL Database:
    - Create a MySQL schema named `tce_db`.
    - Update `application.properties` with your MySQL username and password.
4. Build and run the project: `./mvnw spring-boot:run`

## Usage

- Interact with the provided RESTful APIs using tools like Postman or cURL.
- Secure your API endpoints by leveraging JWT token-based authentication.

## Database Options

- The project uses MySQL by default. You can set up MySQL and configure it in `application-dev.properties`.
- Alternatively, you can switch to H2 in-memory database for development by replacing `dev` with `h2` in application.properties.

## Postman Workspace

Explore our public Postman workspace to conveniently interact with the backend web services. The workspace includes sample requests for various API endpoints.

[Postman Workspace](https://www.postman.com/zin-bhone-htut/workspace/zin-bhone-htut-public-workspace/collection/7812344-9ca4be37-ace8-4067-abb8-b782c513ec34?action=share&creator=7812344)

## License

This project is licensed under the Apache License. See the [LICENSE](LICENSE) file for details.

## Current Status

The project is ongoing, focusing initially on user CRUD operations and secure authentication using JWT tokens and refresh tokens. Future plans include expanding the functionality and scope of the project.

## Contributions

Contributions are warmly welcomed! If you're interested in contributing, please create a pull request.

## Contact

For any inquiries or suggestions, don't hesitate to contact [zinbhonehtut@gmail.com](mailto:zinbhonehtut@gmail.com).
