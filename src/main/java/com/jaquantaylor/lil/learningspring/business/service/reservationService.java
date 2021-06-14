package com.jaquantaylor.lil.learningspring.business.service;

import com.jaquantaylor.lil.learningspring.business.domain.RoomReservation;
import com.jaquantaylor.lil.learningspring.data.entity.Guest;
import com.jaquantaylor.lil.learningspring.data.entity.Reservation;
import com.jaquantaylor.lil.learningspring.data.entity.Room;
import com.jaquantaylor.lil.learningspring.data.repository.GuestRepository;
import com.jaquantaylor.lil.learningspring.data.repository.ReservationRepository;
import com.jaquantaylor.lil.learningspring.data.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class reservationService
{
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public reservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(Date date){
        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long, RoomReservation> roomReservationsMap = new HashMap<>();
        rooms.forEach( room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getRoomId());
            roomReservation.setRoomName(room.getRoomName());
            roomReservation.setRoomNumber(room.getRoomNumber());
            roomReservationsMap.put(room.getRoomId(),roomReservation);
        }        );
        Iterable<Reservation> reservations = this.reservationRepository.findReservationByReservationDate(new java.sql.Date(date.getTime()));
        reservations.forEach(
                reservation -> {
                    RoomReservation roomReservation = roomReservationsMap.get(reservation.getRoomId());
                    roomReservation.setDate(date);
                    Guest guest = this.guestRepository.findById(reservation.getGuestID()).get();
                    roomReservation.setFirstName(guest.getFirstName());
                    roomReservation.setLastName(guest.getLastName());
                    roomReservation.setGuestId(guest.getGuestID());
                }
        );
        List<RoomReservation> roomReservations = new ArrayList<>();
        for(Long id: roomReservationsMap.keySet()){
            roomReservations.add(roomReservationsMap.get(id));
        }
        return roomReservations;
    }
}
