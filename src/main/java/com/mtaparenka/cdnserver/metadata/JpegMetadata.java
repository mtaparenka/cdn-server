package com.mtaparenka.cdnserver.metadata;

import java.util.Arrays;

public class JpegMetadata {
    public final String make;
    public final String model;
    public final String dateTime;

    private JpegMetadata(String make, String model, String dateTime) {
        this.make = make;
        this.model = model;
        this.dateTime = dateTime;
    }

    public static JpegMetadata read(byte[] data) {
        var tiffHeaderEnd = 0;
        var i = 0;
        while (tiffHeaderEnd == 0) {
            //EXIF
            if (data[i] == 0x45 && data[i + 1] == 0x78 && data[i + 2] == 0x69 && data[i + 3] == 0x66) {
                tiffHeaderEnd = i + 6;
            }

            i++;
        }

        var make = readAsciiTag(Tag.MAKE, data, tiffHeaderEnd);
        var model = readAsciiTag(Tag.MODEL, data, tiffHeaderEnd);
        var dateTime = readAsciiTag(Tag.DATE_TIME, data, tiffHeaderEnd);

        return new JpegMetadata(make, model, dateTime);
    }

    private static String readAsciiTag(Tag tag, byte[] data, int tiffHeaderEnd) {
        var tagStart = 0;
        var i = 0;
        var tagAddressHex = Integer.toHexString(tag.address);
        byte tagFirstByte = (byte) (tagAddressHex.length() % 2 == 0
                ? Integer.parseInt(tagAddressHex.substring(0, 2), 16)
                : Integer.parseInt(tagAddressHex.substring(0, 1), 16));
        byte tagSecondByte = (byte) (tagAddressHex.length() % 2 == 0
                ? Integer.parseInt(tagAddressHex.substring(2, 4), 16)
                : Integer.parseInt(tagAddressHex.substring(1, 3), 16));

        try {
            while (tagStart == 0) {
                if (data[i] == tagFirstByte && data[i + 1] == tagSecondByte && data[i + 2] == 0x00 && data[i + 3] == tag.type.value) {
                    tagStart = i;
                }

                i++;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        var tagOffsetIndex = tagStart + 8;
        var tagOffsetStart = ((0xFF & data[tagOffsetIndex]) << 24)
                | ((0xFF & data[tagOffsetIndex + 1]) << 16)
                | ((0xFF & data[tagOffsetIndex + 2]) << 8)
                | (0xFF & data[tagOffsetIndex + 3])
                + tiffHeaderEnd;

        var tagOffsetEnd = tagOffsetStart;

        while (data[tagOffsetEnd] != 0x00) {
            tagOffsetEnd++;
        }

        return new String(Arrays.copyOfRange(data, tagOffsetStart, tagOffsetEnd));
    }
}
