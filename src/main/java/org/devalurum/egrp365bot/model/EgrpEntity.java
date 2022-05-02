package org.devalurum.egrp365bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@AllArgsConstructor
@Data
public class EgrpEntity {

    private String cadastralNumber;
    private String address;
    private double latitude;
    private double longitude;

    private Map<String, String> fullInfo;

}
