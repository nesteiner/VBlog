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
	roles: Role[],
	nickname: string,
	enabled: boolean,
	email: string,
	userface: string,
	registerTime: string
}

declare interface RegisterUserRequest {
	name: string,
	roles: Role[],
	passwordHash: string,
	nickname: string,
	enabled: boolean,
	email: string,
	userface: string
}

declare interface Category {
	id: number,
	name: string,
	date: string
}

declare interface RegisterCategoryRequest {
	name: string
}

declare interface Tag {
	id: number,
	name: string
}

declare interface RegisterTagRequest {
	name: string
}

declare interface Article {
	id?: number,
	title: string,
	markdownContent: string,
	htmlContent: string,
	summary: string,
	category: Category,
	author: User,
	publishDate: string,
	editTime: string,
	state: number,
	pageView: number,
	tags: Tag[]
}

declare interface RegisterArticleRequest {
	id?: number,
	title: string,
	markdownContent: string,
	htmlContent: string,
	summary: string,
	category: Category,
	state: number,
	tags: Tag[]
}

declare interface UpdateArticleRequest {
	id: number,
	title: string,
	markdownContent: string,
	htmlContent: string,
	summary: string,
	category: Category,
	tags: Tag[]
}

declare interface DataResponse<T> {
	content: T[],
	totalPages: number
}

declare interface UpdateStateRequest {
	articleId: number,
	state: number
}

declare interface RestoreRequest {
	id: number
}

declare interface UpdateEnabledRequest {
	userid: number,
	enabled: boolean
}

declare interface UpdateRolesRequest {
	userid: number,
	roleids: number[]
}

