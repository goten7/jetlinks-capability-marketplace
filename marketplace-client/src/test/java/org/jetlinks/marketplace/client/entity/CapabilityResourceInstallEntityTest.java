package org.jetlinks.marketplace.client.entity;

import org.jetlinks.marketplace.CapabilityInfo;
import org.jetlinks.marketplace.CapabilityPackage;
import org.jetlinks.marketplace.InstalledResource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CapabilityResourceInstallEntityTest {

    @Test
    void shouldGenerateStableIdForSameLogicalBinding() {
        CapabilityPackage pkg = capabilityPackage("cap-1", "1.0.0");
        InstalledResource resource = installedResource("tool", "resource-1", "data-1");

        CapabilityResourceInstallEntity first = CapabilityResourceInstallEntity.from(resource, pkg);
        CapabilityResourceInstallEntity second = CapabilityResourceInstallEntity.from(resource, pkg);

        assertThat(first.getId()).isEqualTo(second.getId());
    }

    @Test
    void shouldIgnoreVersionWhenGeneratingBindingId() {
        InstalledResource resource = installedResource("tool", "resource-1", "data-1");

        CapabilityResourceInstallEntity first = CapabilityResourceInstallEntity.from(resource, capabilityPackage("cap-1", "1.0.0"));
        CapabilityResourceInstallEntity second = CapabilityResourceInstallEntity.from(resource, capabilityPackage("cap-1", "2.0.0"));

        assertThat(first.getId()).isEqualTo(second.getId());
        assertThat(first.getVersion()).isEqualTo("1.0.0");
        assertThat(second.getVersion()).isEqualTo("2.0.0");
    }

    @Test
    void shouldChangeIdWhenLogicalBindingChanges() {
        CapabilityPackage pkg = capabilityPackage("cap-1", "1.0.0");

        CapabilityResourceInstallEntity first = CapabilityResourceInstallEntity.from(
            installedResource("tool", "resource-1", "data-1"),
            pkg
        );
        CapabilityResourceInstallEntity second = CapabilityResourceInstallEntity.from(
            installedResource("tool", "resource-2", "data-1"),
            pkg
        );

        assertThat(first.getId()).isNotEqualTo(second.getId());
    }

    private static CapabilityPackage capabilityPackage(String capabilityId, String version) {
        CapabilityInfo info = new CapabilityInfo();
        info.setId(capabilityId);

        CapabilityPackage pkg = new CapabilityPackage();
        pkg.setInfo(info);
        pkg.setVersion(version);
        return pkg;
    }

    private static InstalledResource installedResource(String type, String resourceId, String dataId) {
        InstalledResource resource = new InstalledResource();
        resource.setType(type);
        resource.setResourceId(resourceId);
        resource.setDataId(dataId);
        return resource;
    }
}
