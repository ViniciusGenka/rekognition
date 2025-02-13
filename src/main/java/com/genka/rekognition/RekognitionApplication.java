package com.genka.rekognition;

import com.genka.rekognition.services.RekognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

import java.util.List;

@SpringBootApplication
public class RekognitionApplication implements CommandLineRunner {

	@Autowired
	private RekognitionService rekognitionService;

	public static void main(String[] args) {
		SpringApplication.run(RekognitionApplication.class, args);
	}

	private void runDetectLabels() throws Exception {
		String imagePath = "src/main/resources/images/landscape.jpg";
		List<Label> labels = rekognitionService.detectLabels(imagePath);
		for (Label label : labels) {
			System.out.printf("%s (%.2f%% confidence)%n", label.name(), label.confidence());
		}
	}

	private void runCompareFaces() throws Exception {
		String sourceImagePath = "src/main/resources/images/silvio_santos_tv.png";
		String targetImagePath = "src/main/resources/images/silvio_santos_casual.jpg";
		List<CompareFacesMatch> matches = rekognitionService.compareFaces(sourceImagePath, targetImagePath);

		if (matches.isEmpty()) {
			System.out.println("Nenhuma correspondência encontrada!");
		} else {
			System.out.println("Rostos encontrados!");
			for (CompareFacesMatch match : matches) {
				System.out.printf("Similaridade: %.2f%%\n", match.similarity());
			}
		}
	}

	private void runDetectText() throws Exception {
		String imagePath = "src/main/resources/images/caneca_tgid.jpeg";
		List<TextDetection> texts = rekognitionService.detectText(imagePath);

		for (TextDetection text : texts) {
			System.out.println("Texto: " + text.detectedText());
			System.out.printf("Confiança: %.2f%%\n", text.confidence());
			System.out.println("Tipo: " + text.type());
		}
	}

	@Override
	public void run(String... args) throws Exception {
		runDetectText();
		runDetectLabels();
		runCompareFaces();
	}
}
