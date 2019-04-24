package csee2210.camera;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.lang.ProcessBuilder;
import java.lang.Process;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

/**
 * This class represents an application that lets the user take an image using 
 * the Raspberry Pi Camera Module.  That image is then run through Google Tesseract 
 * and the GNUstep speech engine to produce an auditory transcription of the text
 * within the image.
 */
public class CameraApp extends Application {
    //instance variables
    private Button demo;
    private Button takePic;
    private Button exit;
    private VBox text;
    
    /**
     * {@inheritDoc}
     */
    public void start(Stage stage) {
		VBox pane = new VBox();
		Scene scene = new Scene(pane);

		//Making some boxes
		HBox opt = new HBox(10);
		opt.setAlignment(Pos.CENTER);
		text = new VBox();

		//adding box bois to the scene
		pane.getChildren().addAll(text, opt);

		//add the text to the app
		makeText();
		
		//setting up options
		demo = new Button("Demo Camera");
		demo.setMinHeight(40);
		takePic = new Button("Take a Picture");
		takePic.setMinHeight(40);
		exit = new Button("Exit the Program");
		exit.setMinHeight(40);

		//give buttons functionality
		demo.setOnAction(demoCamera());
		takePic.setOnAction(takePicture());
		exit.setOnAction((ActionEvent a) -> System.exit(0));

		//add buttons to opt
		opt.getChildren().addAll(demo, takePic, exit);

		stage.setResizable(false);
		stage.setTitle("Camera!");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
    }

    /** 
     * This method initializes the text seen when opening the application.  The 
	 * purpose of this text is to inform the user on what the app is and how to
	 * use it.
     */
    private void makeText() {
		//make text objects
		Text title = new Text("Welcome to ImageReader!");
		title.setFont(new Font(36));
		title.setTextAlignment(TextAlignment.CENTER);

		Text intro = new Text("This app allows you to take a picture and have " +
								"it read to you.\n");
		intro.setFont(new Font(16));

		Text funcs = new Text("You have three options: \n" +
					"1.Demo Camera: This shows you the view from the camera.\n" +
					"2.Take a  Picture: This takes the picture and reads its text " +
					"(if any).\n" +
					"3.Exit the Program: This exits the application.\n");
		funcs.setFont(new Font(16));

		Text tips = new Text("Tips: \n" +
					"-Try to take your picture in the best lighting possible.\n" +
					"-Try to hold your device steadily.\n" +
					"-Try to take your picture as straight as possible.\n");
		tips.setFont(new Font(16));

		//add them to the scene graph
		text.getChildren().addAll(title, intro, funcs, tips);
    }

    /**
     * This method runs the Raspberry Pi Cammera Module's demo mode.
	 * @return an {@code EventHandler} that calls the Pi Camera's demo mode
     */
    private EventHandler<ActionEvent> demoCamera() {
		EventHandler<ActionEvent> demoCam = (ActionEvent a) -> {
			try {
				ProcessBuilder pb = new ProcessBuilder("raspistill", "-d");
				Process p = pb.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};
		return demoCam;
    }

    /**
     * This method tells the Raspberry Pi Camera Module to take a picture with some 
	 * attributes.  Mainly, the image is 1920x1080 in resolution, it has a quality of 
	 * 100, and it is saved as a jpg file under the resources folder.  The buttons are
	 * disabled so the user cannot take another picture until the initial one is
	 * transcribed.  After the picture is taken, it goes through the next step in the 
	 * process, generating the text from the image.
	 * @return an {@code EventHandler} that takes a picture among other thingss
     */
    private EventHandler<ActionEvent> takePicture() {
		EventHandler<ActionEvent> newPic = (ActionEvent a) -> {
			//first, disable buttons, so user can't mess with them
			demo.setDisable(true);
			takePic.setDisable(true);
			exit.setDisable(true);

			//take picture
			try {
				ProcessBuilder pb = new ProcessBuilder("raspistill", "-w", "1920", "-h", "1080", "-o", "resources/tempImg.jpg", "-q", "100", "-sh", "100");
				Process p = pb.start();
				p.waitFor();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//generate text from Google Tesseract
			genText();

			//everything is done, so re-enable the buttons
			demo.setDisable(false);
			takePic.setDisable(false);
			exit.setDisable(false);
			};
		return newPic;
    }

    /** 
     * This method generates text from the image taken in {@code takePicture()} using 
	 * Google Tesseract.  The psm is set to 6, so the text in the image is treated as a 
	 * block of text.
     */
    private void genText() {
        try {
			ProcessBuilder pb = new ProcessBuilder("tesseract", "resources/tempImg.jpg", "resources/tempTxt", "-psm", "6");
			Process p = pb.start();
			p.waitFor();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
        }

		//now that text is generated, generate audio
		genAudio();
    }

    /**
     * This method takes the text generated from {@code genText()} and outputs it 
	 * as audio using the GNUstep speech engine.  After this is called, the application
	 * is done with the current picture, and the user can take another one.
     */
    private void genAudio() {
		try {
			ProcessBuilder pb = new ProcessBuilder("say", "-f", "resources/tempTxt.txt");
			Process p = pb.start();
			p.waitFor();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method launches the application.  This main method isn't perfect, but it
	 * works for this application, which is all I can ask for.
	 * @param args the command line arguments, of which there are none :)
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
