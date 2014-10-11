package com.gwt.wizard.qrcode;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class GetImagesFromPDF
{
    public static final Logger log = Logger.getLogger(GetImagesFromPDF.class.getName());

    public static String encode(InputStream is)
    {
        String result = null;

        try
        {
            PDDocument document = PDDocument.load(is);

            for (Object pdPageObject : document.getDocumentCatalog().getAllPages())
            {
                PDPage page = (PDPage) pdPageObject;
                PDResources pdResources = page.getResources();

                Map pageImages = pdResources.getImages();
                if (pageImages != null)
                {

                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext())
                    {
                        String key = (String) imageIter.next();
                        PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);

                        int height = pdxObjectImage.getHeight();
                        int width = pdxObjectImage.getWidth();
                        log.info("height:" + height);
                        log.info("width:" + width);

                        byte[] image = pdxObjectImage.getPDStream().getByteArray();

                        LuminanceSource source = new GAELuminanceSource(width, height, image);

                        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Map hintMap = new HashMap();
                        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                                hintMap);
                        result = qrCodeResult.getText();
                        log.info("qr found:" + result);
                    }

                }

            }
        }
        catch (Exception ex)
        {
            log.info("no qr found");
        }

        return result;
    }
}
