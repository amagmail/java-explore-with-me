package ru.practicum.explorewithme.statdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatDto {

    private String app;
    private String uri;
    private Integer hits;

}
