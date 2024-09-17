package io.go7.hackathon.bedrockproxy.beans;

import io.go7.commons.flight.PassengerTypeGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerQuantity {

    private PassengerTypeGroup type;
    private int quantity;
}
