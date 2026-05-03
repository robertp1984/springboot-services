package org.softwarecave.springboottours.tour.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tour_package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_package_seq")
    @SequenceGenerator(name = "tour_package_seq", sequenceName = "tour_package_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    @NotBlank
    private String code;

    @Column(name = "name")
    @NotBlank
    private String name;

    public TourPackage(String code, String name) {
        this.id = null;
        this.code = code;
        this.name = name;
    }

}
