package com.genka.rekognition;

import com.genka.rekognition.services.RekognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.Label;

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
			System.out.printf("➡ %s (%.2f%% confidence)%n", label.name(), label.confidence());
		}
	}

	private void runCompareFaces() throws Exception {
		String sourceImagePath = "src/main/resources/images/silvio_santos_tv.png";
		String targetImagePath = "src/main/resources/images/silvio_santos_casual.jpg";
		List<CompareFacesMatch> matches = rekognitionService.compareFaces(sourceImagePath, targetImagePath);

		if (matches.isEmpty()) {
			System.out.println("❌ Nenhuma correspondência encontrada!");
		} else {
			System.out.println("✅ Rostos encontrados! Nível de similaridade:");
			for (CompareFacesMatch match : matches) {
				System.out.printf("➡ Similaridade: %.2f%%\n", match.similarity());
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		runDetectLabels();
		runCompareFaces();
	}
}
