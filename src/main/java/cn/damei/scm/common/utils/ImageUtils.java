package cn.damei.scm.common.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

/**
 * 图片处理工具类
 * Utilities methods for image manipulation. It does not support writting of GIF images, but it can read from. GIF images will be saved as PNG.
 */

public final class ImageUtils {
    public static final int IMAGE_UNKNOWN = -1;//bmp类型
    public static final int IMAGE_JPEG = 0; //jpg 或 jpeg图片
    public static final int IMAGE_PNG = 1; //png类型
    public static final int IMAGE_GIF = 2;//gif类型

    @SuppressWarnings("unused")
    private static String[] imageFormatArray = new String[]{".jpg", ".gif", ".png", ".bmp", ".jpeg"};


    /**
     * 返回压缩后的图片，如果imgName参数指定的图片的 高度<maxWidth && 宽度<maxHeight 则返回null,表示原图片不用压缩(该方法比createZoomImage 快6倍  推荐使用该方法压缩)
     * Resizes an image
     *
     * @param imgFile         The image name to resize. Must be the complet path to the file
     * @param type            int
     * @param maxWidth        The image's max width
     * @param maxHeight       The image's max height
     * @param ratioConsistent 表示是否按照原来图片的宽高比 压缩
     * @return A resized <code>BufferedImage</code>
     * 例如：
     * 修改到合适大小并保存头像
     * int maxWidth = SystemGlobals.getSettings().getAvatarMaxWidth();
     * int maxHeight = SystemGlobals.getSettings().getAvatarMaxHeight();
     * BufferedImage image = ImageUtils.resizeImage(avatarFinalFileName, type, maxWidth,
     * maxHeight,true);
     * if(image!=null)
     * ImageUtils.saveImage(image, avatarFinalFileName, type);
     */
    public static BufferedImage resizeImage(String imgFile, int type, int maxWidth, int maxHeight, boolean ratioConsistent) {
        try {
            return resizeImage(ImageIO.read(new File(imgFile)), type, maxWidth, maxHeight, ratioConsistent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回压缩后的图片，如果image参数指定的图片的 高度<maxWidth && 宽度<maxHeight  则返回null,表示原图片不用压缩
     *
     * @param ratioConsistent 表示是否按照原来图片的宽高比 压缩
     * @param type
     */
    public static BufferedImage resizeImage(BufferedImage image, int type, int resizeToWidth, int resizeToHeight, boolean ratioConsistent) {
        // Original size
        if (image == null)
            return null;
        int imageRealWidth = image.getWidth(null);
        int imageRealHeight = image.getHeight(null);

        int newWidth = 0;
        int newHeight = 0;
        if (!(imageRealWidth > resizeToWidth || imageRealHeight > resizeToHeight))
            return null;

        // 如果是等比缩放
        if (ratioConsistent) {
            // 为等比缩放计算输出的图片宽度及高度   
            double scaleX = ((double) imageRealWidth) / (double) resizeToWidth + 0.1;
            double scaleY = ((double) imageRealHeight) / (double) resizeToHeight + 0.1;
            // 根据缩放比率大的进行缩放控制   
            double scale = Math.max(scaleX, scaleY);
            newWidth = (int) (imageRealWidth / scale);
            newHeight = (int) (imageRealHeight / scale);
        } else {
            Dimension largestDimension = new Dimension(resizeToWidth, resizeToHeight);
            float aspectRatio = (float) imageRealWidth / imageRealHeight;
            if ((float) largestDimension.width / largestDimension.height > aspectRatio) {
                largestDimension.width = (int) Math.ceil(largestDimension.height * aspectRatio);
            } else {
                largestDimension.height = (int) Math.ceil(largestDimension.width / aspectRatio);
            }
            newWidth = largestDimension.width;
            newHeight = largestDimension.height;
        }
        return createHeadlessSmoothBufferedImage(image, type, newWidth, newHeight);
    }

    /**
     * Saves an image to the disk.
     *
     * @param image      The image to save
     * @param toFileName The filename to use
     * @param type       The image type. Use <code>ImageUtils.IMAGE_JPEG</code> to save as JPEG images, or <code>ImageUtils.IMAGE_PNG</code> to save as PNG.
     * @return <code>false</code> if no appropriate writer is found
     */
    public static boolean saveImage(BufferedImage image, String toFileName, int type) {
        try {
            return ImageIO.write(image, type == IMAGE_JPEG ? "jpg" : "png", new File(toFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compress and save an image to the disk. Currently this method only supports JPEG images.
     *
     * @param image      The image to save
     * @param toFileName The filename to use
     * @param type       The image type. Use <code>ImageUtils.IMAGE_JPEG</code> to save as JPEG images, or <code>ImageUtils.IMAGE_PNG</code> to save as PNG.
     */
    public static void saveCompressedImage(BufferedImage image, String toFileName, int type) {
        try {
            if (type == IMAGE_PNG) {
                throw new UnsupportedOperationException("PNG compression not implemented");
            }

            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer;
            writer = iter.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(toFileName));
            writer.setOutput(ios);

            ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());

            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwparam.setCompressionQuality(0.7F);

            writer.write(null, new IIOImage(image, null, null), iwparam);

            ios.flush();
            writer.dispose();
            ios.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a <code>BufferedImage</code> from an <code>Image</code>. This method can function on a completely headless system. This especially includes Linux and Unix systems that do not have
     * the X11 libraries installed, which are required for the AWT subsystem to operate. This method uses nearest neighbor approximation, so it's quite fast. Unfortunately, the result is nowhere near
     * as nice looking as the createHeadlessSmoothBufferedImage method.
     *
     * @param image The image to convert
     * @param w     The desired image width
     * @param h     The desired image height
     * @param type  int
     * @return The converted image
     */
    public static BufferedImage createHeadlessBufferedImage(BufferedImage image, int type, int width, int height) {
        if (type == ImageUtils.IMAGE_PNG && hasAlpha(image)) {
            type = BufferedImage.TYPE_INT_ARGB;
        } else {
            type = BufferedImage.TYPE_INT_RGB;
        }

        BufferedImage bi = new BufferedImage(width, height, type);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bi.setRGB(x, y, image.getRGB(x * image.getWidth() / width, y * image.getHeight() / height));
            }
        }

        return bi;
    }

    /**
     * Creates a <code>BufferedImage</code> from an <code>Image</code>. This method can function on a completely headless system. This especially includes Linux and Unix systems that do not have
     * the X11 libraries installed, which are required for the AWT subsystem to operate. The resulting image will be smoothly scaled using bilinear filtering.
     *
     * @param source The image to convert
     * @param w      The desired image width
     * @param h      The desired image height
     * @param type   int
     * @return The converted image
     */
    public static BufferedImage createHeadlessSmoothBufferedImage(BufferedImage source, int type, int width, int height) {
        if (type == ImageUtils.IMAGE_PNG && hasAlpha(source)) {
            type = BufferedImage.TYPE_INT_ARGB;
        } else {
            type = BufferedImage.TYPE_INT_RGB;
        }

        BufferedImage dest = new BufferedImage(width, height, type);

        int sourcex;
        int sourcey;

        double scalex = (double) width / source.getWidth();
        double scaley = (double) height / source.getHeight();

        int x1;
        int y1;

        double xdiff;
        double ydiff;

        int rgb;
        int rgb1;
        int rgb2;

        for (int y = 0; y < height; y++) {
            sourcey = y * source.getHeight() / dest.getHeight();
            ydiff = scale(y, scaley) - sourcey;

            for (int x = 0; x < width; x++) {
                sourcex = x * source.getWidth() / dest.getWidth();
                xdiff = scale(x, scalex) - sourcex;

                x1 = Math.min(source.getWidth() - 1, sourcex + 1);
                y1 = Math.min(source.getHeight() - 1, sourcey + 1);

                rgb1 = getRGBInterpolation(source.getRGB(sourcex, sourcey), source.getRGB(x1, sourcey), xdiff);
                rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source.getRGB(x1, y1), xdiff);

                rgb = getRGBInterpolation(rgb1, rgb2, ydiff);

                dest.setRGB(x, y, rgb);
            }
        }
        return dest;
    }

    private static double scale(int point, double scale) {
        return point / scale;
    }

    private static int getRGBInterpolation(int value1, int value2, double distance) {
        int alpha1 = (value1 & 0xFF000000) >>> 24;
        int red1 = (value1 & 0x00FF0000) >> 16;
        int green1 = (value1 & 0x0000FF00) >> 8;
        int blue1 = (value1 & 0x000000FF);

        int alpha2 = (value2 & 0xFF000000) >>> 24;
        int red2 = (value2 & 0x00FF0000) >> 16;
        int green2 = (value2 & 0x0000FF00) >> 8;
        int blue2 = (value2 & 0x000000FF);

        int rgb = ((int) (alpha1 * (1.0 - distance) + alpha2 * distance) << 24) | ((int) (red1 * (1.0 - distance) + red2 * distance) << 16)
                | ((int) (green1 * (1.0 - distance) + green2 * distance) << 8) | (int) (blue1 * (1.0 - distance) + blue2 * distance);

        return rgb;
    }

    /**
     * Determines if the image has transparent pixels.
     *
     * @param image The image to check for transparent pixel.s
     * @return <code>true</code> of <code>false</code>, according to the result
     */
    public static boolean hasAlpha(Image image) {
        try {
            PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
            pg.grabPixels();

            return pg.getColorModel().hasAlpha();
        } catch (InterruptedException e) {
            return false;
        }
    }


    /**
     * 判断文件是否为 图片文件
     */
    public static boolean isImageFile(String extName) {
        if (extName == null || extName.length() < 1)
            return false;
        return "jpg,jpeg,gif,bmp,png".indexOf(extName.toLowerCase()) > -1;
    }
}