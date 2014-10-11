package com.gwt.wizard.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.gwt.wizard.qrcode.GetImagesFromPDF;

public class GetImagesFromPDFTest
{

    @Test
    public void test() throws IOException
    {
        String result = GetImagesFromPDF.encode(new FileInputStream(new File("war/taxi_order.pdf")));
        Assert.assertTrue("5207287069147136".equals(result));
    }
}
