package io.go7.hackathon.bedrockproxy.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerQuantity {

    private String type;
    private int quantity;
}
