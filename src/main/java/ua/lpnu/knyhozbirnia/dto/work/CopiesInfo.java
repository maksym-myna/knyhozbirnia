package ua.lpnu.knyhozbirnia.dto.work;

import ua.lpnu.knyhozbirnia.model.WorkMedium;

public record CopiesInfo (
        Integer quantity,
        WorkMedium medium
) {

}
