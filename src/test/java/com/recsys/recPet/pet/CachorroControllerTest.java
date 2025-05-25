package com.recsys.recPet.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Cachorro;
import com.recsys.recPet.repository.CachorroRepository;
import com.recsys.recPet.service.ImageService;
import com.recsys.recPet.utils.AuthUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CachorroControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUtils testAuthUtils;

    @MockBean
    private ImageService imageService;

    @Autowired
    private CachorroRepository cachorroRepository;

    @Test
    void criarCachorro_DeveManterEnumsCorretos() throws Exception {
        String adminToken = testAuthUtils.generateAdminToken();

        MockMultipartFile imagem = new MockMultipartFile(
                "imagem", "cachorro.jpg", "image/jpeg", "test-image".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class), anyString()))
                .thenReturn(Map.of("secure_url", "http://test-image.com"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/api/cachorro/create")
                        .file(imagem)
                        .param("nome", "Rex")
                        .param("idade", "2 anos")
                        .param("sexo", "MACHO")
                        .param("porte", "MEDIO")
                        .param("pelagem", "CURTA")
                        .param("idealCasa", "true")
                        .param("gostaCrianca", "true")
                        .param("caoGuarda", "false")
                        .param("brincalhao", "true")
                        .param("necessidadeCorrer", "true")
                        .param("quedaPelo", "false")
                        .param("tendeLatir", "true")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Cachorro responseCachorro = new ObjectMapper().readValue(responseJson, Cachorro.class);

        assertNotNull(responseCachorro.getId());
        assertEquals("Rex", responseCachorro.getNome());
        assertEquals("2 anos", responseCachorro.getIdade());
        assertEquals(Sexo.MACHO.toString(), responseCachorro.getSexo());
        assertEquals(Porte.MEDIO.toString(), responseCachorro.getPorte());
        assertEquals("http://test-image.com", responseCachorro.getImagePath());
    }

    @Test
    void atualizarCachorro_DeveAtualizarImagem()  throws Exception {
        String adminToken = testAuthUtils.generateAdminToken();

        MockMultipartFile imagemEditada = new MockMultipartFile(
                "novaImagem", "cachorro2.jpg", "image/jpeg", "test-image".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class), anyString()))
                .thenReturn(Map.of("secure_url", "http://test-image2.com"));

        doNothing().when(imageService).delete(anyString());

        Cachorro cachorroTeste = new Cachorro();
        cachorroTeste.setNome("Luna");
        cachorroTeste.setIdade("2 anos");
        cachorroTeste.setSexo(Sexo.FEMEA.name());
        cachorroTeste.setPorte(Porte.PEQUENO.name());
        cachorroTeste.setPelagem("LONGA");
        cachorroTeste.setIdealCasa(true);
        cachorroTeste.setGostaCrianca(true);
        cachorroTeste.setCaoGuarda(false);
        cachorroTeste.setBrincalhao(true);
        cachorroTeste.setNecessidadeCorrer(true);
        cachorroTeste.setQuedaPelo(false);
        cachorroTeste.setTendeLatir(true);
        cachorroTeste.setImagePath("https://exemplo.com/luna.jpg");

        Cachorro cachorro = cachorroRepository.save(cachorroTeste);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PUT, "/api/cachorro/" + cachorro.getId())
                        .file(imagemEditada)
                        .param("nome", "Nora")
                        .param("sexo", "FEMEA")
                        .param("porte", "GRANDE")
                        .param("pelagem", "CURTA")
                        .param("idealCasa", "true")
                        .param("gostaCrianca", "true")
                        .param("caoGuarda", "false")
                        .param("brincalhao", "true")
                        .param("necessidadeCorrer", "true")
                        .param("quedaPelo", "false")
                        .param("tendeLatir", "true")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        Cachorro responseCachorro = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                Cachorro.class
        );

        assertEquals("Nora", responseCachorro.getNome());
        assertEquals(Sexo.FEMEA.toString(), responseCachorro.getSexo());
        assertEquals(Porte.GRANDE.toString(), responseCachorro.getPorte());
        assertEquals("http://test-image2.com", responseCachorro.getImagePath());
    }
}
