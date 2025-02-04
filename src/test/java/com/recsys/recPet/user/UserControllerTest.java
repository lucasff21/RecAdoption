package com.recsys.recPet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        Map<String, Object> usuarioValido = new HashMap<>();
        usuarioValido.put("nome", "Maria");
        usuarioValido.put("cpf", "09767326022");
        usuarioValido.put("genero", "Feminino");
        usuarioValido.put("dataNascimento", "2006-01-01");
        usuarioValido.put("email", "maria@gmail.com");
        usuarioValido.put("senha", "Coroa123@");
        usuarioValido.put("telefone", "7192321212");
        usuarioValido.put("localidade", "Abreu e Lima");
        usuarioValido.put("uf", "PE");
        usuarioValido.put("complemento", "");
        usuarioValido.put("bairro", "Caetés III");
        usuarioValido.put("logradouro", "Rua Cinquenta e Cinco");
        usuarioValido.put("cep", "53545-330");

        var requestJson = objectMapper.writeValueAsString(usuarioValido);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        Optional<User> user = userRepository.findByEmail("maria@gmail.com");

        assertEquals(TipoUsuario.ADOTANTE, user.get().getTipoUsuario());
    }

    @Test
    void deveRetornarErroQuandoDadoForInvalido() throws Exception {
        Map<String, Object> usuarioInvalido = new HashMap<>();
        usuarioInvalido.put("nome", "");
        usuarioInvalido.put("cpf", "123");
        usuarioInvalido.put("genero", "Feminino");
        usuarioInvalido.put("dataNascimento", "2020-01-01");
        usuarioInvalido.put("email", "m@");
        usuarioInvalido.put("senha", "123");
        usuarioInvalido.put("telefone", "5354533");
        usuarioInvalido.put("cep", "132123");
        usuarioInvalido.put("bairro", "");
        usuarioInvalido.put("logradouro", "");
        usuarioInvalido.put("localidade", "Abreu e Lima");
        usuarioInvalido.put("uf", "PE");
        usuarioInvalido.put("complemento", "");

        var requestJson = objectMapper.writeValueAsString(usuarioInvalido);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senha").value("A senha deve ter pelo menos 8 caracteres"))
                .andExpect(jsonPath("$.dataNascimento").value("Usuário deve ser maior de idade"))
                .andExpect(jsonPath("$.telefone").value("Telefone deve ter entre 10 e 11 dígitos"))
                .andExpect(jsonPath("$.logradouro").value("Logradouro é obrigatório"))
                .andExpect(jsonPath("$.bairro").value("Bairro é obrigatório"))
                .andExpect(jsonPath("$.cpf").value("CPF inválido"))
                .andExpect(jsonPath("$.nome").value("Nome é obrigatório"))
                .andExpect(jsonPath("$.email").value("Email inválido"))
                .andExpect(jsonPath("$.cep").value("CEP inválido, deve estar no formato 00000-000 ou 00000000"));
    }
}

