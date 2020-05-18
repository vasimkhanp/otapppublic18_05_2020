package com.otapp.net.Events.interfac;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;

import java.util.Map;

public interface SeatProcessResponseIntfceTT {
    public void seatGetTicketToatalPrice(String totalPrice, Map<String, GetTicketTypeResponse.GetTickets> tempTicketMap);
}
