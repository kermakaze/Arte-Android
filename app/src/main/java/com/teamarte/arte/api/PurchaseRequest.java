package com.teamarte.arte.api;

public class PurchaseRequest {
   private int ticketId;
   private int quantity;

    public PurchaseRequest(int ticketId, int quantity) {
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

}

