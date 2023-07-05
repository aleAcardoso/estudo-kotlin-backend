package br.com.alura.forum.service

import br.com.alura.forum.model.Usuario
import br.com.alura.forum.repository.UsuarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UsuarioService(private val repository: UsuarioRepository): UserDetailsService {

    fun buscarPorId(idAutor: Long): Usuario  = repository.findByIdOrNull(idAutor) ?: throw RuntimeException()

    override fun loadUserByUsername(username: String?): UserDetails = UserDetail(repository.findByEmail(username) ?: throw java.lang.RuntimeException())
}
