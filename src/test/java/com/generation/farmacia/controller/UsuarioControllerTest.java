package com.generation.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.repository.UsuarioRepository;
import com.generation.farmacia.service.UsuarioService;
import com.generation.farmacia.util.TestBuild;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String USUARIO_ROOT_EMAIL = "root@root.com";
	private static final String USUARIO_ROOT_SENHA = "rootroot";
	private static final String BASE_URL_USUARIOS = "/usuarios";

	@BeforeAll
	void start(){
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuild.criarUsuarioRoot());
	}
	
	@Test
	@DisplayName("Deve cadastrar um novo usuário com sucesso")
	public void deveCadastrarUsuario() {
		
		// Given
		Usuario usuario = TestBuild.criarUsuario(null, "Paulo Antunes",
				"paulo_antunes@email.com.br", "13465278");

		// When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Paulo Antunes", resposta.getBody().getNome());
		assertEquals("paulo_antunes@email.com.br", resposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		
		//Given
		Usuario usuario = TestBuild.criarUsuario(null, "Maria da Silva",
				"maria_silva@email.com.br", "13465278");
		usuarioService.cadastrarUsuario(usuario);

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@DisplayName("Deve atualizar um usuário existente")
	public void deveAtualizarUmUsuario() {
		
		//Given
		Usuario usuario = TestBuild.criarUsuario(null, "Juliana Andrews", "juliana_andrews@email.com.br",
				"juliana123");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
		
		Usuario usuarioUpdate = TestBuild.criarUsuario(usuarioCadastrado.get().getId(), "Juliana Ramos", 
				"juliana_ramos@email.com.br", "juliana123");

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Juliana Ramos", resposta.getBody().getNome());
		assertEquals("juliana_ramos@email.com.br", resposta.getBody().getUsuario());
	}

	@Test
	@DisplayName("Deve listar todos os usuários")
	public void deveListarTodosUsuarios() {
		
		//Given
		usuarioService.cadastrarUsuario(TestBuild.criarUsuario(null, "Ana Clara",
				"ana@email.com", "senha123"));
		usuarioService.cadastrarUsuario(TestBuild.criarUsuario(null, "Carlos Souza",
				"carlos@email.com", "senha123"));

		//When
		ResponseEntity<Usuario[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/all", HttpMethod.GET, null, Usuario[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("Deve buscar usuario especifico")
	public void deveBuscarUsuario() {
		
		//Given
		Usuario usuario = TestBuild.criarUsuario(null, "Josias Teofilo",
				"josias@email.com", "1234");

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);


		//When
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/" + usuarioCadastrado.get().getId().toString(), HttpMethod.GET, null, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Josias Teofilo", resposta.getBody().getNome());
		assertEquals("josias@email.com", resposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve encontrar usuário")
	public void deveBuscarUsuarioInexistente() {
		
		//Given


		//When
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/" + 2000, HttpMethod.GET, null, Usuario.class);

		//Then
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve logar com usuário existente")
	public void deveLogarUsuario() {
		
		//Given
		Usuario usuario = TestBuild.criarUsuario(null, "Josias Teofilo",
				"josias@email.com", "1234");
		
		usuarioService.cadastrarUsuario(usuario);

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/logar", HttpMethod.POST, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("", resposta.getBody().getSenha());
		assertEquals("josias@email.com", resposta.getBody().getUsuario());
	}
	
}