package com.arshak.blog.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String anons;

    @Column(name = "full_text")
    private String fullText;

    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    private User creator;


    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "image")
    private String image;

}
