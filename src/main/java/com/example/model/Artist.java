package com.example.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "artists")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String genre;
    private String listeners;
    private String filename;

    @OneToMany(mappedBy = "artists", cascade = CascadeType.REMOVE)
    private List<Track> tracks;
}
