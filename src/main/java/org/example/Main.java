package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private ResourceBundle rb;
    private Locale currentLocale = Locale.forLanguageTag("en-US");

    private Label languageLabel;
    private ComboBox<String> languageComboBox;
    private Label itemCountLabel;
    private TextField itemCountField;
    private Button generateButton;
    private VBox itemsBox;
    private Button calculateButton;
    private TextArea resultArea;

    private final List<TextField> priceFields = new ArrayList<>();
    private final List<TextField> quantityFields = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        rb = ResourceBundle.getBundle("MessagesBundle", currentLocale);

        languageLabel = new Label();
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        languageComboBox.setValue("English");

        itemCountLabel = new Label();
        itemCountField = new TextField();

        generateButton = new Button();
        calculateButton = new Button();

        itemsBox = new VBox(10);

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(180);

        updateTexts(stage);

        languageComboBox.setOnAction(e -> {
            String selected = languageComboBox.getValue();

            switch (selected) {
                case "Finnish":
                    currentLocale = Locale.forLanguageTag("fi-FI");
                    break;
                case "Swedish":
                    currentLocale = Locale.forLanguageTag("sv-SE");
                    break;
                case "Japanese":
                    currentLocale = Locale.forLanguageTag("ja-JP");
                    break;
                case "Arabic":
                    currentLocale = Locale.forLanguageTag("ar-AR");
                    break;
                default:
                    currentLocale = Locale.forLanguageTag("en-US");
                    break;
            }

            rb = ResourceBundle.getBundle("MessagesBundle", currentLocale);
            updateTexts(stage);
            regenerateItemFields();
        });

        generateButton.setOnAction(e -> regenerateItemFields());
        calculateButton.setOnAction(e -> calculateTotal());

        VBox content = new VBox(
                12,
                languageLabel,
                languageComboBox,
                itemCountLabel,
                itemCountField,
                generateButton,
                itemsBox,
                calculateButton,
                resultArea
        );

        content.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 520, 700);
        stage.setTitle(rb.getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    private void updateTexts(Stage stage) {
        languageLabel.setText(rb.getString("language"));
        itemCountLabel.setText(rb.getString("itemCount"));
        itemCountField.setPromptText(rb.getString("itemCountPrompt"));
        generateButton.setText(rb.getString("generate"));
        calculateButton.setText(rb.getString("calculate"));
        resultArea.clear();
        stage.setTitle(rb.getString("title"));
    }

    private void regenerateItemFields() {
        itemsBox.getChildren().clear();
        priceFields.clear();
        quantityFields.clear();
        resultArea.clear();

        int itemCount;
        try {
            itemCount = Integer.parseInt(itemCountField.getText());
            if (itemCount <= 0) {
                resultArea.setText(rb.getString("invalidItemCount"));
                return;
            }
        } catch (NumberFormatException ex) {
            resultArea.setText(rb.getString("invalidItemCount"));
            return;
        }

        for (int i = 1; i <= itemCount; i++) {
            Label itemLabel = new Label(rb.getString("item") + " " + i);

            Label priceLabel = new Label(rb.getString("price"));
            TextField priceField = new TextField();
            priceField.setPromptText(rb.getString("pricePrompt") + " " + i);

            Label quantityLabel = new Label(rb.getString("quantity"));
            TextField quantityField = new TextField();
            quantityField.setPromptText(rb.getString("quantityPrompt") + " " + i);

            priceFields.add(priceField);
            quantityFields.add(quantityField);

            VBox itemBox = new VBox(5, itemLabel, priceLabel, priceField, quantityLabel, quantityField);
            itemBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
            itemsBox.getChildren().add(itemBox);
        }
    }

    private void calculateTotal() {
        if (priceFields.isEmpty() || quantityFields.isEmpty()) {
            resultArea.setText(rb.getString("generateFirst"));
            return;
        }

        double cartTotal = 0.0;
        StringBuilder output = new StringBuilder();

        try {
            for (int i = 0; i < priceFields.size(); i++) {
                double price = Double.parseDouble(priceFields.get(i).getText());
                int quantity = Integer.parseInt(quantityFields.get(i).getText());

                double itemTotal = ShoppingCartCalculator.calculateItemTotal(price, quantity);
                cartTotal += itemTotal;

                output.append(rb.getString("item"))
                        .append(" ")
                        .append(i + 1)
                        .append(" ")
                        .append(rb.getString("itemTotal"))
                        .append(": ")
                        .append(itemTotal)
                        .append("\n");
            }

            output.append(rb.getString("cartTotal"))
                    .append(": ")
                    .append(cartTotal);

            resultArea.setText(output.toString());

        } catch (NumberFormatException ex) {
            resultArea.setText(rb.getString("invalidInput"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}