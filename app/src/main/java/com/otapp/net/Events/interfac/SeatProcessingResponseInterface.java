package com.otapp.net.Events.interfac;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;

import java.util.Map;

public interface SeatProcessingResponseInterface {
    public void seatToatalPrice(String totalPrice, Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap);

}
