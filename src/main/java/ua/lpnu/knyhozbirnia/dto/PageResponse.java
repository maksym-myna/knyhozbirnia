package ua.lpnu.knyhozbirnia.dto;

import org.springframework.data.util.Streamable;

public record PageResponse<T extends Streamable<?>>(T response, long pageRecordCount, long recordCount){
    public PageResponse(T response, long recordCount) {
        this(response, response.stream().count(), recordCount);
    }
}
