package net.nexarys.alicushions.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class ImageUtils {
    private static final Cache<File, String> hash_cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

    public static String getImageHash(File file) {
        String hash = hash_cache.getIfPresent(file);

        if(hash != null)
            return hash;

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage getPart(int index, BufferedImage image) {
        return switch (index) {
            case 0 -> part1(image);
            case 1 -> part2(image);
            case 2 -> part3(image);
            case 3 -> part4(image);
            default -> null;
        };
    }

    public static BufferedImage adjustSize(BufferedImage img) {
        BufferedImage sides = img.getSubimage(0, 16, 64, 4);
        Graphics2D g = img.createGraphics();
        g.drawImage(scale(sides, 64, 8), 0, 16, null);
        g.dispose();
        return img;
    }

    public static BufferedImage part1(BufferedImage main) {
        BufferedImage result = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        copy(main, result, 16, 8, 8, 0); //RU
        copy(main, result, 32, 8, 16, 0); //RD
        copy(main, result, 16, 16, 8, 8); //R1
        copy(main, result, 8, 16, 0, 8); //R2

        copy(main, result, 16, 8, 16, 8);
        copy(main, result, 16, 8, 24, 8);
        return result;
    }

    public static BufferedImage part2(BufferedImage main) {
        BufferedImage result = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        copy(main, result, 16, 0, 8, 0); //NU
        copy(main, result, 32, 0, 16, 0); //ND
        copy(main, result, 56, 16, 24, 8); //N1
        copy(main, result, 0, 16, 0, 8); //N2

        copy(main, result, 16, 0, 8, 8);
        copy(main, result, 16, 0, 16, 8);
        return result;
    }

    public static BufferedImage part3(BufferedImage main) {
        BufferedImage result = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        copy(main, result, 24, 0, 8, 0); //VU
        copy(main, result, 40, 0, 16, 0); //VD
        copy(main, result, 48, 16, 24, 8); //V1
        copy(main, result, 40, 16, 16, 8); //V2

        copy(main, result, 24, 0, 0, 8);
        copy(main, result, 24, 0, 8, 8);
        return result;
    }

    public static BufferedImage part4(BufferedImage main) {
        BufferedImage result = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        copy(main, result, 24, 8, 8, 0); //AU
        copy(main, result, 40, 8, 16, 0); //AD
        copy(main, result, 32, 16, 16, 8); //A1
        copy(main, result, 24, 16, 8, 8); //A2

        copy(main, result, 24, 8, 0, 8);
        copy(main, result, 24, 8, 24, 8);
        return result;
    }

    public static void copy(
            BufferedImage origen,
            BufferedImage destino,
            int origenX,
            int origenY,
            int destinoX,
            int destinoY) {

        BufferedImage cuadro = origen.getSubimage(origenX, origenY, 8, 8);

        Graphics2D g = destino.createGraphics();
        g.drawImage(cuadro, destinoX, destinoY, null);
        g.dispose();
    }

    private static BufferedImage scale(BufferedImage img, double x, double y) {
        int w = (int) x;
        int h = (int) y;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = AffineTransform.getScaleInstance(x / img.getWidth(), y / img.getHeight());
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, out);
    }
}
