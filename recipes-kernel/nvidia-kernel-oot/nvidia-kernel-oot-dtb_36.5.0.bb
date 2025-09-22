DESCRIPTION = "Virtual/dtb provider for Jetson Linux device trees"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "nvidia-kernel-oot dtc-native"

inherit deploy kernel-arch

PROVIDES = "virtual/dtb"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOTABLE_OVERLAY ??= ""

do_deploy() {
    for dtb in ${KERNEL_DEVICETREE}; do
        dtbf="${STAGING_DIR_HOST}/boot/devicetree/$dtb"
        if [ ! -f "$dtbf" ]; then
            bbfatal "Not found: $dtbf"
        fi
    done
    install -d ${DEPLOYDIR}/devicetree
    install -m 0644 ${STAGING_DIR_HOST}/boot/devicetree/* ${DEPLOYDIR}/devicetree/

    # Create a full feature dtb used to boot kernel directly
    if [ -n "${BOOTABLE_OVERLAY}" ]; then
        basic_dtb=$(echo "${KERNEL_DEVICETREE}" | cut -d' ' -f1)
        basic_dtb_basename="${basic_dtb%.*}"
        cd ${DEPLOYDIR}/devicetree/
        if [ "${MACHINE}" = "nvidia-orin-agx" ]; then
            fdtoverlay -i ${basic_dtb} -o ${basic_dtb_basename}-bootable.dtb ${TEGRA_PLUGIN_MANAGER_OVERLAYS} ${BOOTABLE_OVERLAY}
        elif [ "${MACHINE}" = "nvidia-orin-nano" ]; then
            fdtoverlay -i ${basic_dtb_basename}-boot.dtb -o ${basic_dtb_basename}-bootable.dtb ${TEGRA_PLUGIN_MANAGER_OVERLAYS} ${BOOTABLE_OVERLAY}
        fi
        cd -
    fi
}

addtask deploy before do_build after do_install

ALLOW_EMPTY:${PN} = "1"
