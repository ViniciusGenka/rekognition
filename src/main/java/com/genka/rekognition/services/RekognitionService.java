package com.genka.rekognition.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.CompareFacesResponse;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;

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
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        Image image = Image.builder()
                .bytes(SdkBytes.fromByteArray(imageBytes))
                .build();

        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(5)
                .minConfidence(75F)
                .build();

        DetectLabelsResponse response = rekognitionClient.detectLabels(request);
        return response.labels();
    }

    public List<CompareFacesMatch> compareFaces(String sourceImagePath, String targetImagePath) throws Exception {
        // Lendo as imagens como byte arrays
        byte[] sourceImageBytes = Files.readAllBytes(Paths.get(sourceImagePath));
        byte[] targetImageBytes = Files.readAllBytes(Paths.get(targetImagePath));

        // Criando objetos das imagens
        Image sourceImage = Image.builder()
                .bytes(SdkBytes.fromByteArray(sourceImageBytes))
                .build();

        Image targetImage = Image.builder()
                .bytes(SdkBytes.fromByteArray(targetImageBytes))
                .build();

        // Criando a requisição de comparação
        CompareFacesRequest request = CompareFacesRequest.builder()
                .sourceImage(sourceImage)
                .targetImage(targetImage)
                .similarityThreshold(80F) // Define o nível de similaridade mínima
                .build();

        // Chamando a API Rekognition para comparar as faces
        CompareFacesResponse response = rekognitionClient.compareFaces(request);

        return response.faceMatches(); // Retorna a lista de matches encontrados
    }
}