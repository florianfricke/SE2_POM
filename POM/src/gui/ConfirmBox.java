package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * A box to call the users attention and let him answer a simple yes-no question.
 */
public final class ConfirmBox {

    private static boolean answer;

    /**
     * Displays a new window with a <tt>title</tt>, a<tt>message</tt> and two buttons to choice between.
     *
     * @param title   The title of the window, displayed in the title bar.
     * @param message The message displayed in the window.
     * @return <ul>
     * <li><tt>true</tt> if User clicked yesButton</li>
     * <li><tt>false</tt> if User clicked noButton</li>
     * </ul>
     */
    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setResizable(false);
        
    	window.getIcons().add(new Image(ConfirmBox.class.getResource("Cinderella_Icon.png").toString()));

        //Font font = new Font("Segoe UI", 18);
        Label label = new Label(message);
        //label.setFont(font);
        label.setAlignment(Pos.CENTER);
        Button yesButton = new Button("Yes");
        //yesButton.setFont(font);
        yesButton.setMinWidth(100);
        Button noButton = new Button(("No"));
        //noButton.setFont(font);
        noButton.setMinWidth(100);

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(event -> {
            answer = false;
            window.close();
        });

        HBox hBox = new HBox(10, yesButton, noButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(30, 0, 0, 0));
        VBox layout = new VBox(10, label, hBox);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        window.setScene(new Scene(layout));
        window.showAndWait();

        return answer;
    }
}
