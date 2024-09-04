package com.gamelink.backend.global.error.repository.spec;

import com.gamelink.backend.global.error.domain.model.ErrorSource;
import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import org.springframework.data.jpa.domain.Specification;

public class ErrorLogSpec {

    public static Specification<ErrorLogEntity> withSource(ErrorSource source) {
        if (source == null) {
            return Specification.where(null);
        }
        return (root, query, builder) ->
                builder.equal(root.get("source"), source);
    }
}
