package org.softwarecave.springboottours.tour.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_seq")
    @SequenceGenerator(name = "tour_seq", sequenceName = "tour_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    @NotBlank
    private String code;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Difficulty difficulty;

    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Region region;

    @ManyToOne
    @JoinColumn(name = "tour_package_id")
    @NotNull
    private TourPackage tourPackage;
}
