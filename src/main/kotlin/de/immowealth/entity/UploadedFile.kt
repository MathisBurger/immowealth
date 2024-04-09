package de.immowealth.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

/**
 * File that has been uploaded to server
 */
@Entity
class UploadedFile : BaseEntity(), Archivable {

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
    override fun toString(): String {
        return this.fileRoot.toString() + this.fileName.toString()
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }
}