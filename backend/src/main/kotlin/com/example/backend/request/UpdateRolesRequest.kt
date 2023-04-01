package com.example.backend.request

class UpdateRolesRequest(
    val userid: Long,
    val roleids: Array<Long>
)