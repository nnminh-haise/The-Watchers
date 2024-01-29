# APT Testing Guide

Testing application: Postman

## Connect to DB

1. SSH to remote server on AWS with provied ssh key pair and setup the PostgreSQL server 5432 port to your local favour port. Suggest port 5433.
2. Use pgAdmin app to check SSH connection success.

## Setting up BE service

1. Clone the BE repo with `main` branch
2. You can open it with Intelij or VS Code with necessary extensions.
3. Run service

At this moment, the service should run successfully at localhost:8080

## Sign up API

- Path: `localhost:8080/auth/sign-up`
- Request format: JSON
- Response format: JSON

Request body sample:

```json
{
    "email": "a@gmail.com",
    "password": "a",
    "confirmPassword": "a"
}
```

**Responses:**

1. Sign up successfully:

    - Status code: 200

    Response body sample:

    ```json
    {
        "id": "c7ca3cde-6c54-4b22-ad01-0c8f338073f3",
        "email": "b@gmail.com",
        "password": "$2a$10$bqHNlxGQMqyK.TdSw/xlRuMy29XqiUjIzQO/x00.UxcTeZYNPFNX6",
        "deleteStatus": false,
        "accountNonExpired": true,
        "accountNonLocked": true,
        "credentialsNonExpired": true,
        "authorities": [],
        "username": "b@gmail.com",
        "enabled": true
    }
    ```

2. Sign up failed:

    Request body sample:

    ```json
    {
        "email": "",
        "password": "a",
        "confirmPassword": "a"
    }
    ```

    - Status code: 401

    Response body sample:

    ```json
    {
        "type": "about:blank",
        "title": "Unauthorized",
        "status": 401,
        "detail": "Email is required",
        "instance": "/auth/sign-up",
        "description": "The username or password is incorrect"
    }
    ```

## Sign in API

- Path: `localhost:8080/auth/sign-in`
- Request format: JSON
- Response format: JSON

Request body sample:

```json
{
    "email": "a@gmail.com",
    "password": "a"
}
```

**Responses:**

1. Sign in successfully:

    - Status code: 200

    Response body sample:

    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJubm1pbmguc2FtLjE4MDNAZ21haWwuY29tIiwiaWF0IjoxNzA2NTUyODM2LCJleHAiOjE3MDY1NTY0MzZ9.Wt1E57rDOBLUhCu4Z-fqkOAY-MO2IrJqQBushlAN5ng",
        "expiresIn": 3600000
    }
    ```

2. Sign in failed:

    Request body sample:

    ```json
    {
        "email": "nnminh.sam.1803@gmail.com",
        "password": ""
    }
    ```

    - Status code: 401

    Response body sample:

    ```json
    {
        "type": "about:blank",
        "title": "Unauthorized",
        "status": 401,
        "detail": "Password is required",
        "instance": "/auth/sign-in",
        "description": "The username or password is incorrect"
    }
    ```

## Customer's Info API

Returning customer's personal infomations after they filled out their info.

-  Path: `localhost:8080/api/customer/me`
- Request format: sending the server authenticated token after successfully signed in. Token type: Bearer token.
- Response format: JSON

**Response:**

1. Success:

    - Status code: 200

    Response body samle:

    ```json
    {
        "id": "a33b1cda-f2fc-423a-a782-0183cd361d90",
        "cmnd": "1234567890",
        "ho": "Nguyen Nhat",
        "ten": "Minh",
        "gioitinh": "Nam",
        "ngaysinh": "2003-03-17T17:00:00.000+00:00",
        "diachi": "691 Le Van Viet",
        "sdt": "0704098399",
        "email": "nnminh.sam.1803@gmail.com",
        "masothue": "1234567890",
        "daXoa": false
    }
    ```

2. Failed:

    - Status code: 403

    Response body samle:

    ```json
    {
        "type": "about:blank",
        "title": "Forbidden",
        "status": 403,
        "detail": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
        "instance": "/api/customer/me",
        "description": "The JWT signature is invalid"
    }
    ```

    - Status code: 500

    Response body samle:

    ```json
    {
        "type": "about:blank",
        "title": "Internal Server Error",
        "status": 500,
        "detail": "Malformed JWT JSON: {\"alg\":\"HS256",
        "instance": "/api/customer/me",
        "description": "Unknown internal server error."
    }
    ```    