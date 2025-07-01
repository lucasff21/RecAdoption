package com.recsys.recPet.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Service
public class ImageService {

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.api.key}")
    private String apiKey;

    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;

    Logger logger = org.slf4j.LoggerFactory.getLogger(ImageService.class);

    public ImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map uploadImage(MultipartFile file, String folder) throws IOException {
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, String> signatureParams = new HashMap<>();
        signatureParams.put("folder", folder);
        signatureParams.put("timestamp", String.valueOf(timestamp));
        String uploadPreset = "pet_uploads";
        signatureParams.put("upload_preset", uploadPreset);
        String signature = generateSignature(signatureParams);

        logger.info("Parâmetros da Assinatura: {}", signatureParams);
        logger.info("Assinatura Gerada: {}", signature);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override public String getFilename() { return file.getOriginalFilename(); }
        });
        body.add("api_key", apiKey);
        body.add("upload_preset", uploadPreset);
        body.add("signature", signature);
        body.add("timestamp", String.valueOf(timestamp));
        body.add("folder", folder);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        String url = "https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload";
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Map.class);


        return response.getBody();
    }

    private String generateSignature(Map<String, String> params) {
        try {
            List<String> sortedKeys = new ArrayList<>(params.keySet());
            Collections.sort(sortedKeys);

            StringBuilder sb = new StringBuilder();
            for (String key : sortedKeys) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }

            String toSign = sb.substring(0, sb.length() - 1) + apiSecret;

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(toSign.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar assinatura", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void delete(String imageUrl) {
        String publicId = this.extrairPublicIdCloudinaryUrl(imageUrl);

        String url = "https://api.cloudinary.com/v1_1/" + cloudName + "/image/destroy";
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, String> signatureParams = new HashMap<>();
        signatureParams.put("public_id", publicId);
        signatureParams.put("timestamp", String.valueOf(timestamp));
        String signature = generateSignature(signatureParams);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("api_key", apiKey);
        body.add("public_id", publicId);
        body.add("signature", signature);
        body.add("timestamp", String.valueOf(timestamp));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, requestEntity, Void.class);
    }

    public String extrairPublicIdCloudinaryUrl(String cloudinaryUrl) {

        int posicaoUpload = cloudinaryUrl.indexOf("/upload/") + 8;

        String caminhoComVersaoArquivo = cloudinaryUrl.substring(posicaoUpload);

        String caminhoComArquivo = caminhoComVersaoArquivo.replaceFirst("v\\d+/", "");

        int posicaoUltimoPonto = caminhoComArquivo.lastIndexOf('.');

        return posicaoUltimoPonto > 0 ? caminhoComArquivo.substring(0, posicaoUltimoPonto) : caminhoComArquivo;
    }
}