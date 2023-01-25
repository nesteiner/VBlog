package com.example.backend.service

import com.example.backend.model.Student
import com.example.backend.repository.RoleRepository
import com.example.backend.repository.StudentRepository
import com.example.backend.request.RegisterStudentRequest
import com.example.backend.request.UpdateStudentRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as OtherUser

@Service
class StudentService : UserDetailsService, UserService<Student, RegisterStudentRequest, UpdateStudentRequest> {
    @Autowired
    lateinit var studentRepository: StudentRepository
    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun insertOne(data: RegisterStudentRequest): Student {
        val role = roleRepository.findByName("student")!!
        val student = Student(
            null, data.name, data.grade, data.major, data.clazz, data.institude, data.telephone,
            data.email, data.passwordHash, data.cardId, data.sex, listOf(role)
        )
        return studentRepository.save(student)
    }

    override fun deleteOne(id: Long) {
        studentRepository.deleteById(id)
    }

    override fun updateOne(data: UpdateStudentRequest): Student {
        val ifstudent = studentRepository.findByIdOrNull(data.id)!!
        val student = Student(
            data.id,
            data.name,
            data.grade,
            data.major,
            data.clazz,
            data.institude,
            data.telephone,
            data.email,
            data.passwordHash,
            data.cardId,
            data.sex,
            ifstudent.roles
        )

        return studentRepository.save(student)
    }

    override fun findOne(id: Long): Student? {
        return studentRepository.findByIdOrNull(id)
    }

    override fun findOne(name: String): Student? {
        return studentRepository.findByName(name)
    }

    override fun findAll(): List<Student> {
        return studentRepository.findAll()
    }

    override fun findAll(pageable: Pageable): Page<Student> {
        return studentRepository.findAll(pageable)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val optionalUser = studentRepository.findByName(username)
        return optionalUser?.let {user ->
            val simpleGrantedAuthorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }

            OtherUser(user.name, user.passwordHash, simpleGrantedAuthorities)
        } ?: throw UsernameNotFoundException("no such user")
    }
}