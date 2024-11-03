package com.Kodakdev.KodakHotel.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ROOM")
@AttributeOverride(name = "id", column = @Column(name = "ROOM_ID"))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROOM_TYPE")
    private String roomType;

    @Column(name = "ROOM_PRICE")
    private BigDecimal roomPrice;

    @Column(name = "ROOM_PHOTO_URL")
    private String roomPhotoUrl;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomType='" + roomType + '\'' +
                ", roomPrice=" + roomPrice +
                ", roomPhotoUrl='" + roomPhotoUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
