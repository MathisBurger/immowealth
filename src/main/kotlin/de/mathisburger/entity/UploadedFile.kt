package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

/**
 * File that has been uploaded to server
 */
@Entity
class UploadedFile : BaseEntity() {

    /**
     * Name of the file
     */
    var fileName: String? = null;

    /**
     * Root of the file
     */
    var fileRoot: String? = null;

    /**
     * The real file path the file is stored to
     */
    var realFilePath: String? = null;

    /**
     * The real estate object the file is mapped to
     */
    @ManyToOne
    var realEstateObject: RealEstateObject? = null;
}