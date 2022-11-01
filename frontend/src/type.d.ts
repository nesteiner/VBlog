declare interface RegisterRequest {
    username: string,
    passwordHash: string
}

declare interface LoginRequest {
    username: string,
    passwordHash: string
}

declare interface User {
    id: number,
    name: string
}