package org.strokova.gamestore.service;

import java.io.File;

/**
 * 09.11.2016.
 */

// is used in ApplicationPackageService to parse and save uploaded applications
// package-private
final class ZipDescriptor {
    private String name;
    private String appPackage;
    private String image128Name;
    private String image512Name;
    private File image128File;
    private File image512File;

    static final String TXT_NAME = "name:";
    static final String TXT_PACKAGE = "package:";
    static final String TXT_IMAGE_128 = "picture_128:";
    static final String TXT_IMAGE_512 = "picture_512:";

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getAppPackage() {
        return appPackage;
    }

    void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    File getImage128File() {
        return image128File;
    }

    void setImage128File(File image128File) {
        this.image128File = image128File;
    }

    File getImage512File() {
        return image512File;
    }

    void setImage512File(File image512File) {
        this.image512File = image512File;
    }

    String getImage128Name() {
        return image128Name;
    }

    void setImage128Name(String image128Name) {
        this.image128Name = image128Name;
    }

    String getImage512Name() {
        return image512Name;
    }

    void setImage512Name(String image512Name) {
        this.image512Name = image512Name;
    }
}
