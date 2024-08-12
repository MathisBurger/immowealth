package de.immowealth.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

@Entity
class RenterStatistics : BaseEntity() {

    @ManyToMany
    @JoinTable(name = "renter_join_renterStatistics",
        joinColumns = [(JoinColumn(name = "renter_id", referencedColumnName = "id"))],
        inverseJoinColumns = [(JoinColumn(name = "object_id", referencedColumnName = "id"))]
    )
    var history: MutableList<RealEstateObject> = mutableListOf();

    
}