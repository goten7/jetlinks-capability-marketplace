package org.jetlinks.marketplace.client.spi;

import org.jetlinks.marketplace.client.entity.CapabilityResourceInstallEntity;
import org.jetlinks.marketplace.spi.CapabilityProvider;
import org.jetlinks.marketplace.spi.CapabilityProviders;
import reactor.core.publisher.Flux;

/**
 * 能力 Provider 工具类.
 *
 * <p>该类处理 client 侧安装资源实体，因此放在 marketplace-client 中，避免 marketplace-core 反向依赖
 * marketplace-client。</p>
 *
 * @author zhouhao
 * @since 2.12
 */
public final class CapabilityProviderUtils {

    private CapabilityProviderUtils() {
    }

    /**
     * 根据安装资源自身记录的 Provider ID 和资源类型解析资产类型.
     *
     * @param resources 安装资源
     * @return 带资产类型解析结果的安装资源
     */
    public static Flux<ResolvedAssetTypeInstalledResource> resolveAssetTypes(Flux<CapabilityResourceInstallEntity> resources) {
        return resources
            .groupBy(resource -> new ResourceTypeKey(resource.getProviderId(), resource.getType()))
            .flatMap(group -> CapabilityProviders
                .resolveAssetType(group.key().providerId(), group.key().resourceType())
                .defaultIfEmpty(group.key().resourceType())
                .flatMapMany(assetType -> group
                    .map(resource -> new ResolvedAssetTypeInstalledResource(assetType, resource))));
    }

    /**
     * 使用指定 Provider 解析安装资源的资产类型.
     *
     * @param provider  指定 Provider，可能为空
     * @param resources 安装资源
     * @return 带资产类型解析结果的安装资源
     */
    public static Flux<ResolvedAssetTypeInstalledResource> resolveAssetTypes(CapabilityProvider provider,
                                                                             Flux<CapabilityResourceInstallEntity> resources) {
        return resources
            .groupBy(CapabilityResourceInstallEntity::getType)
            .flatMap(resource -> CapabilityProviders
                .resolveAssetType(provider, resource.key())
                .defaultIfEmpty(resource.key())
                .flatMapMany(assetType -> resource
                    .map(r -> new ResolvedAssetTypeInstalledResource(assetType, r))));
    }

    /**
     * 已解析资产类型的安装资源.
     *
     * @param assetType 资产类型；未解析到 Provider 资产类型时使用安装资源原始类型
     * @param resource  安装资源
     */
    public record ResolvedAssetTypeInstalledResource(String assetType,
                                                     CapabilityResourceInstallEntity resource) {
    }

    private record ResourceTypeKey(String providerId,
                                   String resourceType) {
    }
}
