package com.recsys.recPet.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Cachorro;
import com.recsys.recPet.service.ImageService;
import com.recsys.recPet.utils.AuthUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
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

    @Test
    void criarCachorro_DeveManterEnumsCorretos() throws Exception {
        String adminToken = testAuthUtils.generateAdminToken();

        MockMultipartFile imagem = new MockMultipartFile(
                "imagem", "cachorro.jpg", "image/jpeg", "test-image".getBytes());

        String jsonData = """
                {
                    "nome": "Rex",
                    "idade": "2 anos",
                    "sexo": "MACHO",
                    "porte": "MEDIO",
                    "pelagem": "CURTA",
                    "idealCasa": true,
                    "gostaCrianca": true,
                    "caoGuarda": false,
                    "brincalhao": true,
                    "necessidadeCorrer": true,
                    "quedaPelo": false,
                    "tendeLatir": true
                }
                """;

        MockMultipartFile dados = new MockMultipartFile(
                "cachorroDTO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonData.getBytes()
        );

        when(imageService.uploadImage(any(MultipartFile.class), anyString()))
                .thenReturn(Map.of("secure_url", "http://test-image.com"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/cachorro/create")
                        .file(imagem)
                        .file(dados)
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
}
