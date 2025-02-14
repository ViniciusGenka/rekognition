package com.genka.rekognition.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RekognitionService {

    private final RekognitionClient rekognitionClient;

    public RekognitionService() {
        this.rekognitionClient = RekognitionClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public List<Label> detectLabels(String imagePath) throws Exception {
        // Lendo o arquivo da imagem
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        // Criando o objeto da imagem
        Image image = Image.builder()
                .bytes(SdkBytes.fromByteArray(imageBytes))
                .build();

        // Criando o objeto de requisição da detecção de rótulos,
        // passando a imagem e as configurações da detecção,
        // com no máximo 5 rótulos e nível de confiança mínimo de 75%
        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(5)
                .minConfidence(75F)
                .build();

        // Chamando a API do Rekognition para detectar os rótulos
        DetectLabelsResponse response = rekognitionClient.detectLabels(request);
        return response.labels();
    }

    public List<CompareFacesMatch> compareFaces(String sourceImagePath, String targetImagePath) throws Exception {
        // Lendo os arquivos das imagens
        byte[] sourceImageBytes = Files.readAllBytes(Paths.get(sourceImagePath));
        byte[] targetImageBytes = Files.readAllBytes(Paths.get(targetImagePath));

        // Criando o objeto da imagem de comparação
        Image sourceImage = Image.builder()
                .bytes(SdkBytes.fromByteArray(sourceImageBytes))
                .build();

        // Criando o objeto da imagem alvo da comparação
        Image targetImage = Image.builder()
                .bytes(SdkBytes.fromByteArray(targetImageBytes))
                .build();

        // Criando a requisição de comparação
        // passando a imagem e as configurações da comparação,
        // com nível de similaridade mínimo de 80% entre as faces
        CompareFacesRequest request = CompareFacesRequest.builder()
                .sourceImage(sourceImage)
                .targetImage(targetImage)
                .similarityThreshold(80F)
                .build();

        // Chamando a API do Rekognition para comparar as faces
        CompareFacesResponse response = rekognitionClient.compareFaces(request);
        return response.faceMatches();
    }

    public List<TextDetection> detectText(String imagePath) throws Exception {
        // Lendo o arquivo da imagem
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        // Criando o objeto da imagem que contém os textos
        Image image = Image.builder()
                .bytes(SdkBytes.fromByteArray(imageBytes))
                .build();

        // Criando a requisição de detecção dos textos na imagem alvo
        DetectTextRequest request = DetectTextRequest.builder()
                .image(image)
                .build();

        // Chamando a API do Rekognition para detectar textos
        DetectTextResponse response = rekognitionClient.detectText(request);
        return response.textDetections();
    }

    public List<ModerationLabel> detectModerationLabels(String imagePath) throws Exception {
        // Lendo o arquivo da imagem
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        // Criando o objeto da imagem alvo
        Image image = Image.builder()
                .bytes(SdkBytes.fromByteArray(imageBytes))
                .build();

        // Criando a requisição de detecção dos rótulos de moderação na imagem alvo
        DetectModerationLabelsRequest request = DetectModerationLabelsRequest.builder()
                .image(image)
                .minConfidence(70F)
                .build();

        // Chamando a API do Rekognition para detectar os rótulos de moderação
        DetectModerationLabelsResponse response = rekognitionClient.detectModerationLabels(request);
        return response.moderationLabels();
    }
}