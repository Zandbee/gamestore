package org.strokova.gamestore.form;

import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author vstrokova, 06.10.2016.
 */

// this class is used to validate user input on submitting 'upload' page
public class ApplicationForm {
    @Size(max = 40, message = "{userGivenName.invalidSize}")
    private String userGivenName;

    @Size(max = 300, message = "{description.invalidSize}")
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private MultipartFile file;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
