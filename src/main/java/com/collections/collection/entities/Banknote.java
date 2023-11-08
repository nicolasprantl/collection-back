package com.collections.collection.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "banknotes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Banknote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @Column(name = "country")
    private String country;

    @Column(name = "denomination")
    private Double denomination;

    @Column(name = "series")
    private String series;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "banknote", cascade = CascadeType.ALL)
    @JsonManagedReference
    private BanknoteImage image;

}

