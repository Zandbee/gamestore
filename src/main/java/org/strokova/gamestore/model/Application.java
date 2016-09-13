package org.strokova.gamestore.model;

import javax.persistence.*;

/**
 * author: Veronika, 9/4/2016.
 */
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "package")
    private String appPackage; // max = 300
    // app name from zip
    private String name; // max = 40
    // app name from user input
    @Column(name = "given_name")
    private String userGivenName; // max = 40
    private String description; // max = 300
    @Column(name = "file_path")
    private String filePath; // max = 500
    @Column(name = "image_128_path")
    private String image128Path; // max = 500
    @Column(name = "image_512_path")
    private String image512Path; // max = 500
    private Category category;
    @Column(name = "download_num")
    private Integer downloadNumber;

    protected Application() {}

    public Application(Integer id, String appPackage, String name, String userGivenName, String description, String filePath, String image128Path, String image512Path, Category category, Integer downloadNumber) {
        this.id = id;
        this.appPackage = appPackage;
        this.name = name;
        this.userGivenName = userGivenName;
        this.description = description;
        this.filePath = filePath;
        this.image128Path = image128Path;
        this.image512Path = image512Path;
        this.category = category;
        this.downloadNumber = downloadNumber;
    }

    public Application(String userGivenName, String description) {
        this.userGivenName = userGivenName;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserGivenName() {
        return userGivenName;
    }

    public void setUserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImage128Path() {
        return image128Path;
    }

    public void setImage128Path(String image128Path) {
        this.image128Path = image128Path;
    }

    public String getImage512Path() {
        return image512Path;
    }

    public void setImage512Path(String image512Path) {
        this.image512Path = image512Path;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getDownloadNumber() {
        return downloadNumber;
    }

    public void setDownloadNumber(Integer downloadNumber) {
        this.downloadNumber = downloadNumber;
    }
}
