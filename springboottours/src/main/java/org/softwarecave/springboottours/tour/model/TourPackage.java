package org.softwarecave.springboottours.tour.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TOUR_PACKAGE")
public class TourPackage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_package_seq")
    @SequenceGenerator(name = "tour_package_seq", sequenceName = "tour_package_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "code")
    @NotNull
    @NotBlank
    private String code;

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;

    public TourPackage(String code, String name) {
        this.id = null;
        this.code = code;
        this.name = name;
    }


}
