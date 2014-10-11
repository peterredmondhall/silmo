package com.gwt.wizard.qrcode;

import com.google.zxing.LuminanceSource;

public class GAELuminanceSource extends LuminanceSource
{
    byte[] matrix;

    protected GAELuminanceSource(int width, int height, byte[] image)
    {
        super(width, height);

        int p1 = 0;
        int p2 = 0;
        matrix = new byte[image.length * 8];
        for (int row = 0; row < height; row++)
        {
            for (int w = 0; w < width / 8; w++)
            {
                matrix[p2++] = test(image, p1, 0x80, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x40, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x20, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x10, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x08, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x04, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x02, row) ? (byte) 0xff : (byte) 0;
                matrix[p2++] = test(image, p1, 0x01, row) ? (byte) 0xff : (byte) 0;
                p1++;
            }
        }

    }

    private boolean test(byte[] image, int p, int test, int row)
    {
        boolean isBlack = ((image[p] & test) > 0);
        if (row < 1600)
        {
            return true;
        }
        return isBlack;
    }

    @Override
    public byte[] getMatrix()
    {
        return matrix;
    }

    @Override
    public byte[] getRow(int y, byte[] row)
    {
        if (y < 0 || y >= getHeight())
        {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width)
        {
            row = new byte[width];
        }

        System.arraycopy(matrix, y * width, row, 0, width);
        return row;
    }
}
