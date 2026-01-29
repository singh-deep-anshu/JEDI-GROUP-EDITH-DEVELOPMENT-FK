package com.flipfit.rest.resources;

import com.flipfit.bean.Booking;
import com.flipfit.dao.BookingDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final BookingDAO bookingDao;

    public BookingResource(BookingDAO bookingDao) {
        this.bookingDao = bookingDao;
    }

    @POST
    public Response createBooking(Booking booking) {
        if (booking.getBookingId() == null || booking.getBookingId().isEmpty()) {
            booking.setBookingId(UUID.randomUUID().toString());
        }
        if (booking.getBookingDate() == null) {
            booking.setBookingDate(new Date());
        }
        if (booking.getStatus() == null) {
            // default to CONFIRMED if not set
            try { booking.setStatus(com.flipfit.bean.BookingStatus.CONFIRMED); } catch (Exception ignored) {}
        }
        boolean ok = bookingDao.createBooking(booking);
        if (ok) return Response.status(Response.Status.CREATED).entity(booking).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create booking").build();
    }

    @GET
    @Path("/{id}")
    public Response getBooking(@PathParam("id") String id) {
        Booking b = bookingDao.getBookingById(id);
        if (b == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(b).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getBookingsByUser(@PathParam("userId") String userId) {
        List<Booking> list = bookingDao.getBookingsByUserId(userId);
        return Response.ok(list).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") String id) {
        boolean ok = bookingDao.deleteBooking(id);
        if (ok) return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
    }
}
