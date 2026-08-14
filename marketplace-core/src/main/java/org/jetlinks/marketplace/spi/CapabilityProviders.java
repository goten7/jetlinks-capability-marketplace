package org.jetlinks.marketplace.spi;

import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CapabilityProvider} 注册表
 *
 * @author zhouhao
 * @since 2.12
 */
public final class CapabilityProviders {

    private static final Map<String, CapabilityProvider> BY_TYPE_ID = new ConcurrentHashMap<>();

    private CapabilityProviders() {
    }

    public static void register(CapabilityProvider provider) {
        BY_TYPE_ID.put(provider.getId(), provider);
    }

    public static void unregister(String capabilityTypeId) {
        BY_TYPE_ID.remove(capabilityTypeId);
    }

    public static Optional<CapabilityProvider> get(String id) {
        return Optional.ofNullable(BY_TYPE_ID.get(id));
    }

    public static CapabilityProvider getOrThrow(String id) {
        return get(id)
            .orElseThrow(() -> new UnsupportedOperationException("unsupported provider " + id));
    }

    public static Collection<CapabilityProvider> all() {
        return BY_TYPE_ID.values();
    }

    /**
     * 根据安装记录中的 Provider ID 解析资源对应的资产类型.
     *
     * <p>Provider 缺失或资源没有资产类型映射时返回空 Mono。调用方可按自身规则回退处理；
     * 已解析 Provider 的默认行为由 {@link CapabilityProvider#resolveAssetType(String)} 决定。</p>
     *
     * @param providerId Provider ID，可能为空
     * @param resourceType 能力资源类型，可能为空
     * @return 资产类型 ID
     * @see CapabilityProvider#resolveAssetType(String)
     * @since 2.12
     */
    public static Mono<String> resolveAssetType(String providerId, String resourceType) {
        return Mono
            .justOrEmpty(providerId)
            .map(CapabilityProviders::get)
            .flatMap(provider -> resolveAssetType(provider.orElse(null), resourceType));
    }

    /**
     * 使用已解析的 Provider 获取资源对应的资产类型.
     *
     * @param provider 能力 Provider，可能为空
     * @param resourceType 能力资源类型，可能为空
     * @return 资产类型 ID
     * @see CapabilityProvider#resolveAssetType(String)
     * @since 2.12
     */
    public static Mono<String> resolveAssetType(CapabilityProvider provider, String resourceType) {
        return provider == null
            ? Mono.empty()
            : provider.resolveAssetType(resourceType);
    }

    public static void clear() {
        BY_TYPE_ID.clear();
    }
}
