package org.jetlinks.marketplace;

import lombok.Getter;
import lombok.Setter;
import org.jetlinks.marketplace.enums.LicenseType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 能力市场搜索请求.
 *
 * @author zhouhao
 * @since 2.12
 */
@Getter
@Setter
public class CapabilitySearchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String keyword;
    private String type;
    private String domain;

    private List<String> tagClassifiers;
    private List<String> tags;
    private String classifier;

    /** 是否启用分页，默认启用。 */
    private boolean paging = true;
    private int pageIndex = 0;
    private int pageSize = 20;
}
