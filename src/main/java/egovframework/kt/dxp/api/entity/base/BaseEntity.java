package egovframework.kt.dxp.api.entity.base;

import egovframework.kt.dxp.api.common.jpa.annotations.SearchableField;
import egovframework.kt.dxp.api.entity.converter.LocalDateTimeConverter;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author GEONLEE
 * @since 2024-08-01
 */
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Column(name = "UPDT_DT")
//    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updateDate;

    @Column(name = "CRT_DT", updatable = false)
//    @Temporal(value = TemporalType.TIMESTAMP)
    @CreatedDate
    @SearchableField
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createDate;
}
