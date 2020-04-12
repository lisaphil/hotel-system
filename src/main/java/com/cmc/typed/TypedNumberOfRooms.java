package com.cmc.typed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor @Getter
public class TypedNumberOfRooms {
    private int suiteInt;
    private int juniourInt;
    private int singleInt;
    private int doubleInt;
    private int doubleWithExtra;
}
