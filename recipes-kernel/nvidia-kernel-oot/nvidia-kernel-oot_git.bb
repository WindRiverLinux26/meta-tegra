SRC_REPO_NV_OOT = "gitlab.com/nvidia/nv-tegra/linux-nv-oot.git;protocol=https"
SRC_REPO_NV_ETHERNETRM = "gitlab.com/nvidia/nv-tegra/kernel/nvethernetrm.git;protocol=https"
SRC_REPO_NV_KERNEL_DISPLAY = "gitlab.com/nvidia/nv-tegra/tegra/kernel-src/nv-kernel-display-driver.git;protocol=https"
SRC_REPO_HWPM = "gitlab.com/nvidia/nv-tegra/linux-hwpm.git;protocol=https"
SRC_REPO_NVGPU = "gitlab.com/nvidia/nv-tegra/linux-nvgpu.git;protocol=https"
SRC_REPO_T264_DTS = "gitlab.com/nvidia/nv-tegra/device/hardware/nvidia/t264-public-dts.git;protocol=https"
SRC_REPO_T23X_DTS = "gitlab.com/nvidia/nv-tegra/device/hardware/nvidia/t23x-public-dts.git;protocol=https"
SRC_REPO_TEGRA_DTS = "gitlab.com/nvidia/nv-tegra/device/hardware/nvidia/tegra-public-dts.git;protocol=https"
SRC_REPO_KERNEL_DTS = "gitlab.com/nvidia/nv-tegra/linux/kernel-devicetree.git;protocol=https"

SRC_URI = " \
    git://${SRC_REPO_NV_OOT};branch=${SRCBRANCH};name=nvidia-oot;destsuffix=${BPN}-${PV}/nvidia-oot \
    git://${SRC_REPO_NV_ETHERNETRM};branch=${SRCBRANCH};name=nvethernetrm;destsuffix=${BPN}-${PV}/nvethernetrm \
    git://${SRC_REPO_NV_KERNEL_DISPLAY};branch=${SRCBRANCH};name=nvdisplay;destsuffix=${BPN}-${PV}/nvdisplay \
    git://${SRC_REPO_HWPM};branch=${SRCBRANCH};name=hwpm;destsuffix=${BPN}-${PV}/hwpm \
    git://${SRC_REPO_NVGPU};branch=${SRCBRANCH};name=nvgpu;destsuffix=${BPN}-${PV}/nvgpu \
    git://${SRC_REPO_T23X_DTS};branch=${SRCBRANCH};name=t23x-dts;destsuffix=${BPN}-${PV}/hardware/nvidia/t23x/nv-public \
    git://${SRC_REPO_TEGRA_DTS};branch=${SRCBRANCH};name=tegra-dts;destsuffix=${BPN}-${PV}/hardware/nvidia/tegra/nv-public \
    git://${SRC_REPO_KERNEL_DTS};branch=${SRCBRANCH};name=kernel-devicetree;destsuffix=${BPN}-${PV}/kernel-devicetree \
    file://Makefile \
"

SRCBRANCH = "l4t/l4t-r36.5"
# tag: jetson_36.5
SRCREV_nvidia-oot = "b05da1f94ae5a2dbfe928ef7ab387035a2b77a20"
SRCREV_nvethernetrm = "22e582e01d1c9c258ac56873f1aa0822acb695f3"
SRCREV_nvdisplay = "a3da679668071cff718a33922b6d302bd18ebaa1"
SRCREV_hwpm = "ebfe0e9c4b4d96e07232ceb37f67168a1234ffeb"
SRCREV_nvgpu = "a4145c7b6e05af9b5ee4b8da246e3b3820023dca"
SRCREV_t23x-dts = "ed1b0f6b113bb050c8cda1ccb411a163f8e2799f"
SRCREV_tegra-dts = "8ba5d53ef1e1753f9f2a5b1f7b7b5fc5039de68e"
SRCREV_kernel-devicetree = "19952c8e25702e9de23500c3b1fb351bf4380446"

SRCREV_FORMAT = "nvidia-oot_nvethernetrm_nvdisplay_hwpm_nvgpu_t23x-dts_tegra-dts_kernel-devicetree"

inherit l4t_bsp

PV = "${L4T_VERSION}+git"

install_makefile() {
    cp ${UNPACKDIR}/Makefile ${S}/
}
do_unpack[postfuncs] += "install_makefile"

add_nvethernetrm_symlink() {
    ln -snf ../../../../../../nvethernetrm ${S}/nvidia-oot/drivers/net/ethernet/nvidia/nvethernet/nvethernetrm
}
do_unpack[postfuncs] += "add_nvethernetrm_symlink"

require nvidia-kernel-oot.inc

EXTRA_PATCHES = "\
    file://0001-t23x-overlay-add-dts-to-support-Arducam-6-channels-o.patch;patchdir=hardware/nvidia/t23x/nv-public \
    file://0001-drivers-video-nvcsi-t194-fix-the-failure-of-creating.patch;patchdir=nvidia-oot \
    file://0001-t23x-overlay-create-a-overlay-dts-to-support-for-boo.patch;patchdir=hardware/nvidia/t23x/nv-public \
    file://0001-net-nvethernet-generate-a-random-mac-address.patch;patchdir=nvidia-oot \
    file://0001-Fix-rcu-stall-when-CONFIG_SHADOW_CALL_STACK-enabled.patch;patchdir=nvdisplay \
    file://0001-drm-tegra-release-the-count-of-pid-to-avoid-memory-l.patch;patchdir=nvidia-oot \
    file://0001-nvdisplay-improve-coed-to-allow-nested-lock.patch;patchdir=nvdisplay \
    file://0001-t23x-overlay-create-dts-for-booting-NVIDIA-Orin-Nano.patch;patchdir=hardware/nvidia/t23x/nv-public \
    file://0001-usb-typec-remove-IRQF_ONESHOT-flag.patch;patchdir=nvidia-oot \
    file://0001-host1x-fence-call-fput-to-avoid-memory-leak.patch;patchdir=nvidia-oot \
    file://0001-drivers-capture-common-only-create-memory-cache-at-t.patch;patchdir=nvidia-oot \
    file://0001-usb-typec-release-resource-when-removing-usb-driver-.patch;patchdir=nvidia-oot \
    file://0001-nvdisplay-nv-platform-call-platform_device_put-to-de.patch;patchdir=nvdisplay \
    file://0001-nvidia-modeset-dp-decrease-reference-if-destroy-func.patch;patchdir=nvdisplay \
    file://0001-nvdisplay-nv-platform-set-the-dma-segment-size-for-n.patch;patchdir=nvdisplay \
    file://0001-drivers-devfreq-call-devfreq_get_freq_range-in-locke.patch;patchdir=nvidia-oot \
    file://0001-drivers-devfreq-avoid-deadlock-issue-when-calling-de.patch;patchdir=nvidia-oot \
    file://0001-drivers-nvethernet-speed-10-is-not-supported-while-a.patch;patchdir=nvidia-oot \
    file://0001-nvdisplay-replace-lock-sync-with-wait-event-mechanis.patch;patchdir=nvdisplay \
    file://0001-nvgpu-dma-use-unlocked-variant-function-to-process-d.patch;patchdir=nvgpu \
    file://0001-nvdisplay-dma-use-unlocked-variant-function-when-map.patch;patchdir=nvdisplay \
    file://0001-gpu-nvgpu-probe-clock-availability-during-acquisitio.patch;patchdir=nvgpu \
    file://0001-gpu-host1x-Do-not-setup-DMA-for-virtual-devices.patch;patchdir=nvidia-oot \
    file://0002-video-tegra-pva-use-dma_map_phys-for-RAM-backed-carv.patch;patchdir=nvidia-oot \
    file://0003-video-tegra-pva-use-DMA-IOVA-API-for-contiguous-sync.patch;patchdir=nvidia-oot \
    file://0004-video-tegra-pva-add-missing-nvhost-syncpt-interface-.patch;patchdir=nvidia-oot \
    file://0001-video-tegra-nvcsi-fix-resource-leak-on-module-unload.patch;patchdir=nvidia-oot \
    file://0001-video-tegra-isp5-fix-use-after-free-on-module-unload.patch;patchdir=nvidia-oot \
    file://0002-video-tegra-isp5-fix-resource-leak-on-module-unload.patch;patchdir=nvidia-oot \
    file://0001-video-tegra-vi5-fix-resource-leak-on-module-unload.patch;patchdir=nvidia-oot \
    file://0001-media-tegra-capture-vi-fix-use-after-free-on-module-.patch;patchdir=nvidia-oot \
    file://0002-media-tegra-vi-fix-NULL-pointer-dereference-in-chann.patch;patchdir=nvidia-oot \
    file://0003-video-tegra-camera_platform-fix-debugfs-leak-on-modu.patch;patchdir=nvidia-oot \
    file://0001-media-tegra-vi-fix-memory-leak-in-media-v4l2-cleanup.patch;patchdir=nvidia-oot \
    file://0001-video-tegra-capture-support-fix-resource-leak-on-mod.patch;patchdir=nvidia-oot \
    file://0001-net-can-mttcan-fix-use-after-free-issue-of-skb.patch;patchdir=nvidia-oot \
    file://0001-conftest-fix-__assign_str_has_no_src_arg-test-failin.patch;patchdir=nvidia-oot \
"
