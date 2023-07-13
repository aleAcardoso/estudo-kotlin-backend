package br.com.alura.forum.model

object UsuarioTest {
    fun build() = Usuario(
        id = 1,
        nome = "Ana da Silva",
        email = "ana@email.com",
        password = "123456"
    )
}