package org.jetlinks.marketplace.command;

import org.jetlinks.core.command.AbstractCommand;
import org.jetlinks.core.command.GenericInputCommand;
import org.jetlinks.marketplace.CapabilitySearchRequest;
import org.jetlinks.marketplace.CapabilityLatestVersionInfo;
import reactor.core.publisher.Flux;

/**
 * 搜索能力最新版本详情命令.
 *
 * @author zhouhao
 * @since 2.12
 */
public class SearchCapabilityVersionInfoCommand
    extends AbstractCommand<Flux<CapabilityLatestVersionInfo>, SearchCapabilityVersionInfoCommand>
    implements GenericInputCommand<CapabilitySearchRequest> {

    /**
     * 获取能力搜索条件.
     *
     * @return 搜索条件
     */
    public CapabilitySearchRequest asRequest() {
        return as(CapabilitySearchRequest.class);
    }

    /**
     * 设置能力搜索条件.
     *
     * @param request 搜索条件
     * @return 当前命令
     */
    public SearchCapabilityVersionInfoCommand with(CapabilitySearchRequest request) {
        return with((Object) request);
    }
}
