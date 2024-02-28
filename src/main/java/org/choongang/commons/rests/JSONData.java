package org.choongang.commons.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JSONData {
    private boolean success = true;
    private HttpStatus status = HttpStatus.OK;
    private Object messages;
    @NonNull
    private Object data;
}
