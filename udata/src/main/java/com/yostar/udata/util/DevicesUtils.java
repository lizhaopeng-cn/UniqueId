package com.yostar.udata.util;

import android.os.Build;;

public class DevicesUtils {
    /**
     * 更改列表编号或“M4-rc20”之类的标签。
     */
    public static String getDeviceId() {
        return Build.ID;
    }

    /**
     * 用于向用户显示的构建 ID 字符串
     */
    public static String getDeviceDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 整体产品的名称。
     */
    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    /**
     * 工业设计的名称。
     */
    public static String getDeviceDevice() {
        return Build.DEVICE;
    }

    /**
     * 底层板的名称，如“goldfish”。
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }

    /**
     * 此设备支持的 ABI 的有序列表。
     */
    public static String getDeviceAbis() {
        String[] abis = Build.SUPPORTED_ABIS;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < abis.length; i++) {
            sb.append(" " + abis[i]);
        }
        return sb.toString();
    }

    /**
     * 产品/硬件的制造商。
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 与产品/硬件相关联的消费者可见品牌（如果有）
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 最终产品的最终用户可见名称。
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 设备主要片上系统的制造商。
     */
    public static String getDeviceSocManufacturer() {
//        return Build.SOC_MANUFACTURER;
        return "";
    }

    /**
     * 设备的主要片上系统的型号名称。
     */
    public static String getDeviceSocModel() {
//        return Build.SOC_MODEL;
        return "";
    }

    /**
     * 系统引导加载程序版本号。
     */
    public static String getDeviceBootloader() {
        return Build.BOOTLOADER;
    }

    /**
     * 无线电固件版本号。
     */
    public static String getDeviceRadioVersion() {
        return Build.getRadioVersion();
    }

    /**
     * 硬件的名称（来自内核命令行或 /proc）。
     */
    public static String getDeviceHardware() {
        return Build.HARDWARE;
    }

    /**
     * 硬件的 SKU（来自内核命令行）。
     */
    public static String getDeviceSKU() {
//        return Build.SKU;
        return "";
    }

    /**
     * 原始设计制造商设置的设备 SKU
     */
    public static String getDeviceOdmSKU() {
//        return Build.ODM_SKU;
        return "";
    }

    /**
     * 构建类型，如“user”或“eng”。
     */
    public static String getDeviceType() {
        return Build.TYPE;
    }

    /**
     * 描述构建的逗号分隔标签，如“unsigned,debug”。
     */
    public static String getDeviceTags() {
        return Build.TAGS;
    }

    /**
     * 唯一标识此构建的字符串。 不要尝试解析这个值。
     */
    public static String getDeviceFubgerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 生成构建的时间，以 UNIX 时代以来的毫秒为单位。
     **/
    public static long getDeviceTime() {
        return Build.TIME;
    }

    /**
     * 获取手机用户名
     */
    public static String getDeviceUser() {
        return Build.USER;
    }

    /**
     * 主机
     */
    public static String getDeviceHost() {
        return Build.HOST;
    }

    /**
     * 底层源代码管理使用的内部值代表这个构建。
     *
     * 例如，perforce 更改列表编号或 git 哈希。
     */
    public static String getDeviceIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * 用户可见的版本字符串。
     *
     * 例如，“1.0”或“3.4b5”或“香蕉”。
     * 该字段是一个不透明的字符串。 不要假设它的价值
     */
    public static String getDeviceRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 我们向用户显示的版本字符串；
     *
     * 如果不是最终发布版本，可能是 {RELEASE} 或 {CODENAME}。
     */
    public static String getDeviceReleaseOrCodename() {
//        return Build.VERSION.RELEASE_OR_CODENAME;
        return "";
    }

    /**
     * 产品所基于的基本操作系统构建。
     */
    public static String getDeviceBaseOS() {
        return Build.VERSION.BASE_OS;
    }

    /**
     * 用户可见的安全补丁级别。
     *
     * 该值表示设备的日期最近应用了一个安全补丁。
     */
    public static String getDeviceSecurityPatch() {
        return Build.VERSION.SECURITY_PATCH;
    }

    /**
     * 设备的媒体性能等级，如果没有，则为 0。
     *
     * <p>
     * 如果该值不是<code>0</code>，则设备符合媒体性能等级
     * 此值的 SDK 版本的定义。 当设备处于运行状态时，此值永远不会改变
     * 已启动，但当硬件制造商提供 OTA 更新时，它可能会增加。
     * <p>
     * 可能的非零值在 {@link Build.VERSION_CODES} 中定义，以
     * {@link Build.VERSION_CODES#R}。
     */
    public static int getDeviceMediaPerformanceClass() {
//        return Build.VERSION.MEDIA_PERFORMANCE_CLASS;
        return 0;
    }

    /**
     * 当前在此硬件上运行的软件的 SDK 版本设备。
     *
     * 该值在设备启动时永远不会改变，但它可能
     * 当硬件制造商提供 OTA 更新时增加。
     * <p>
     * {@link Build.VERSION_CODES} 中定义了可能的值。
     */
    public static int getDeviceSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 预发布 SDK 的开发者预览版。
     *
     * 该值将始终在生产平台构建/设备上为 <code>0</code>。
     * <p>当该值不为零时，自上次以来添加的任何新 API
     * 官方发布的 {link SDK_INT API level} 只保证存在
     * 在那个特定的预览版本上。 例如，一个 API <code>Activity.fooBar()</code>
     * 可能存在于预览修订版 1 中，但在
     * 预览修订版 2，这可能会导致尝试调用它的应用程序崩溃
     * 在运行时。</p>
     * <p>面向预览 API 的实验性应用应检查此值
     * 等价 (<code>==</code>) 与他们为之构建的预览 SDK 修订版
     * 在使用任何预发布平台 API 之前。 检测预览版 SDK 修订版的应用
     * 除了他们期望的特定 API 之外，他们应该回退到使用来自
     * 之前发布的 API 级别只是为了避免不需要的运行时异常。
     * </p>
     */
    public static int getDevicePreviewSdkInt() {
        return Build.VERSION.PREVIEW_SDK_INT;
    }

}
