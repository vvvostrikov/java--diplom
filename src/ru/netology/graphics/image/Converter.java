package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter{
    double ratio;
    double maxRatio;
    int maxWidth;
    int maxHeight;

    Schema schema = new Schema();

    @Override
    public String convert(String url) throws  IOException, BadImageSizeException{
        BufferedImage img = ImageIO.read(new URL(url));

        if (img.getWidth() > img.getHeight()){
            if ((double) img.getWidth() / img.getHeight()  > maxRatio){
                throw new BadImageSizeException((double) img.getWidth() / img.getHeight(), maxRatio);
            }
        }else{
            if ((double) img.getHeight() / img.getWidth() > maxRatio) {
                throw new BadImageSizeException((double) img.getHeight() / img.getWidth(), maxRatio);
            }
        }
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        if (newWidth > maxWidth && newHeight > maxHeight) {
            if (newWidth > newHeight) {
                ratio = (double) maxWidth / newWidth;
                newWidth = (int) (ratio * newWidth);
                newHeight = (int) (ratio * newHeight);
            } else {
                ratio = (double) maxHeight / newHeight;
                newWidth = (int) (ratio * newWidth);
                newHeight = (int) (ratio * newHeight);
            }
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        char[][] charImg = new char[newHeight][newWidth];
        for (int pixelW = 0; pixelW < newWidth; pixelW++) {
            for (int pixelH = 0; pixelH < newHeight; pixelH++) {
                int color = bwRaster.getPixel(pixelW, pixelH, new int[3])[0];
                char c = schema.convert(color);
                charImg[pixelH][pixelW] = c;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char[] row : charImg) {
            for (char cell : row) {
                sb
                        .append(cell)
                        .append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
    }
}
