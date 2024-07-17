package work.project.beercenter.utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.google.zxing.common.BitMatrix;

public class MatrixToImageWriter {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
//    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
//        BufferedImage image = toBufferedImage(matrix);
//
//        ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
//        if (writer == null) {
//            throw new IllegalStateException("No writer found for format " + format);
//        }
//
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file)) {
//            writer.setOutput(ios);
//            writer.write(image);
//        } finally {
//            writer.dispose();
//        }
//    }

    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);

        ImageWriter writer = null;
        ImageOutputStream ios = null;
        try {
            writer = ImageIO.getImageWritersByFormatName(format).next();
            ios = ImageIO.createImageOutputStream(file);
            writer.setOutput(ios);
            writer.write(image);
        } finally {
            if (writer != null) {
                writer.dispose();
            }
            if (ios != null) {
                ios.close();
            }
        }
    }
}
