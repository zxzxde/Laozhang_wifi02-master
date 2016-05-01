package space.arstar.laozhang_wifi01;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

public class EncodingHandler {
        private static final int BLACK = 0xff000000;
        //老张：自定义无信息区域使用颜色，因为imageView对于无信息的像素自我显示为白色，而生成的图片image中
        //无信息含量的像素自动用黑色填充，所以会导致生成的图片全为黑色
        private static final int WHITE= 0xFFFFFFFF;
        private static final int RED= 0xFFFF0000;
        private static final int YELLOW= 0xFDFFE100;
        private static final int GREEN= 0xC000FC00;
        private static final int PURPlE= 0xC0740AFD;
        private static final int PINK= 0xC0FF00AE;
        private static final int SYK= 0xC00B8CFD;

        public static Bitmap createQRCode(String str, int widthAndHeight,int color) throws WriterException {
                Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                BitMatrix matrix = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
                int width = matrix.getWidth();
                int height = matrix.getHeight();
                int[] pixels = new int[width * height];

                for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                                if (matrix.get(x, y)) {
                                       // pixels[y * width + x] = BLACK;
                                        pixels[y * width + x] = color;
                                }
                                else {
                                        pixels[y * width + x] = WHITE;
                                }
                        }
                }
                Bitmap bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                return bitmap;
        }
        }
