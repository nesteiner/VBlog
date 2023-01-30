declare interface Role {
    id: number,
    name: string
}

declare interface RegisterRoleRequest {
    name: string
}

declare interface UpdateRoleRequest {
    id: number,
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

declare interface Student {
    id: number,
    name: string,
    grade: string,
    major: string,
    clazz: string,
    institute: string,
    telephone: string,
    email: string,
    sex: string,
    roles: Role[],
    cardId: string
}

declare interface Admin {
    id: number,
    name: string,
    roles: Role[]
}

declare interface RegisterStudentRequest {
    name: string,
    grade: string,
    major: string,
    clazz: string,
    institute: string,
    telephone: string,
    passwordHash: string,
    email: string,
    sex: string,
    cardId: string
}

declare interface UpdateStudentRequest {
    name: string,
    grade: string,
    major: string,
    clazz: string,
    institute: string,
    telephone: string,
    passwordHash: string,
    email: string,
    sex: string,
    cardId: string
}

declare interface DataResponse<T> {
    content: T[],
    totalPages: number
}