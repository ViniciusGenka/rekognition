package com.genka.rekognition;

import com.genka.rekognition.services.RekognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;
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
		System.out.println(labels);
	}

	private void runCompareFaces() throws Exception {
		String sourceImagePath = "src/main/resources/images/silvio_santos_tv.png";
		String targetImagePath = "src/main/resources/images/silvio_santos_casual.jpg";
		List<CompareFacesMatch> matches = rekognitionService.compareFaces(sourceImagePath, targetImagePath);
		System.out.println(matches);
	}

	private void runDetectText() throws Exception {
		String imagePath = "src/main/resources/images/caneca_tgid.jpeg";
		List<TextDetection> texts = rekognitionService.detectText(imagePath);
		System.out.println(texts);
	}

	private void runDetectModerationLabels() throws Exception {
		String imagePath = "src/main/resources/images/tropa_de_elite.jpg";
		List<ModerationLabel> moderationLabels = rekognitionService.detectModerationLabels(imagePath);
		System.out.println(moderationLabels);
	}



	@Override
	public void run(String... args) throws Exception {
		runDetectModerationLabels();
		runDetectText();
		runDetectLabels();
		runCompareFaces();
	}
}
