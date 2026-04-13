package org.example;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @Test
    void mainClassShouldBeCreated() {
        Main main = new Main();
        assertNotNull(main);
    }

    @Test
    void startShouldInitializeUiComponents() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Main[] appHolder = new Main[1];
        final Throwable[] errorHolder = new Throwable[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());
                appHolder[0] = app;
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        Main app = appHolder[0];
        assertNotNull(app);
        assertNotNull(getField(app, "languageLabel"));
        assertNotNull(getField(app, "languageComboBox"));
        assertNotNull(getField(app, "itemCountLabel"));
        assertNotNull(getField(app, "itemCountField"));
        assertNotNull(getField(app, "generateButton"));
        assertNotNull(getField(app, "itemsBox"));
        assertNotNull(getField(app, "calculateButton"));
        assertNotNull(getField(app, "resultArea"));
    }

    @Test
    void languageSelectionShouldCoverAllChoices() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final ComboBox<?>[] comboHolder = new ComboBox<?>[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                Stage stage = new Stage();
                app.start(stage);

                @SuppressWarnings("unchecked")
                ComboBox<String> languageComboBox =
                        (ComboBox<String>) getField(app, "languageComboBox");

                comboHolder[0] = languageComboBox;

                languageComboBox.setValue("Finnish");
                languageComboBox.getOnAction().handle(new javafx.event.ActionEvent());

                languageComboBox.setValue("Swedish");
                languageComboBox.getOnAction().handle(new javafx.event.ActionEvent());

                languageComboBox.setValue("Japanese");
                languageComboBox.getOnAction().handle(new javafx.event.ActionEvent());

                languageComboBox.setValue("Arabic");
                languageComboBox.getOnAction().handle(new javafx.event.ActionEvent());

                languageComboBox.setValue("English");
                languageComboBox.getOnAction().handle(new javafx.event.ActionEvent());

            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(comboHolder[0]);
        assertEquals("English", comboHolder[0].getValue());
    }

    @Test
    void regenerateItemFieldsShouldShowInvalidItemCountForNonNumericInput() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final String[] resultText = new String[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextField itemCountField = (TextField) getField(app, "itemCountField");
                TextArea resultArea = (TextArea) getField(app, "resultArea");

                itemCountField.setText("abc");

                Method method = Main.class.getDeclaredMethod("regenerateItemFields");
                method.setAccessible(true);
                method.invoke(app);

                resultText[0] = resultArea.getText();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(resultText[0]);
        assertFalse(resultText[0].isEmpty());
    }

    @Test
    void regenerateItemFieldsShouldShowInvalidItemCountForZero() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final String[] resultText = new String[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextField itemCountField = (TextField) getField(app, "itemCountField");
                TextArea resultArea = (TextArea) getField(app, "resultArea");

                itemCountField.setText("0");

                Method method = Main.class.getDeclaredMethod("regenerateItemFields");
                method.setAccessible(true);
                method.invoke(app);

                resultText[0] = resultArea.getText();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(resultText[0]);
        assertFalse(resultText[0].isEmpty());
    }

    @Test
    void regenerateItemFieldsShouldCreateFieldsForValidItemCount() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final int[] childCount = new int[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextField itemCountField = (TextField) getField(app, "itemCountField");
                VBox itemsBox = (VBox) getField(app, "itemsBox");

                itemCountField.setText("2");

                Method method = Main.class.getDeclaredMethod("regenerateItemFields");
                method.setAccessible(true);
                method.invoke(app);

                childCount[0] = itemsBox.getChildren().size();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertEquals(2, childCount[0]);
    }

    @Test
    void calculateTotalShouldShowGenerateFirstWhenFieldsAreEmpty() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final String[] resultText = new String[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextArea resultArea = (TextArea) getField(app, "resultArea");

                Method method = Main.class.getDeclaredMethod("calculateTotal");
                method.setAccessible(true);
                method.invoke(app);

                resultText[0] = resultArea.getText();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(resultText[0]);
        assertFalse(resultText[0].isEmpty());
    }

    @Test
    void calculateTotalShouldHandleInvalidNumberInput() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final String[] resultText = new String[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextField itemCountField = (TextField) getField(app, "itemCountField");
                Method regenerateMethod = Main.class.getDeclaredMethod("regenerateItemFields");
                regenerateMethod.setAccessible(true);

                itemCountField.setText("1");
                regenerateMethod.invoke(app);

                @SuppressWarnings("unchecked")
                List<TextField> priceFields = (List<TextField>) getField(app, "priceFields");
                @SuppressWarnings("unchecked")
                List<TextField> quantityFields = (List<TextField>) getField(app, "quantityFields");
                TextArea resultArea = (TextArea) getField(app, "resultArea");

                priceFields.get(0).setText("abc");
                quantityFields.get(0).setText("2");

                Method calculateMethod = Main.class.getDeclaredMethod("calculateTotal");
                calculateMethod.setAccessible(true);
                calculateMethod.invoke(app);

                resultText[0] = resultArea.getText();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(resultText[0]);
        assertFalse(resultText[0].isEmpty());
    }

    @Test
    void calculateTotalShouldCalculateCorrectlyForValidInput() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] errorHolder = new Throwable[1];
        final String[] resultText = new String[1];

        javafx.application.Platform.runLater(() -> {
            try {
                Main app = new Main();
                app.start(new Stage());

                TextField itemCountField = (TextField) getField(app, "itemCountField");
                Method regenerateMethod = Main.class.getDeclaredMethod("regenerateItemFields");
                regenerateMethod.setAccessible(true);

                itemCountField.setText("2");
                regenerateMethod.invoke(app);

                @SuppressWarnings("unchecked")
                List<TextField> priceFields = (List<TextField>) getField(app, "priceFields");
                @SuppressWarnings("unchecked")
                List<TextField> quantityFields = (List<TextField>) getField(app, "quantityFields");
                TextArea resultArea = (TextArea) getField(app, "resultArea");

                priceFields.get(0).setText("10");
                quantityFields.get(0).setText("2");
                priceFields.get(1).setText("5");
                quantityFields.get(1).setText("3");

                Method calculateMethod = Main.class.getDeclaredMethod("calculateTotal");
                calculateMethod.setAccessible(true);
                calculateMethod.invoke(app);

                resultText[0] = resultArea.getText();
            } catch (Throwable e) {
                errorHolder[0] = e;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        if (errorHolder[0] != null) {
            throw new RuntimeException(errorHolder[0]);
        }

        assertNotNull(resultText[0]);
        assertTrue(resultText[0].contains("35"));
    }

    private Object getField(Object target, String fieldName) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}