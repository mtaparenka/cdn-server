package com.mtaparenka.cdnserver.metadata;

public enum Type {
    BYTE(1),
    ASCII(2),
    SHORT(3),
    LONG(4);
    //TODO rational, undefined, slong, srational

    public final int value;

    Type(int value) {
        this.value = value;
    }
}
