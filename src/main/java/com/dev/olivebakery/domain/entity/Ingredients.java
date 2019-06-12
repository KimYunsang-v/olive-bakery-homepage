package com.dev.olivebakery.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredients_id")
    private Long id;

    private String name;

    private String origin;

//    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.MERGE})
//    @JsonBackReference
//    @JoinTable(
//            name = "bread_ingredients",
//            joinColumns = @JoinColumn(name = "ingredients_id"),
//            inverseJoinColumns = @JoinColumn(name = "bread_id")
//    )
//    private List<Bread> breads = new ArrayList<>();
}