package com.otapp.net.Events.interfac;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;

public interface EventTicketClassInterface {
    void selectedFloorId(EventsListResponse.Events.EventTickets eventTickets);

    void selectedTicketList(GetTicketTypeResponse getTickets);
}
