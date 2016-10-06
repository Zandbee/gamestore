package org.strokova.gamestore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * author: Veronika, 9/4/2016.
 */
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 300)
    @Column(name = "package")
    private String appPackage;

    // app name from zip
    @NotNull
    @Size(max = 40)
    private String name;

    // app name from user input
    @NotNull
    @Size(max = 40)
    @Column(name = "given_name")
    private String userGivenName;

    @Size(max = 300)
    private String description;

    @NotNull
    @Size(max = 500)
    @Column(name = "file_path")
    private String filePath;

    @Size(max = 500)
    @Column(name = "image_128_path")
    private String image128Path;

    @Size(max = 500)
    @Column(name = "image_512_path")
    private String image512Path;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "download_num")
    private Integer downloadNumber;

    @Column(name = "time_uploaded")
    private Timestamp timeUploaded;

    public Application() {}

    public Integer getId() {
        return id;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getName() {
        return name;
    }

    public String getUserGivenName() {
        return userGivenName;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getImage128Path() {
        return image128Path;
    }

    public String getImage512Path() {
        return image512Path;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getDownloadNumber() {
        return downloadNumber;
    }

    public Timestamp getTimeUploaded() {
        return timeUploaded;
    }

    public Application setId(Integer id) {
        this.id = id;
        return this;
    }

    public Application setAppPackage(String appPackage) {
        this.appPackage = appPackage;
        return this;
    }

    public Application setName(String name) {
        this.name = name;
        return this;
    }

    public Application setUserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
        return this;
    }

    public Application setDescription(String description) {
        this.description = description;
        return this;
    }

    public Application setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public Application setImage128Path(String image128Path) {
        this.image128Path = image128Path;
        return this;
    }

    public Application setImage512Path(String image512Path) {
        this.image512Path = image512Path;
        return this;
    }

    public Application setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Application setDownloadNumber(Integer downloadNumber) {
        this.downloadNumber = downloadNumber;
        return this;
    }
}
