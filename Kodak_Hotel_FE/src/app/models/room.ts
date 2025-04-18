import { Bookings } from "./bookings";

export interface Room {
    roomId: string,
    roomType: string,
    roomPhotoUrl: string,
    description: string,
    roomPrice: number,
    bookings: Bookings[]
}

export interface RoomFilter {
    checkInDate: string,
    checkOutDate: string,
    roomType: string
}