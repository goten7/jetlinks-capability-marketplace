package org.jetlinks.marketplace.spi;

import org.jetlinks.core.monitor.Monitor;
import org.jetlinks.marketplace.CapabilityInstallRequest;
import org.jetlinks.marketplace.CapabilityPackage;
import org.jetlinks.marketplace.InstalledCapability;
import org.jetlinks.marketplace.InstalledResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 能力提供者 SPI
 *
 * @author zhouhao
 * @see CapabilityProviders
 * @since 2.12
 */
public interface CapabilityProvider {

    /**
     * ID
     */
    String getId();

    /**
     * 名称
     */
    String getName();

    /**
     * 安装能力.
     *
     * <p>返回结果表示本次安装目标范围内最终应保留的完整资源集合，而不是仅包含实际发生变更的增量集合。
     * 安装编排会删除{@link CapabilityContext#loadInstallResources()}返回的旧资源绑定，再使用本方法返回结果重建绑定。
     * 因资产权限、配置或业务判断未执行更新的旧资源，也必须原样包含在返回结果中；漏返将被视为移除对应安装绑定。</p>
     *
     * @param context 安装上下文
     * @return 本次目标范围内最终保留的完整安装资源集合
     */
    Flux<InstalledResource> install(CapabilityContext context);


    interface CapabilityContext {

        /**
         * 加载本次安装或升级目标范围内的旧资源.
         *
         * <p>{@link CapabilityProvider#install(CapabilityContext)}返回结果将完整替换这些旧资源绑定。
         * Provider 只更新其中部分资源时，必须将未更新资源原样合并到安装结果中返回。</p>
         *
         * @return 本次目标范围内的旧资源
         */
        Flux<InstalledResource> loadInstallResources();

        /**
         * 加载当前安装包依赖能力的已安装资源.
         *
         * @return InstalledResource
         */
        default Flux<InstalledResource> loadDependencyResources() {
            return Flux.empty();
        }

        /**
         * 按资源类型加载当前安装包依赖能力的已安装资源.
         *
         * @param type 资源类型
         * @return InstalledResource
         */
        default Flux<InstalledResource> loadDependencyResources(String type) {
            return loadDependencyResources()
                .filter(resource -> type.equals(resource.getType()));
        }

        /**
         * 获取安装包
         *
         * @return 安装包
         */
        CapabilityPackage pkg();

        /**
         * 监控对象,用于打印日志等操作
         */
        Monitor monitor();

        Map<String,Object> configuration();

        default CapabilityInstallRequest request() {
            return CapabilityInstallRequest.ofConfiguration(configuration());
        }
    }
}
