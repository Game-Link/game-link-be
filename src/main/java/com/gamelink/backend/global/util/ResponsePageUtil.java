package com.gamelink.backend.global.util;

import com.gamelink.backend.global.base.ResponsePage;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ResponsePageUtil {

    public static <T> ResponsePage<T> toResponsePage(List<T> content, int page, int size, long totalElements) {
        Pageable pageable = PageRequest.of(page, size);

        PageImpl<T> pageImpl = new PageImpl<>(content, pageable, totalElements);

        return new ResponsePage<>(pageImpl);
    }
}
