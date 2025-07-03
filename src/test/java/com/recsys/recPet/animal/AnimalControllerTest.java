package com.recsys.recPet.animal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.repository.AnimalRepository;
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

import java.time.LocalDate;
import java.util.Calendar;
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
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUtils testAuthUtils;

    @MockBean
    private ImageService imageService;

    @Autowired
    private AnimalRepository animalRepository;

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
                        .param("idade", "2023-01-02")
                        .param("sexo", "MACHO")
                        .param("porte", "MEDIO")
                        .param("pelagem", "CURTA")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Animal responseCachorro = new ObjectMapper().readValue(responseJson, Animal.class);

        assertNotNull(responseCachorro.getId());
        assertEquals("Rex", responseCachorro.getNome());
        assertEquals("2023-01-02", responseCachorro.getDataNascimentoAproximada().toString());
        assertEquals(Sexo.MACHO, responseCachorro.getSexo());
        assertEquals(Porte.MEDIO, responseCachorro.getPorte());
        assertEquals("http://test-image.com", responseCachorro.getImagemPath());
    }

    @Test
    void atualizarCachorro_DeveAtualizarImagem()  throws Exception {
        String adminToken = testAuthUtils.generateAdminToken();

        MockMultipartFile imagemEditada = new MockMultipartFile(
                "novaImagem", "cachorro2.jpg", "image/jpeg", "test-image".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class), anyString()))
                .thenReturn(Map.of("secure_url", "http://test-image2.com"));

        doNothing().when(imageService).delete(anyString());

        Animal cachorroTeste = new Animal();
        cachorroTeste.setNome("Luna");
        cachorroTeste.setDataNascimentoAproximada(LocalDate.of(2023, Calendar.FEBRUARY, 2));
        cachorroTeste.setSexo(Sexo.FEMEA);
        cachorroTeste.setPorte(Porte.PEQUENO);
        cachorroTeste.setPelagem(Pelagem.LONGA);
        cachorroTeste.setImagemPath("https://exemplo.com/luna.jpg");

        Animal cachorro = animalRepository.save(cachorroTeste);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PUT, "/api/cachorro/" + cachorro.getId())
                        .file(imagemEditada)
                        .param("nome", "Nora")
                        .param("sexo", "FEMEA")
                        .param("porte", "GRANDE")
                        .param("pelagem", "CURTA")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        Animal responseCachorro = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                Animal.class
        );

        assertEquals("Nora", responseCachorro.getNome());
        assertEquals(Sexo.FEMEA, responseCachorro.getSexo());
        assertEquals(Porte.GRANDE, responseCachorro.getPorte());
        assertEquals("http://test-image2.com", responseCachorro.getImagemPath());
    }
}
