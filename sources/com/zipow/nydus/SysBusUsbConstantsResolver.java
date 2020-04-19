package com.zipow.nydus;

class SysBusUsbConstantsResolver {
    private static final int USB_CLASS_APP_SPEC = 254;
    private static final int USB_CLASS_AUDIO = 1;
    private static final int USB_CLASS_CDC_DATA = 10;
    private static final int USB_CLASS_COMM = 2;
    private static final int USB_CLASS_CONTENT_SEC = 13;
    private static final int USB_CLASS_CSCID = 11;
    private static final int USB_CLASS_DIAGNOSTICS = 220;
    private static final int USB_CLASS_HID = 3;
    private static final int USB_CLASS_HUB = 9;
    private static final int USB_CLASS_MASS_STORAGE = 8;
    private static final int USB_CLASS_MISC = 239;
    private static final int USB_CLASS_PERSONAL_HEALTH = 15;
    private static final int USB_CLASS_PER_INTERFACE = 0;
    private static final int USB_CLASS_PHYSICAL = 5;
    private static final int USB_CLASS_PRINTER = 7;
    private static final int USB_CLASS_STILL_IMAGE = 6;
    private static final int USB_CLASS_VENDOR_SPEC = 255;
    private static final int USB_CLASS_VIDEO = 14;
    private static final int USB_CLASS_WIRELESS_CONTROLLER = 224;
    private static final int USB_DIR_IN = 128;
    private static final int USB_DIR_OUT = 0;
    private static final int USB_ENDPOINT_XFER_BULK = 2;
    private static final int USB_ENDPOINT_XFER_CONTROL = 0;
    private static final int USB_ENDPOINT_XFER_INT = 3;
    private static final int USB_ENDPOINT_XFER_ISOC = 1;

    SysBusUsbConstantsResolver() {
    }

    protected static String resolveUsbClass(int i) {
        if (i == 220) {
            StringBuilder sb = new StringBuilder();
            sb.append("Diagnostics Device (0x");
            sb.append(Integer.toHexString(i));
            sb.append(")");
            return sb.toString();
        } else if (i == 224) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Wireless Controller (0x");
            sb2.append(Integer.toHexString(i));
            sb2.append(")");
            return sb2.toString();
        } else if (i != 239) {
            switch (i) {
                case 0:
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Use class information in the Interface Descriptors (0x");
                    sb3.append(Integer.toHexString(i));
                    sb3.append(")");
                    return sb3.toString();
                case 1:
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Audio Device (0x");
                    sb4.append(Integer.toHexString(i));
                    sb4.append(")");
                    return sb4.toString();
                case 2:
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Communication Device (0x");
                    sb5.append(Integer.toHexString(i));
                    sb5.append(")");
                    return sb5.toString();
                case 3:
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Human Interaction Device (0x");
                    sb6.append(Integer.toHexString(i));
                    sb6.append(")");
                    return sb6.toString();
                default:
                    switch (i) {
                        case 5:
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("Physical Device (0x");
                            sb7.append(Integer.toHexString(i));
                            sb7.append(")");
                            return sb7.toString();
                        case 6:
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("Still Image Device (0x");
                            sb8.append(Integer.toHexString(i));
                            sb8.append(")");
                            return sb8.toString();
                        case 7:
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append("Printer (0x");
                            sb9.append(Integer.toHexString(i));
                            sb9.append(")");
                            return sb9.toString();
                        case 8:
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("Mass Storage Device (0x");
                            sb10.append(Integer.toHexString(i));
                            sb10.append(")");
                            return sb10.toString();
                        case 9:
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("USB Hub (0x");
                            sb11.append(Integer.toHexString(i));
                            sb11.append(")");
                            return sb11.toString();
                        case 10:
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append("Communication Device Class (CDC) (0x");
                            sb12.append(Integer.toHexString(i));
                            sb12.append(")");
                            return sb12.toString();
                        case 11:
                            StringBuilder sb13 = new StringBuilder();
                            sb13.append("Content SmartCard Device (0x");
                            sb13.append(Integer.toHexString(i));
                            sb13.append(")");
                            return sb13.toString();
                        default:
                            switch (i) {
                                case 13:
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("Content Security Device (0x");
                                    sb14.append(Integer.toHexString(i));
                                    sb14.append(")");
                                    return sb14.toString();
                                case 14:
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("Video Device (0x");
                                    sb15.append(Integer.toHexString(i));
                                    sb15.append(")");
                                    return sb15.toString();
                                case 15:
                                    StringBuilder sb16 = new StringBuilder();
                                    sb16.append("Personal Healthcare Device (0x");
                                    sb16.append(Integer.toHexString(i));
                                    sb16.append(")");
                                    return sb16.toString();
                                default:
                                    switch (i) {
                                        case USB_CLASS_APP_SPEC /*254*/:
                                            StringBuilder sb17 = new StringBuilder();
                                            sb17.append("Application Specific (0x");
                                            sb17.append(Integer.toHexString(i));
                                            sb17.append(")");
                                            return sb17.toString();
                                        case 255:
                                            StringBuilder sb18 = new StringBuilder();
                                            sb18.append("Vendor Specific (0x");
                                            sb18.append(Integer.toHexString(i));
                                            sb18.append(")");
                                            return sb18.toString();
                                        default:
                                            StringBuilder sb19 = new StringBuilder();
                                            sb19.append("Unknown (0x");
                                            sb19.append(Integer.toHexString(i));
                                            sb19.append(")");
                                            return sb19.toString();
                                    }
                            }
                    }
            }
        } else {
            StringBuilder sb20 = new StringBuilder();
            sb20.append("Miscellaneous (0x");
            sb20.append(Integer.toHexString(i));
            sb20.append(")");
            return sb20.toString();
        }
    }

    protected static String resolveUsbEndpointDirection(int i) {
        if (i == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Outbound (0x");
            sb.append(Integer.toHexString(i));
            sb.append(")");
            return sb.toString();
        } else if (i != 128) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unknown (0x");
            sb2.append(Integer.toHexString(i));
            sb2.append(")");
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Inbound (0x");
            sb3.append(Integer.toHexString(i));
            sb3.append(")");
            return sb3.toString();
        }
    }

    protected static String resolveUsbEndpointType(int i) {
        switch (i) {
            case 0:
                StringBuilder sb = new StringBuilder();
                sb.append("Control (0x");
                sb.append(Integer.toHexString(i));
                sb.append(")");
                return sb.toString();
            case 1:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Isochronous (0x");
                sb2.append(Integer.toHexString(i));
                sb2.append(")");
                return sb2.toString();
            case 2:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Bulk (0x");
                sb3.append(Integer.toHexString(i));
                sb3.append(")");
                return sb3.toString();
            case 3:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Intrrupt (0x");
                sb4.append(Integer.toHexString(i));
                sb4.append(")");
                return sb4.toString();
            default:
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Unknown (0x");
                sb5.append(Integer.toHexString(i));
                sb5.append(")");
                return sb5.toString();
        }
    }
}
