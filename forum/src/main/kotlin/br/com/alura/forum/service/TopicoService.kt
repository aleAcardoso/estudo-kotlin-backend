package br.com.alura.forum.service

import br.com.alura.forum.dto.AutalizacaoTopicoForm
import br.com.alura.forum.dto.NovoTopicoForm
import br.com.alura.forum.dto.TopicoPorCategoriaDTO
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.repository.TopicoRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TopicoService(
    private val repository: TopicoRepository,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper
) {
    private val notFoundMessage: String = "Topico nao encontrado!"

    @Cacheable(cacheNames = ["Topicos"], key = "#root.method.name")
    fun listar(
        nomeCurso: String?,
        paginacao: Pageable
    ): Page<TopicoView> {
        val topicos = nomeCurso?.let { nome ->
            repository.findByCursoNome(nome, paginacao)
        } ?: run {
            repository.findAll(paginacao)
        }
        return topicos.map { t -> topicoViewMapper.map(t) }
    }

    fun buscarPorId(id: Long): TopicoView =
        topicoViewMapper.map(
            repository.findById(id).orElseThrow{NotFoundException(notFoundMessage)}
        )

    @CacheEvict(cacheNames = ["Topicos"], allEntries = true)
    fun cadastrar(form: NovoTopicoForm): TopicoView {
        val topico = topicoFormMapper.map(form)
        repository.save(topico)
        return topicoViewMapper.map(topico)
    }

    @CacheEvict(cacheNames = ["Topicos"], allEntries = true)
    fun atualizar(form: AutalizacaoTopicoForm): TopicoView {
        val topico = repository.findById(form.id).orElseThrow{NotFoundException(notFoundMessage)}
        topico.titulo = form.titulo
        topico.mensagem = form.mensagem
        topico.dataAlteracao = LocalDate.now()
        return topicoViewMapper.map(topico)
    }

    @CacheEvict(cacheNames = ["Topicos"], allEntries = true)
    fun deletar(id: Long) {
        repository.deleteById(id)
    }

    fun relatorio(): List<TopicoPorCategoriaDTO> {
        return repository.relatorio()
    }
}