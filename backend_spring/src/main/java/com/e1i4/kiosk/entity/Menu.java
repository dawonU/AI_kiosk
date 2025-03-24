package com.e1i4.kiosk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int menuId;
    private String category;
    private int categoryId;
    private int daypartId;
    private int price;
    private String imgUrl;
    @Column(name = "sub_menus", columnDefinition = "json")
    private String subMenus;
}