declare interface Role {
    id: number,
    name: string
}

declare interface RegisterRequest {
    username: string,
    passwordHash: string,
}

declare interface RegisterRoleRequest {
    name: string
}

declare interface LoginRequest {
    username: string,
    passwordHash: string
}

declare interface User {
    id: number,
    name: string,
    roles: Role[]
}

declare interface UpdateUserNameRequest {
    id?: number,
    name: string
}

declare interface UpdateUserRoleRequest {
    id: number,
    roles: Role[]
}