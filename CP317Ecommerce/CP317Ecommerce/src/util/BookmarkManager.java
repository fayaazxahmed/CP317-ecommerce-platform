package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import domain.Product;

public class BookmarkManager {

    private static final String BOOKMARK_FILE = "bookmarks.txt";

    /** Save a product to bookmarks.txt, avoids duplicates */
    public static void saveBookmark(Product product) {
        List<String> lines = loadBookmarks();

        // Format the product exactly like in category file using Product.toFileLine()
        String productLine = product.toFileLine();

        if (!lines.contains(productLine)) {
            lines.add(productLine);
            writeBookmarks(lines);
        }
    }

    /** Load all bookmarks as raw lines (can parse to Products later if needed) */
    public static List<String> loadBookmarks() {
        List<String> lines = new ArrayList<>();
        File file = new File(BOOKMARK_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()) lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    /** Overwrites bookmarks.txt with given lines */
    private static void writeBookmarks(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKMARK_FILE))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
