package org.jetlinks.marketplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 能力标签信息.
 *
 * @author zhouhao
 * @since 2.12
 */
@Getter
@Setter
public class CapabilityTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String categoryId;

    private String description;

//    /**
//     * 按展示字段和语言代码组织的国际化文本。
//     */
//    private Map<String, Map<String, String>> i18nMessages;

    private String i18nName;

    private String i18nDescription;

    /**
     * 展示用图标：可为图片 URL、或颜色值（如 #RRGGBB、rgb()），由前端解析样式。
     */
    private String icon;

    private List<CapabilityTag> children;
}
