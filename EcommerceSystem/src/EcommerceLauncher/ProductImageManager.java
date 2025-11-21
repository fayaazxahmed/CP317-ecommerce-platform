package EcommerceLauncher;

import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProductImageManager {

    private static final String IMAGE_DIR = "images";

    // Opens file chooser, copies image into /images/, returns final path
    public static String chooseImage() {
	try {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

		File chosen = chooser.getSelectedFile();

		// Create images folder if missing
		File folder = new File(IMAGE_DIR);
		if (!folder.exists())
		    folder.mkdir();

		File saved = new File(folder, chosen.getName());

		Files.copy(chosen.toPath(), saved.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return saved.getAbsolutePath();
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return "";
    }

    // Return a scaled JLabel showing the product image, or a placeholder
    public static JLabel getImageLabel(String path, int width, int height) {
	try {
	    if (path != null && !path.isEmpty()) {
		ImageIcon icon = new ImageIcon(path);
		Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(scaled));
	    }
	} catch (Exception ignored) {
	}
	return new JLabel("No Image Available", JLabel.CENTER);
    }
}
