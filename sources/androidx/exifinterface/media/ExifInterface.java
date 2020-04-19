package androidx.exifinterface.media;

import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.view.InputDeviceCompat;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.common.base.Ascii;
import com.zipow.videobox.ptapp.XmppError;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.TokenParser;
import p021us.zoom.androidlib.widget.recyclerview.ZMBaseRecyclerViewAdapter;

public class ExifInterface {
    public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
    public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
    static final Charset ASCII = Charset.forName("US-ASCII");
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = {4};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = {8};
    public static final int[] BITS_PER_SAMPLE_RGB = {8, 8, 8};
    static final short BYTE_ALIGN_II = 18761;
    static final short BYTE_ALIGN_MM = 19789;
    public static final int COLOR_SPACE_S_RGB = 1;
    public static final int COLOR_SPACE_UNCALIBRATED = 65535;
    public static final short CONTRAST_HARD = 2;
    public static final short CONTRAST_NORMAL = 0;
    public static final short CONTRAST_SOFT = 1;
    public static final int DATA_DEFLATE_ZIP = 8;
    public static final int DATA_HUFFMAN_COMPRESSED = 2;
    public static final int DATA_JPEG = 6;
    public static final int DATA_JPEG_COMPRESSED = 7;
    public static final int DATA_LOSSY_JPEG = 34892;
    public static final int DATA_PACK_BITS_COMPRESSED = 32773;
    public static final int DATA_UNCOMPRESSED = 1;
    private static final boolean DEBUG = false;
    static final byte[] EXIF_ASCII_PREFIX = {65, 83, 67, 73, 73, 0, 0, 0};
    private static final ExifTag[] EXIF_POINTER_TAGS = {new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 1), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 1)};
    static final ExifTag[][] EXIF_TAGS;
    public static final short EXPOSURE_MODE_AUTO = 0;
    public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
    public static final short EXPOSURE_MODE_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_ACTION = 6;
    public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
    public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
    public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
    public static final short EXPOSURE_PROGRAM_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_NORMAL = 2;
    public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
    public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
    public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
    public static final short FILE_SOURCE_DSC = 3;
    public static final short FILE_SOURCE_OTHER = 0;
    public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
    public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
    public static final short FLAG_FLASH_FIRED = 1;
    public static final short FLAG_FLASH_MODE_AUTO = 24;
    public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
    public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
    public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
    public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
    public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
    public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
    private static final List<Integer> FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(7), Integer.valueOf(4), Integer.valueOf(5)});
    public static final short FORMAT_CHUNKY = 1;
    public static final short FORMAT_PLANAR = 2;
    public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
    public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
    public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
    public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
    public static final short GAIN_CONTROL_NONE = 0;
    public static final String GPS_DIRECTION_MAGNETIC = "M";
    public static final String GPS_DIRECTION_TRUE = "T";
    public static final String GPS_DISTANCE_KILOMETERS = "K";
    public static final String GPS_DISTANCE_MILES = "M";
    public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
    public static final String GPS_MEASUREMENT_2D = "2";
    public static final String GPS_MEASUREMENT_3D = "3";
    public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
    public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
    public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
    public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
    public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
    public static final String GPS_SPEED_KNOTS = "N";
    public static final String GPS_SPEED_MILES_PER_HOUR = "M";
    static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final ExifTag[] IFD_EXIF_TAGS = {new ExifTag(TAG_EXPOSURE_TIME, 33434, 5), new ExifTag(TAG_F_NUMBER, 33437, 5), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3), new ExifTag(TAG_SPECTRAL_SENSITIVITY, 34852, 2), new ExifTag(TAG_PHOTOGRAPHIC_SENSITIVITY, 34855, 3), new ExifTag(TAG_OECF, 34856, 7), new ExifTag(TAG_EXIF_VERSION, 36864, 2), new ExifTag(TAG_DATETIME_ORIGINAL, 36867, 2), new ExifTag(TAG_DATETIME_DIGITIZED, 36868, 2), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, 37122, 5), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10), new ExifTag(TAG_APERTURE_VALUE, 37378, 5), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5), new ExifTag(TAG_METERING_MODE, 37383, 3), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3), new ExifTag(TAG_FLASH, 37385, 3), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5), new ExifTag(TAG_SUBJECT_AREA, 37396, 3), new ExifTag(TAG_MAKER_NOTE, 37500, 7), new ExifTag(TAG_USER_COMMENT, 37510, 7), new ExifTag(TAG_SUBSEC_TIME, 37520, 2), new ExifTag(TAG_SUBSEC_TIME_ORIGINAL, 37521, 2), new ExifTag(TAG_SUBSEC_TIME_DIGITIZED, 37522, 2), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7), new ExifTag(TAG_COLOR_SPACE, 40961, 3), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_FLASH_ENERGY, 41483, 5), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5), new ExifTag(TAG_SENSING_METHOD, 41495, 3), new ExifTag(TAG_FILE_SOURCE, 41728, 7), new ExifTag(TAG_SCENE_TYPE, 41729, 7), new ExifTag(TAG_CFA_PATTERN, 41730, 7), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3), new ExifTag(TAG_WHITE_BALANCE, 41987, 3), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3), new ExifTag(TAG_GAIN_CONTROL, 41991, 3), new ExifTag(TAG_CONTRAST, 41992, 3), new ExifTag(TAG_SATURATION, 41993, 3), new ExifTag(TAG_SHARPNESS, 41994, 3), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
    private static final int IFD_FORMAT_BYTE = 1;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    static final String[] IFD_FORMAT_NAMES = {"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS = {new ExifTag(TAG_GPS_VERSION_ID, 0, 1), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2), new ExifTag(TAG_GPS_LATITUDE, 2, 5), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2), new ExifTag(TAG_GPS_LONGITUDE, 4, 5), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1), new ExifTag(TAG_GPS_ALTITUDE, 6, 5), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5), new ExifTag(TAG_GPS_SATELLITES, 8, 2), new ExifTag(TAG_GPS_STATUS, 9, 2), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2), new ExifTag(TAG_GPS_DOP, 11, 5), new ExifTag(TAG_GPS_SPEED_REF, 12, 2), new ExifTag(TAG_GPS_SPEED, 13, 5), new ExifTag(TAG_GPS_TRACK_REF, 14, 2), new ExifTag(TAG_GPS_TRACK, 15, 5), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7), new ExifTag(TAG_GPS_DATESTAMP, 29, 2), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3)};
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = {new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2)};
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = {new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, InputDeviceCompat.SOURCE_KEYBOARD, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, SubsamplingScaleImageView.ORIENTATION_270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, ZMBaseRecyclerViewAdapter.HEADER_VIEW, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, XmppError.XmppError_JidMalformed, 2), new ExifTag(TAG_ARTIST, XmppError.XmppError_UndefinedCondition, 2), new ExifTag(TAG_WHITE_POINT, XmppError.XmppError_Undefined, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
    private static final ExifTag[] IFD_TIFF_TAGS = {new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_IMAGE_LENGTH, InputDeviceCompat.SOURCE_KEYBOARD, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, SubsamplingScaleImageView.ORIENTATION_270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, ZMBaseRecyclerViewAdapter.HEADER_VIEW, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, XmppError.XmppError_JidMalformed, 2), new ExifTag(TAG_ARTIST, XmppError.XmppError_UndefinedCondition, 2), new ExifTag(TAG_WHITE_POINT, XmppError.XmppError_Undefined, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_RW2_SENSOR_TOP_BORDER, 4, 4), new ExifTag(TAG_RW2_SENSOR_LEFT_BORDER, 5, 4), new ExifTag(TAG_RW2_SENSOR_BOTTOM_BORDER, 6, 4), new ExifTag(TAG_RW2_SENSOR_RIGHT_BORDER, 7, 4), new ExifTag(TAG_RW2_ISO, 23, 3), new ExifTag(TAG_RW2_JPG_FROM_RAW, 46, 7)};
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    static final int IFD_TYPE_PREVIEW = 5;
    static final int IFD_TYPE_PRIMARY = 0;
    static final int IFD_TYPE_THUMBNAIL = 4;
    private static final int IMAGE_TYPE_ARW = 1;
    private static final int IMAGE_TYPE_CR2 = 2;
    private static final int IMAGE_TYPE_DNG = 3;
    private static final int IMAGE_TYPE_JPEG = 4;
    private static final int IMAGE_TYPE_NEF = 5;
    private static final int IMAGE_TYPE_NRW = 6;
    private static final int IMAGE_TYPE_ORF = 7;
    private static final int IMAGE_TYPE_PEF = 8;
    private static final int IMAGE_TYPE_RAF = 9;
    private static final int IMAGE_TYPE_RW2 = 10;
    private static final int IMAGE_TYPE_SRW = 11;
    private static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4);
    static final byte[] JPEG_SIGNATURE = {-1, MARKER_SOI, -1};
    public static final String LATITUDE_NORTH = "N";
    public static final String LATITUDE_SOUTH = "S";
    public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
    public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
    public static final short LIGHT_SOURCE_D50 = 23;
    public static final short LIGHT_SOURCE_D55 = 20;
    public static final short LIGHT_SOURCE_D65 = 21;
    public static final short LIGHT_SOURCE_D75 = 22;
    public static final short LIGHT_SOURCE_DAYLIGHT = 1;
    public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
    public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
    public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
    public static final short LIGHT_SOURCE_FLASH = 4;
    public static final short LIGHT_SOURCE_FLUORESCENT = 2;
    public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
    public static final short LIGHT_SOURCE_OTHER = 255;
    public static final short LIGHT_SOURCE_SHADE = 11;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
    public static final short LIGHT_SOURCE_TUNGSTEN = 3;
    public static final short LIGHT_SOURCE_UNKNOWN = 0;
    public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
    public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
    public static final String LONGITUDE_EAST = "E";
    public static final String LONGITUDE_WEST = "W";
    static final byte MARKER = -1;
    static final byte MARKER_APP1 = -31;
    private static final byte MARKER_COM = -2;
    static final byte MARKER_EOI = -39;
    private static final byte MARKER_SOF0 = -64;
    private static final byte MARKER_SOF1 = -63;
    private static final byte MARKER_SOF10 = -54;
    private static final byte MARKER_SOF11 = -53;
    private static final byte MARKER_SOF13 = -51;
    private static final byte MARKER_SOF14 = -50;
    private static final byte MARKER_SOF15 = -49;
    private static final byte MARKER_SOF2 = -62;
    private static final byte MARKER_SOF3 = -61;
    private static final byte MARKER_SOF5 = -59;
    private static final byte MARKER_SOF6 = -58;
    private static final byte MARKER_SOF7 = -57;
    private static final byte MARKER_SOF9 = -55;
    private static final byte MARKER_SOI = -40;
    private static final byte MARKER_SOS = -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    public static final short METERING_MODE_AVERAGE = 1;
    public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
    public static final short METERING_MODE_MULTI_SPOT = 4;
    public static final short METERING_MODE_OTHER = 255;
    public static final short METERING_MODE_PARTIAL = 6;
    public static final short METERING_MODE_PATTERN = 5;
    public static final short METERING_MODE_SPOT = 3;
    public static final short METERING_MODE_UNKNOWN = 0;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = {new ExifTag(TAG_ORF_PREVIEW_IMAGE_START, InputDeviceCompat.SOURCE_KEYBOARD, 4), new ExifTag(TAG_ORF_PREVIEW_IMAGE_LENGTH, 258, 4)};
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = {new ExifTag(TAG_ORF_ASPECT_FRAME, 4371, 3)};
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = {79, 76, 89, 77, 80, 0};
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = {new ExifTag(TAG_ORF_THUMBNAIL_IMAGE, 256, 7), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 4), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 4)};
    private static final short ORF_SIGNATURE_1 = 20306;
    private static final short ORF_SIGNATURE_2 = 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS = {new ExifTag(TAG_COLOR_SPACE, 55, 3)};
    public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int RAF_INFO_SIZE = 160;
    private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    public static final int REDUCED_RESOLUTION_IMAGE = 1;
    public static final short RENDERED_PROCESS_CUSTOM = 1;
    public static final short RENDERED_PROCESS_NORMAL = 0;
    public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
    public static final short RESOLUTION_UNIT_INCHES = 2;
    private static final List<Integer> ROTATION_ORDER = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(6), Integer.valueOf(3), Integer.valueOf(8)});
    private static final short RW2_SIGNATURE = 85;
    public static final short SATURATION_HIGH = 0;
    public static final short SATURATION_LOW = 0;
    public static final short SATURATION_NORMAL = 0;
    public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
    public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
    public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
    public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
    public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
    public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
    public static final short SENSITIVITY_TYPE_REI = 2;
    public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
    public static final short SENSITIVITY_TYPE_SOS = 1;
    public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
    public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
    public static final short SENSOR_TYPE_NOT_DEFINED = 1;
    public static final short SENSOR_TYPE_ONE_CHIP = 2;
    public static final short SENSOR_TYPE_THREE_CHIP = 4;
    public static final short SENSOR_TYPE_TRILINEAR = 7;
    public static final short SENSOR_TYPE_TWO_CHIP = 3;
    public static final short SHARPNESS_HARD = 2;
    public static final short SHARPNESS_NORMAL = 0;
    public static final short SHARPNESS_SOFT = 1;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    static final byte START_CODE = 42;
    public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
    public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
    public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
    public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GAMMA = "Gamma";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO_SPEED = "ISOSpeed";
    public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
    public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";
    @Deprecated
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LENS_MAKE = "LensMake";
    public static final String TAG_LENS_MODEL = "LensModel";
    public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
    public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag(TAG_STRIP_OFFSETS, ZMBaseRecyclerViewAdapter.HEADER_VIEW, 3);
    public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
    private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    @Deprecated
    public static final int WHITEBALANCE_AUTO = 0;
    @Deprecated
    public static final int WHITEBALANCE_MANUAL = 1;
    public static final short WHITE_BALANCE_AUTO = 0;
    public static final short WHITE_BALANCE_MANUAL = 1;
    public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
    public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
    private static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap<>();
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet<String> sTagSetForCompatibility = new HashSet<>(Arrays.asList(new String[]{TAG_F_NUMBER, TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP}));
    private final AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes;
    private Set<Integer> mAttributesOffsets;
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    private final String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(inputStream);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }

        public ByteOrderedDataInputStream(byte[] bArr) throws IOException {
            this((InputStream) new ByteArrayInputStream(bArr));
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void seek(long j) throws IOException {
            int i = this.mPosition;
            if (((long) i) > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                j -= (long) i;
            }
            int i2 = (int) j;
            if (skipBytes(i2) != i2) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public int peek() {
            return this.mPosition;
        }

        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public String readLine() throws IOException {
            Log.d(ExifInterface.TAG, "Currently unsupported");
            return null;
        }

        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mPosition += i2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, i, i2) != i2) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public void readFully(byte[] bArr) throws IOException {
            this.mPosition += bArr.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(bArr, 0, bArr.length) != bArr.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                if (read >= 0) {
                    return (byte) read;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (short) ((read2 << 8) + read);
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (short) ((read << 8) + read2);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public int skipBytes(int i) throws IOException {
            int min = Math.min(i, this.mLength - this.mPosition);
            int i2 = 0;
            while (i2 < min) {
                i2 += this.mDataInputStream.skipBytes(min - i2);
            }
            this.mPosition += i2;
            return i2;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                if ((read | read2) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (read2 << 8) + read;
                    }
                    if (byteOrder == BIG_ENDIAN) {
                        return (read << 8) + read2;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition <= this.mLength) {
                int read = this.mDataInputStream.read();
                int read2 = this.mDataInputStream.read();
                int read3 = this.mDataInputStream.read();
                int read4 = this.mDataInputStream.read();
                int read5 = this.mDataInputStream.read();
                int read6 = this.mDataInputStream.read();
                int read7 = this.mDataInputStream.read();
                int read8 = this.mDataInputStream.read();
                if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                    ByteOrder byteOrder = this.mByteOrder;
                    if (byteOrder == LITTLE_ENDIAN) {
                        return (((long) read8) << 56) + (((long) read7) << 48) + (((long) read6) << 40) + (((long) read5) << 32) + (((long) read4) << 24) + (((long) read3) << 16) + (((long) read2) << 8) + ((long) read);
                    }
                    int i = read2;
                    if (byteOrder == BIG_ENDIAN) {
                        return (((long) read) << 56) + (((long) i) << 48) + (((long) read3) << 40) + (((long) read4) << 32) + (((long) read5) << 24) + (((long) read6) << 16) + (((long) read7) << 8) + ((long) read8);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid byte order: ");
                    sb.append(this.mByteOrder);
                    throw new IOException(sb.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }
    }

    private static class ByteOrderedDataOutputStream extends FilterOutputStream {
        private ByteOrder mByteOrder;
        private final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void write(byte[] bArr) throws IOException {
            this.mOutputStream.write(bArr);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.mOutputStream.write(bArr, i, i2);
        }

        public void writeByte(int i) throws IOException {
            this.mOutputStream.write(i);
        }

        public void writeShort(short s) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((s >>> 0) & 255);
                this.mOutputStream.write((s >>> 8) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((s >>> 8) & 255);
                this.mOutputStream.write((s >>> 0) & 255);
            }
        }

        public void writeInt(int i) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((i >>> 0) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 24) & 255);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((i >>> 24) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 0) & 255);
            }
        }

        public void writeUnsignedShort(int i) throws IOException {
            writeShort((short) i);
        }

        public void writeUnsignedInt(long j) throws IOException {
            writeInt((int) j);
        }
    }

    private static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        ExifAttribute(int i, int i2, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytes = bArr;
        }

        public static ExifAttribute createUShort(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * iArr.length)]);
            wrap.order(byteOrder);
            for (int i : iArr) {
                wrap.putShort((short) i);
            }
            return new ExifAttribute(3, iArr.length, wrap.array());
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            return createUShort(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createULong(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * jArr.length)]);
            wrap.order(byteOrder);
            for (long j : jArr) {
                wrap.putInt((int) j);
            }
            return new ExifAttribute(4, jArr.length, wrap.array());
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            return createULong(new long[]{j}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * iArr.length)]);
            wrap.order(byteOrder);
            for (int putInt : iArr) {
                wrap.putInt(putInt);
            }
            return new ExifAttribute(9, iArr.length, wrap.array());
        }

        public static ExifAttribute createSLong(int i, ByteOrder byteOrder) {
            return createSLong(new int[]{i}, byteOrder);
        }

        public static ExifAttribute createByte(String str) {
            if (str.length() != 1 || str.charAt(0) < '0' || str.charAt(0) > '1') {
                byte[] bytes2 = str.getBytes(ExifInterface.ASCII);
                return new ExifAttribute(1, bytes2.length, bytes2);
            }
            byte[] bArr = {(byte) (str.charAt(0) - '0')};
            return new ExifAttribute(1, bArr.length, bArr);
        }

        public static ExifAttribute createString(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(0);
            byte[] bytes2 = sb.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes2.length, bytes2);
        }

        public static ExifAttribute createURational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(5, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return createURational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * rationalArr.length)]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(10, rationalArr.length, wrap.array());
        }

        public static ExifAttribute createSRational(Rational rational, ByteOrder byteOrder) {
            return createSRational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] dArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * dArr.length)]);
            wrap.order(byteOrder);
            for (double putDouble : dArr) {
                wrap.putDouble(putDouble);
            }
            return new ExifAttribute(12, dArr.length, wrap.array());
        }

        public static ExifAttribute createDouble(double d, ByteOrder byteOrder) {
            return createDouble(new double[]{d}, byteOrder);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            sb.append(", data length:");
            sb.append(this.bytes.length);
            sb.append(")");
            return sb.toString();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:164:0x01dd A[SYNTHETIC, Splitter:B:164:0x01dd] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object getValue(java.nio.ByteOrder r9) {
            /*
                r8 = this;
                r0 = 0
                androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r1 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ IOException -> 0x01c2, all -> 0x01bf }
                byte[] r2 = r8.bytes     // Catch:{ IOException -> 0x01c2, all -> 0x01bf }
                r1.<init>(r2)     // Catch:{ IOException -> 0x01c2, all -> 0x01bf }
                r1.setByteOrder(r9)     // Catch:{ IOException -> 0x01bd }
                int r9 = r8.format     // Catch:{ IOException -> 0x01bd }
                r2 = 1
                r3 = 0
                switch(r9) {
                    case 1: goto L_0x016c;
                    case 2: goto L_0x0119;
                    case 3: goto L_0x00fb;
                    case 4: goto L_0x00dd;
                    case 5: goto L_0x00b6;
                    case 6: goto L_0x016c;
                    case 7: goto L_0x0119;
                    case 8: goto L_0x0098;
                    case 9: goto L_0x007a;
                    case 10: goto L_0x0051;
                    case 11: goto L_0x0032;
                    case 12: goto L_0x0014;
                    default: goto L_0x0012;
                }     // Catch:{ IOException -> 0x01bd }
            L_0x0012:
                goto L_0x01b0
            L_0x0014:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                double[] r9 = new double[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x0018:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x0025
                double r4 = r1.readDouble()     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r4     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x0018
            L_0x0025:
                r1.close()     // Catch:{ IOException -> 0x0029 }
                goto L_0x0031
            L_0x0029:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0031:
                return r9
            L_0x0032:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                double[] r9 = new double[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x0036:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x0044
                float r2 = r1.readFloat()     // Catch:{ IOException -> 0x01bd }
                double r4 = (double) r2     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r4     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x0036
            L_0x0044:
                r1.close()     // Catch:{ IOException -> 0x0048 }
                goto L_0x0050
            L_0x0048:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0050:
                return r9
            L_0x0051:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                androidx.exifinterface.media.ExifInterface$Rational[] r9 = new androidx.exifinterface.media.ExifInterface.Rational[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x0055:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x006d
                int r2 = r1.readInt()     // Catch:{ IOException -> 0x01bd }
                long r4 = (long) r2     // Catch:{ IOException -> 0x01bd }
                int r2 = r1.readInt()     // Catch:{ IOException -> 0x01bd }
                long r6 = (long) r2     // Catch:{ IOException -> 0x01bd }
                androidx.exifinterface.media.ExifInterface$Rational r2 = new androidx.exifinterface.media.ExifInterface$Rational     // Catch:{ IOException -> 0x01bd }
                r2.<init>(r4, r6)     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r2     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x0055
            L_0x006d:
                r1.close()     // Catch:{ IOException -> 0x0071 }
                goto L_0x0079
            L_0x0071:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0079:
                return r9
            L_0x007a:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                int[] r9 = new int[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x007e:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x008b
                int r2 = r1.readInt()     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r2     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x007e
            L_0x008b:
                r1.close()     // Catch:{ IOException -> 0x008f }
                goto L_0x0097
            L_0x008f:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0097:
                return r9
            L_0x0098:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                int[] r9 = new int[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x009c:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x00a9
                short r2 = r1.readShort()     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r2     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x009c
            L_0x00a9:
                r1.close()     // Catch:{ IOException -> 0x00ad }
                goto L_0x00b5
            L_0x00ad:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x00b5:
                return r9
            L_0x00b6:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                androidx.exifinterface.media.ExifInterface$Rational[] r9 = new androidx.exifinterface.media.ExifInterface.Rational[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x00ba:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x00d0
                long r4 = r1.readUnsignedInt()     // Catch:{ IOException -> 0x01bd }
                long r6 = r1.readUnsignedInt()     // Catch:{ IOException -> 0x01bd }
                androidx.exifinterface.media.ExifInterface$Rational r2 = new androidx.exifinterface.media.ExifInterface$Rational     // Catch:{ IOException -> 0x01bd }
                r2.<init>(r4, r6)     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r2     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x00ba
            L_0x00d0:
                r1.close()     // Catch:{ IOException -> 0x00d4 }
                goto L_0x00dc
            L_0x00d4:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x00dc:
                return r9
            L_0x00dd:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                long[] r9 = new long[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x00e1:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x00ee
                long r4 = r1.readUnsignedInt()     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r4     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x00e1
            L_0x00ee:
                r1.close()     // Catch:{ IOException -> 0x00f2 }
                goto L_0x00fa
            L_0x00f2:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x00fa:
                return r9
            L_0x00fb:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                int[] r9 = new int[r9]     // Catch:{ IOException -> 0x01bd }
            L_0x00ff:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x010c
                int r2 = r1.readUnsignedShort()     // Catch:{ IOException -> 0x01bd }
                r9[r3] = r2     // Catch:{ IOException -> 0x01bd }
                int r3 = r3 + 1
                goto L_0x00ff
            L_0x010c:
                r1.close()     // Catch:{ IOException -> 0x0110 }
                goto L_0x0118
            L_0x0110:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0118:
                return r9
            L_0x0119:
                int r9 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                byte[] r4 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x01bd }
                int r4 = r4.length     // Catch:{ IOException -> 0x01bd }
                if (r9 < r4) goto L_0x013a
                r9 = 0
            L_0x0121:
                byte[] r4 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x01bd }
                int r4 = r4.length     // Catch:{ IOException -> 0x01bd }
                if (r9 >= r4) goto L_0x0135
                byte[] r4 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                byte r4 = r4[r9]     // Catch:{ IOException -> 0x01bd }
                byte[] r5 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x01bd }
                byte r5 = r5[r9]     // Catch:{ IOException -> 0x01bd }
                if (r4 == r5) goto L_0x0132
                r2 = 0
                goto L_0x0135
            L_0x0132:
                int r9 = r9 + 1
                goto L_0x0121
            L_0x0135:
                if (r2 == 0) goto L_0x013a
                byte[] r9 = androidx.exifinterface.media.ExifInterface.EXIF_ASCII_PREFIX     // Catch:{ IOException -> 0x01bd }
                int r3 = r9.length     // Catch:{ IOException -> 0x01bd }
            L_0x013a:
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01bd }
                r9.<init>()     // Catch:{ IOException -> 0x01bd }
            L_0x013f:
                int r2 = r8.numberOfComponents     // Catch:{ IOException -> 0x01bd }
                if (r3 >= r2) goto L_0x015b
                byte[] r2 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                byte r2 = r2[r3]     // Catch:{ IOException -> 0x01bd }
                if (r2 != 0) goto L_0x014a
                goto L_0x015b
            L_0x014a:
                r4 = 32
                if (r2 < r4) goto L_0x0153
                char r2 = (char) r2     // Catch:{ IOException -> 0x01bd }
                r9.append(r2)     // Catch:{ IOException -> 0x01bd }
                goto L_0x0158
            L_0x0153:
                r2 = 63
                r9.append(r2)     // Catch:{ IOException -> 0x01bd }
            L_0x0158:
                int r3 = r3 + 1
                goto L_0x013f
            L_0x015b:
                java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01bd }
                r1.close()     // Catch:{ IOException -> 0x0163 }
                goto L_0x016b
            L_0x0163:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x016b:
                return r9
            L_0x016c:
                byte[] r9 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                int r9 = r9.length     // Catch:{ IOException -> 0x01bd }
                if (r9 != r2) goto L_0x019a
                byte[] r9 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                byte r9 = r9[r3]     // Catch:{ IOException -> 0x01bd }
                if (r9 < 0) goto L_0x019a
                byte[] r9 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                byte r9 = r9[r3]     // Catch:{ IOException -> 0x01bd }
                if (r9 > r2) goto L_0x019a
                java.lang.String r9 = new java.lang.String     // Catch:{ IOException -> 0x01bd }
                char[] r2 = new char[r2]     // Catch:{ IOException -> 0x01bd }
                byte[] r4 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                byte r4 = r4[r3]     // Catch:{ IOException -> 0x01bd }
                int r4 = r4 + 48
                char r4 = (char) r4     // Catch:{ IOException -> 0x01bd }
                r2[r3] = r4     // Catch:{ IOException -> 0x01bd }
                r9.<init>(r2)     // Catch:{ IOException -> 0x01bd }
                r1.close()     // Catch:{ IOException -> 0x0191 }
                goto L_0x0199
            L_0x0191:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x0199:
                return r9
            L_0x019a:
                java.lang.String r9 = new java.lang.String     // Catch:{ IOException -> 0x01bd }
                byte[] r2 = r8.bytes     // Catch:{ IOException -> 0x01bd }
                java.nio.charset.Charset r3 = androidx.exifinterface.media.ExifInterface.ASCII     // Catch:{ IOException -> 0x01bd }
                r9.<init>(r2, r3)     // Catch:{ IOException -> 0x01bd }
                r1.close()     // Catch:{ IOException -> 0x01a7 }
                goto L_0x01af
            L_0x01a7:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x01af:
                return r9
            L_0x01b0:
                r1.close()     // Catch:{ IOException -> 0x01b4 }
                goto L_0x01bc
            L_0x01b4:
                r9 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r9)
            L_0x01bc:
                return r0
            L_0x01bd:
                r9 = move-exception
                goto L_0x01c4
            L_0x01bf:
                r9 = move-exception
                r1 = r0
                goto L_0x01db
            L_0x01c2:
                r9 = move-exception
                r1 = r0
            L_0x01c4:
                java.lang.String r2 = "ExifInterface"
                java.lang.String r3 = "IOException occurred during reading a value"
                android.util.Log.w(r2, r3, r9)     // Catch:{ all -> 0x01da }
                if (r1 == 0) goto L_0x01d9
                r1.close()     // Catch:{ IOException -> 0x01d1 }
                goto L_0x01d9
            L_0x01d1:
                r9 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r9)
            L_0x01d9:
                return r0
            L_0x01da:
                r9 = move-exception
            L_0x01db:
                if (r1 == 0) goto L_0x01e9
                r1.close()     // Catch:{ IOException -> 0x01e1 }
                goto L_0x01e9
            L_0x01e1:
                r0 = move-exception
                java.lang.String r1 = "ExifInterface"
                java.lang.String r2 = "IOException occurred while closing InputStream"
                android.util.Log.e(r1, r2, r0)
            L_0x01e9:
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (double) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return (double) iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        return rationalArr[0].calculate();
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i < jArr.length) {
                    sb.append(jArr[i]);
                    i++;
                    if (i != jArr.length) {
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                return sb.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i < iArr.length) {
                    sb.append(iArr[i]);
                    i++;
                    if (i != iArr.length) {
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                return sb.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i < dArr.length) {
                    sb.append(dArr[i]);
                    i++;
                    if (i != dArr.length) {
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                return sb.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    sb.append(rationalArr[i].numerator);
                    sb.append('/');
                    sb.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                return sb.toString();
            }
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String str, int i, int i2) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        ExifTag(String str, int i, int i2, int i3) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }

        /* access modifiers changed from: 0000 */
        public boolean isFormatCompatible(int i) {
            int i2 = this.primaryFormat;
            if (!(i2 == 7 || i == 7 || i2 == i)) {
                int i3 = this.secondaryFormat;
                if (i3 != i) {
                    if ((i2 == 4 || i3 == 4) && i == 3) {
                        return true;
                    }
                    if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && i == 8) {
                        return true;
                    }
                    if ((this.primaryFormat == 12 || this.secondaryFormat == 12) && i == 11) {
                        return true;
                    }
                    return false;
                }
            }
            return true;
        }
    }

    @RestrictTo({Scope.LIBRARY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IfdType {
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        Rational(double d) {
            this((long) (d * 10000.0d), 10000);
        }

        Rational(long j, long j2) {
            if (j2 == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.numerator);
            sb.append("/");
            sb.append(this.denominator);
            return sb.toString();
        }

        public double calculate() {
            return ((double) this.numerator) / ((double) this.denominator);
        }
    }

    static {
        ExifTag[] exifTagArr;
        ExifTag[] exifTagArr2 = IFD_TIFF_TAGS;
        EXIF_TAGS = new ExifTag[][]{exifTagArr2, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, exifTagArr2, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS};
        ExifTag[][] exifTagArr3 = EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[exifTagArr3.length];
        sExifTagMapsForWriting = new HashMap[exifTagArr3.length];
        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            sExifTagMapsForReading[i] = new HashMap<>();
            sExifTagMapsForWriting[i] = new HashMap<>();
            for (ExifTag exifTag : EXIF_TAGS[i]) {
                sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
            }
        }
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), Integer.valueOf(5));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), Integer.valueOf(1));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), Integer.valueOf(2));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[3].number), Integer.valueOf(3));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), Integer.valueOf(7));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), Integer.valueOf(8));
    }

    public ExifInterface(@NonNull String str) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (str != null) {
            FileInputStream fileInputStream = null;
            this.mAssetInputStream = null;
            this.mFilename = str;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(str);
                try {
                    loadAttributes(fileInputStream2);
                    closeQuietly(fileInputStream2);
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    closeQuietly(fileInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                closeQuietly(fileInputStream);
                throw th;
            }
        } else {
            throw new IllegalArgumentException("filename cannot be null");
        }
    }

    public ExifInterface(@NonNull InputStream inputStream) throws IOException {
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        if (inputStream != null) {
            this.mFilename = null;
            if (inputStream instanceof AssetInputStream) {
                this.mAssetInputStream = (AssetInputStream) inputStream;
            } else {
                this.mAssetInputStream = null;
            }
            loadAttributes(inputStream);
            return;
        }
        throw new IllegalArgumentException("inputStream cannot be null");
    }

    @Nullable
    private ExifAttribute getExifAttribute(@NonNull String str) {
        if (TAG_ISO_SPEED_RATINGS.equals(str)) {
            str = TAG_PHOTOGRAPHIC_SENSITIVITY;
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(str);
            if (exifAttribute != null) {
                return exifAttribute;
            }
        }
        return null;
    }

    @Nullable
    public String getAttribute(@NonNull String str) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute == null) {
            return null;
        }
        if (!sTagSetForCompatibility.contains(str)) {
            return exifAttribute.getStringValue(this.mExifByteOrder);
        }
        if (!str.equals(TAG_GPS_TIMESTAMP)) {
            try {
                return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
            } catch (NumberFormatException unused) {
                return null;
            }
        } else if (exifAttribute.format == 5 || exifAttribute.format == 10) {
            Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
            if (rationalArr == null || rationalArr.length != 3) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid GPS Timestamp array. array=");
                sb.append(Arrays.toString(rationalArr));
                Log.w(str2, sb.toString());
                return null;
            }
            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) rationalArr[0].numerator) / ((float) rationalArr[0].denominator))), Integer.valueOf((int) (((float) rationalArr[1].numerator) / ((float) rationalArr[1].denominator))), Integer.valueOf((int) (((float) rationalArr[2].numerator) / ((float) rationalArr[2].denominator)))});
        } else {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("GPS Timestamp format is not rational. format=");
            sb2.append(exifAttribute.format);
            Log.w(str3, sb2.toString());
            return null;
        }
    }

    public int getAttributeInt(@NonNull String str, int i) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute == null) {
            return i;
        }
        try {
            return exifAttribute.getIntValue(this.mExifByteOrder);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public double getAttributeDouble(@NonNull String str, double d) {
        ExifAttribute exifAttribute = getExifAttribute(str);
        if (exifAttribute == null) {
            return d;
        }
        try {
            return exifAttribute.getDoubleValue(this.mExifByteOrder);
        } catch (NumberFormatException unused) {
            return d;
        }
    }

    public void setAttribute(@NonNull String str, @Nullable String str2) {
        int i;
        String str3;
        String str4;
        String str5 = str2;
        String str6 = str;
        String str7 = TAG_ISO_SPEED_RATINGS.equals(str6) ? TAG_PHOTOGRAPHIC_SENSITIVITY : str6;
        int i2 = 2;
        int i3 = 1;
        if (str5 != null && sTagSetForCompatibility.contains(str7)) {
            if (str7.equals(TAG_GPS_TIMESTAMP)) {
                Matcher matcher = sGpsTimestampPattern.matcher(str5);
                if (!matcher.find()) {
                    String str8 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid value for ");
                    sb.append(str7);
                    sb.append(" : ");
                    sb.append(str5);
                    Log.w(str8, sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Integer.parseInt(matcher.group(1)));
                sb2.append("/1,");
                sb2.append(Integer.parseInt(matcher.group(2)));
                sb2.append("/1,");
                sb2.append(Integer.parseInt(matcher.group(3)));
                sb2.append("/1");
                str5 = sb2.toString();
            } else {
                try {
                    str5 = new Rational(Double.parseDouble(str2)).toString();
                } catch (NumberFormatException unused) {
                    String str9 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid value for ");
                    sb3.append(str7);
                    sb3.append(" : ");
                    sb3.append(str5);
                    Log.w(str9, sb3.toString());
                    return;
                }
            }
        }
        int i4 = 0;
        while (i4 < EXIF_TAGS.length) {
            if (i4 != 4 || this.mHasThumbnail) {
                ExifTag exifTag = (ExifTag) sExifTagMapsForWriting[i4].get(str7);
                if (exifTag != null) {
                    if (str5 != null) {
                        Pair guessDataFormat = guessDataFormat(str5);
                        if (exifTag.primaryFormat == ((Integer) guessDataFormat.first).intValue() || exifTag.primaryFormat == ((Integer) guessDataFormat.second).intValue()) {
                            i = exifTag.primaryFormat;
                        } else if (exifTag.secondaryFormat != -1 && (exifTag.secondaryFormat == ((Integer) guessDataFormat.first).intValue() || exifTag.secondaryFormat == ((Integer) guessDataFormat.second).intValue())) {
                            i = exifTag.secondaryFormat;
                        } else if (exifTag.primaryFormat == i3 || exifTag.primaryFormat == 7 || exifTag.primaryFormat == i2) {
                            i = exifTag.primaryFormat;
                        } else {
                            String str10 = TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Given tag (");
                            sb4.append(str7);
                            sb4.append(") value didn't match with one of expected ");
                            sb4.append("formats: ");
                            sb4.append(IFD_FORMAT_NAMES[exifTag.primaryFormat]);
                            if (exifTag.secondaryFormat == -1) {
                                str3 = "";
                            } else {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(", ");
                                sb5.append(IFD_FORMAT_NAMES[exifTag.secondaryFormat]);
                                str3 = sb5.toString();
                            }
                            sb4.append(str3);
                            sb4.append(" (guess: ");
                            sb4.append(IFD_FORMAT_NAMES[((Integer) guessDataFormat.first).intValue()]);
                            if (((Integer) guessDataFormat.second).intValue() == -1) {
                                str4 = "";
                            } else {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(", ");
                                sb6.append(IFD_FORMAT_NAMES[((Integer) guessDataFormat.second).intValue()]);
                                str4 = sb6.toString();
                            }
                            sb4.append(str4);
                            sb4.append(")");
                            Log.w(str10, sb4.toString());
                        }
                        switch (i) {
                            case 1:
                                this.mAttributes[i4].put(str7, ExifAttribute.createByte(str5));
                                break;
                            case 2:
                            case 7:
                                this.mAttributes[i4].put(str7, ExifAttribute.createString(str5));
                                break;
                            case 3:
                                String[] split = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                int[] iArr = new int[split.length];
                                for (int i5 = 0; i5 < split.length; i5++) {
                                    iArr[i5] = Integer.parseInt(split[i5]);
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createUShort(iArr, this.mExifByteOrder));
                                break;
                            case 4:
                                String[] split2 = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                long[] jArr = new long[split2.length];
                                for (int i6 = 0; i6 < split2.length; i6++) {
                                    jArr[i6] = Long.parseLong(split2[i6]);
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createULong(jArr, this.mExifByteOrder));
                                break;
                            case 5:
                                String[] split3 = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                Rational[] rationalArr = new Rational[split3.length];
                                for (int i7 = 0; i7 < split3.length; i7++) {
                                    String[] split4 = split3[i7].split("/", -1);
                                    rationalArr[i7] = new Rational((long) Double.parseDouble(split4[0]), (long) Double.parseDouble(split4[1]));
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createURational(rationalArr, this.mExifByteOrder));
                                break;
                            case 9:
                                String[] split5 = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                int[] iArr2 = new int[split5.length];
                                for (int i8 = 0; i8 < split5.length; i8++) {
                                    iArr2[i8] = Integer.parseInt(split5[i8]);
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createSLong(iArr2, this.mExifByteOrder));
                                break;
                            case 10:
                                String[] split6 = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                Rational[] rationalArr2 = new Rational[split6.length];
                                int i9 = 0;
                                while (i9 < split6.length) {
                                    String[] split7 = split6[i9].split("/", -1);
                                    rationalArr2[i9] = new Rational((long) Double.parseDouble(split7[0]), (long) Double.parseDouble(split7[i3]));
                                    i9++;
                                    i3 = 1;
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createSRational(rationalArr2, this.mExifByteOrder));
                                break;
                            case 12:
                                String[] split8 = str5.split(PreferencesConstants.COOKIE_DELIMITER, -1);
                                double[] dArr = new double[split8.length];
                                for (int i10 = 0; i10 < split8.length; i10++) {
                                    dArr[i10] = Double.parseDouble(split8[i10]);
                                }
                                this.mAttributes[i4].put(str7, ExifAttribute.createDouble(dArr, this.mExifByteOrder));
                                break;
                            default:
                                String str11 = TAG;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("Data format isn't one of expected formats: ");
                                sb7.append(i);
                                Log.w(str11, sb7.toString());
                                break;
                        }
                    } else {
                        this.mAttributes[i4].remove(str7);
                    }
                }
            }
            i4++;
            i2 = 2;
            i3 = 1;
        }
    }

    public void resetOrientation() {
        setAttribute(TAG_ORIENTATION, Integer.toString(1));
    }

    public void rotate(int i) {
        if (i % 90 == 0) {
            int attributeInt = getAttributeInt(TAG_ORIENTATION, 1);
            int i2 = 0;
            if (ROTATION_ORDER.contains(Integer.valueOf(attributeInt))) {
                int indexOf = (ROTATION_ORDER.indexOf(Integer.valueOf(attributeInt)) + (i / 90)) % 4;
                if (indexOf < 0) {
                    i2 = 4;
                }
                i2 = ((Integer) ROTATION_ORDER.get(indexOf + i2)).intValue();
            } else if (FLIPPED_ROTATION_ORDER.contains(Integer.valueOf(attributeInt))) {
                int indexOf2 = (FLIPPED_ROTATION_ORDER.indexOf(Integer.valueOf(attributeInt)) + (i / 90)) % 4;
                if (indexOf2 < 0) {
                    i2 = 4;
                }
                i2 = ((Integer) FLIPPED_ROTATION_ORDER.get(indexOf2 + i2)).intValue();
            }
            setAttribute(TAG_ORIENTATION, Integer.toString(i2));
            return;
        }
        throw new IllegalArgumentException("degree should be a multiple of 90");
    }

    public void flipVertically() {
        int i = 1;
        switch (getAttributeInt(TAG_ORIENTATION, 1)) {
            case 1:
                i = 4;
                break;
            case 2:
                i = 3;
                break;
            case 3:
                i = 2;
                break;
            case 4:
                break;
            case 5:
                i = 8;
                break;
            case 6:
                i = 7;
                break;
            case 7:
                i = 6;
                break;
            case 8:
                i = 5;
                break;
            default:
                i = 0;
                break;
        }
        setAttribute(TAG_ORIENTATION, Integer.toString(i));
    }

    public void flipHorizontally() {
        int i = 1;
        switch (getAttributeInt(TAG_ORIENTATION, 1)) {
            case 1:
                i = 2;
                break;
            case 2:
                break;
            case 3:
                i = 4;
                break;
            case 4:
                i = 3;
                break;
            case 5:
                i = 6;
                break;
            case 6:
                i = 5;
                break;
            case 7:
                i = 8;
                break;
            case 8:
                i = 7;
                break;
            default:
                i = 0;
                break;
        }
        setAttribute(TAG_ORIENTATION, Integer.toString(i));
    }

    public boolean isFlipped() {
        int attributeInt = getAttributeInt(TAG_ORIENTATION, 1);
        if (!(attributeInt == 2 || attributeInt == 7)) {
            switch (attributeInt) {
                case 4:
                case 5:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public int getRotationDegrees() {
        switch (getAttributeInt(TAG_ORIENTATION, 1)) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 8:
                return SubsamplingScaleImageView.ORIENTATION_270;
            case 6:
            case 7:
                return 90;
            default:
                return 0;
        }
    }

    private boolean updateAttribute(String str, ExifAttribute exifAttribute) {
        boolean z = false;
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            if (this.mAttributes[i].containsKey(str)) {
                this.mAttributes[i].put(str, exifAttribute);
                z = true;
            }
        }
        return z;
    }

    private void removeAttribute(String str) {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            this.mAttributes[i].remove(str);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0048, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r4.mIsSupportedFile = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        addDefaultValuesForCompatibility();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0053, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadAttributes(@androidx.annotation.NonNull java.io.InputStream r5) throws java.io.IOException {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
        L_0x0002:
            androidx.exifinterface.media.ExifInterface$ExifTag[][] r2 = EXIF_TAGS     // Catch:{ IOException -> 0x004a }
            int r2 = r2.length     // Catch:{ IOException -> 0x004a }
            if (r1 >= r2) goto L_0x0013
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r4.mAttributes     // Catch:{ IOException -> 0x004a }
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ IOException -> 0x004a }
            r3.<init>()     // Catch:{ IOException -> 0x004a }
            r2[r1] = r3     // Catch:{ IOException -> 0x004a }
            int r1 = r1 + 1
            goto L_0x0002
        L_0x0013:
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x004a }
            r2 = 5000(0x1388, float:7.006E-42)
            r1.<init>(r5, r2)     // Catch:{ IOException -> 0x004a }
            r5 = r1
            java.io.BufferedInputStream r5 = (java.io.BufferedInputStream) r5     // Catch:{ IOException -> 0x004a }
            int r5 = r4.getMimeType(r5)     // Catch:{ IOException -> 0x004a }
            r4.mMimeType = r5     // Catch:{ IOException -> 0x004a }
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r5 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch:{ IOException -> 0x004a }
            r5.<init>(r1)     // Catch:{ IOException -> 0x004a }
            int r1 = r4.mMimeType     // Catch:{ IOException -> 0x004a }
            switch(r1) {
                case 0: goto L_0x003e;
                case 1: goto L_0x003e;
                case 2: goto L_0x003e;
                case 3: goto L_0x003e;
                case 4: goto L_0x003a;
                case 5: goto L_0x003e;
                case 6: goto L_0x003e;
                case 7: goto L_0x0036;
                case 8: goto L_0x003e;
                case 9: goto L_0x0032;
                case 10: goto L_0x002e;
                case 11: goto L_0x003e;
                default: goto L_0x002d;
            }     // Catch:{ IOException -> 0x004a }
        L_0x002d:
            goto L_0x0041
        L_0x002e:
            r4.getRw2Attributes(r5)     // Catch:{ IOException -> 0x004a }
            goto L_0x0041
        L_0x0032:
            r4.getRafAttributes(r5)     // Catch:{ IOException -> 0x004a }
            goto L_0x0041
        L_0x0036:
            r4.getOrfAttributes(r5)     // Catch:{ IOException -> 0x004a }
            goto L_0x0041
        L_0x003a:
            r4.getJpegAttributes(r5, r0, r0)     // Catch:{ IOException -> 0x004a }
            goto L_0x0041
        L_0x003e:
            r4.getRawAttributes(r5)     // Catch:{ IOException -> 0x004a }
        L_0x0041:
            r4.setThumbnailData(r5)     // Catch:{ IOException -> 0x004a }
            r5 = 1
            r4.mIsSupportedFile = r5     // Catch:{ IOException -> 0x004a }
            goto L_0x004c
        L_0x0048:
            r5 = move-exception
            goto L_0x0050
        L_0x004a:
            r4.mIsSupportedFile = r0     // Catch:{ all -> 0x0048 }
        L_0x004c:
            r4.addDefaultValuesForCompatibility()
            return
        L_0x0050:
            r4.addDefaultValuesForCompatibility()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.loadAttributes(java.io.InputStream):void");
    }

    private void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; i++) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("The size of tag group[");
            sb.append(i);
            sb.append("]: ");
            sb.append(this.mAttributes[i].size());
            Log.d(str, sb.toString());
            for (Entry entry : this.mAttributes[i].entrySet()) {
                ExifAttribute exifAttribute = (ExifAttribute) entry.getValue();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("tagName: ");
                sb2.append((String) entry.getKey());
                sb2.append(", tagType: ");
                sb2.append(exifAttribute.toString());
                sb2.append(", tagValue: '");
                sb2.append(exifAttribute.getStringValue(this.mExifByteOrder));
                sb2.append("'");
                Log.d(str2, sb2.toString());
            }
        }
    }

    public void saveAttributes() throws IOException {
        Throwable th;
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        if (!this.mIsSupportedFile || this.mMimeType != 4) {
            throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
        } else if (this.mFilename != null) {
            this.mThumbnailBytes = getThumbnail();
            StringBuilder sb = new StringBuilder();
            sb.append(this.mFilename);
            sb.append(".tmp");
            File file = new File(sb.toString());
            if (new File(this.mFilename).renameTo(file)) {
                FileOutputStream fileOutputStream2 = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        fileOutputStream = new FileOutputStream(this.mFilename);
                    } catch (Throwable th2) {
                        th = th2;
                        closeQuietly(fileInputStream);
                        closeQuietly(fileOutputStream2);
                        file.delete();
                        throw th;
                    }
                    try {
                        saveJpegAttributes(fileInputStream, fileOutputStream);
                        closeQuietly(fileInputStream);
                        closeQuietly(fileOutputStream);
                        file.delete();
                        this.mThumbnailBytes = null;
                    } catch (Throwable th3) {
                        FileOutputStream fileOutputStream3 = fileOutputStream;
                        th = th3;
                        fileOutputStream2 = fileOutputStream3;
                        closeQuietly(fileInputStream);
                        closeQuietly(fileOutputStream2);
                        file.delete();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fileInputStream = null;
                    closeQuietly(fileInputStream);
                    closeQuietly(fileOutputStream2);
                    file.delete();
                    throw th;
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not rename to ");
                sb2.append(file.getAbsolutePath());
                throw new IOException(sb2.toString());
            }
        } else {
            throw new IOException("ExifInterface does not support saving attributes for the current input.");
        }
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    @Nullable
    public byte[] getThumbnail() {
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return getThumbnailBytes();
        }
        return null;
    }

    @Nullable
    public byte[] getThumbnailBytes() {
        InputStream inputStream;
        if (!this.mHasThumbnail) {
            return null;
        }
        byte[] bArr = this.mThumbnailBytes;
        if (bArr != null) {
            return bArr;
        }
        try {
            if (this.mAssetInputStream != null) {
                inputStream = this.mAssetInputStream;
                try {
                    if (inputStream.markSupported()) {
                        inputStream.reset();
                    } else {
                        Log.d(TAG, "Cannot read thumbnail from inputstream without mark/reset support");
                        closeQuietly(inputStream);
                        return null;
                    }
                } catch (IOException e) {
                    e = e;
                    try {
                        Log.d(TAG, "Encountered exception while getting thumbnail", e);
                        closeQuietly(inputStream);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(inputStream);
                        throw th;
                    }
                }
            } else {
                inputStream = this.mFilename != null ? new FileInputStream(this.mFilename) : null;
            }
            if (inputStream == null) {
                throw new FileNotFoundException();
            } else if (inputStream.skip((long) this.mThumbnailOffset) == ((long) this.mThumbnailOffset)) {
                byte[] bArr2 = new byte[this.mThumbnailLength];
                if (inputStream.read(bArr2) == this.mThumbnailLength) {
                    this.mThumbnailBytes = bArr2;
                    closeQuietly(inputStream);
                    return bArr2;
                }
                throw new IOException("Corrupted image");
            } else {
                throw new IOException("Corrupted image");
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
            Log.d(TAG, "Encountered exception while getting thumbnail", e);
            closeQuietly(inputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            closeQuietly(inputStream);
            throw th;
        }
    }

    @Nullable
    public Bitmap getThumbnailBitmap() {
        if (!this.mHasThumbnail) {
            return null;
        }
        if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = getThumbnailBytes();
        }
        int i = this.mThumbnailCompression;
        if (i == 6 || i == 7) {
            return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength);
        }
        if (i == 1) {
            int[] iArr = new int[(this.mThumbnailBytes.length / 3)];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                byte[] bArr = this.mThumbnailBytes;
                int i3 = i2 * 3;
                iArr[i2] = (bArr[i3] << Ascii.DLE) + 0 + (bArr[i3 + 1] << 8) + bArr[i3 + 2];
            }
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_LENGTH);
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[4].get(TAG_IMAGE_WIDTH);
            if (!(exifAttribute == null || exifAttribute2 == null)) {
                return Bitmap.createBitmap(iArr, exifAttribute2.getIntValue(this.mExifByteOrder), exifAttribute.getIntValue(this.mExifByteOrder), Config.ARGB_8888);
            }
        }
        return null;
    }

    public boolean isThumbnailCompressed() {
        int i = this.mThumbnailCompression;
        return i == 6 || i == 7;
    }

    @Nullable
    public long[] getThumbnailRange() {
        if (!this.mHasThumbnail) {
            return null;
        }
        return new long[]{(long) this.mThumbnailOffset, (long) this.mThumbnailLength};
    }

    @Deprecated
    public boolean getLatLong(float[] fArr) {
        double[] latLong = getLatLong();
        if (latLong == null) {
            return false;
        }
        fArr[0] = (float) latLong[0];
        fArr[1] = (float) latLong[1];
        return true;
    }

    @Nullable
    public double[] getLatLong() {
        String attribute = getAttribute(TAG_GPS_LATITUDE);
        String attribute2 = getAttribute(TAG_GPS_LATITUDE_REF);
        String attribute3 = getAttribute(TAG_GPS_LONGITUDE);
        String attribute4 = getAttribute(TAG_GPS_LONGITUDE_REF);
        if (!(attribute == null || attribute2 == null || attribute3 == null || attribute4 == null)) {
            try {
                return new double[]{convertRationalLatLonToDouble(attribute, attribute2), convertRationalLatLonToDouble(attribute3, attribute4)};
            } catch (IllegalArgumentException unused) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Latitude/longitude values are not parseable. ");
                sb.append(String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", new Object[]{attribute, attribute2, attribute3, attribute4}));
                Log.w(str, sb.toString());
            }
        }
        return null;
    }

    public void setGpsInfo(Location location) {
        if (location != null) {
            setAttribute(TAG_GPS_PROCESSING_METHOD, location.getProvider());
            setLatLong(location.getLatitude(), location.getLongitude());
            setAltitude(location.getAltitude());
            setAttribute(TAG_GPS_SPEED_REF, "K");
            setAttribute(TAG_GPS_SPEED, new Rational((double) ((location.getSpeed() * ((float) TimeUnit.HOURS.toSeconds(1))) / 1000.0f)).toString());
            String[] split = sFormatter.format(new Date(location.getTime())).split("\\s+", -1);
            setAttribute(TAG_GPS_DATESTAMP, split[0]);
            setAttribute(TAG_GPS_TIMESTAMP, split[1]);
        }
    }

    public void setLatLong(double d, double d2) {
        if (d < -90.0d || d > 90.0d || Double.isNaN(d)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Latitude value ");
            sb.append(d);
            sb.append(" is not valid.");
            throw new IllegalArgumentException(sb.toString());
        } else if (d2 < -180.0d || d2 > 180.0d || Double.isNaN(d2)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Longitude value ");
            sb2.append(d2);
            sb2.append(" is not valid.");
            throw new IllegalArgumentException(sb2.toString());
        } else {
            setAttribute(TAG_GPS_LATITUDE_REF, d >= 0.0d ? "N" : LATITUDE_SOUTH);
            setAttribute(TAG_GPS_LATITUDE, convertDecimalDegree(Math.abs(d)));
            setAttribute(TAG_GPS_LONGITUDE_REF, d2 >= 0.0d ? LONGITUDE_EAST : LONGITUDE_WEST);
            setAttribute(TAG_GPS_LONGITUDE, convertDecimalDegree(Math.abs(d2)));
        }
    }

    public double getAltitude(double d) {
        double attributeDouble = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
        int attributeInt = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (attributeDouble < 0.0d || attributeInt < 0) {
            return d;
        }
        int i = 1;
        if (attributeInt == 1) {
            i = -1;
        }
        return attributeDouble * ((double) i);
    }

    public void setAltitude(double d) {
        String str = d >= 0.0d ? "0" : "1";
        setAttribute(TAG_GPS_ALTITUDE, new Rational(Math.abs(d)).toString());
        setAttribute(TAG_GPS_ALTITUDE_REF, str);
    }

    @RestrictTo({Scope.LIBRARY})
    public void setDateTime(long j) {
        long j2 = j % 1000;
        setAttribute(TAG_DATETIME, sFormatter.format(new Date(j)));
        setAttribute(TAG_SUBSEC_TIME, Long.toString(j2));
    }

    @RestrictTo({Scope.LIBRARY})
    public long getDateTime() {
        String attribute = getAttribute(TAG_DATETIME);
        if (attribute == null || !sNonZeroTimePattern.matcher(attribute).matches()) {
            return -1;
        }
        try {
            Date parse = sFormatter.parse(attribute, new ParsePosition(0));
            if (parse == null) {
                return -1;
            }
            long time = parse.getTime();
            String attribute2 = getAttribute(TAG_SUBSEC_TIME);
            if (attribute2 != null) {
                try {
                    long parseLong = Long.parseLong(attribute2);
                    while (parseLong > 1000) {
                        parseLong /= 10;
                    }
                    time += parseLong;
                } catch (NumberFormatException unused) {
                }
            }
            return time;
        } catch (IllegalArgumentException unused2) {
            return -1;
        }
    }

    @RestrictTo({Scope.LIBRARY})
    public long getGpsDateTime() {
        String attribute = getAttribute(TAG_GPS_DATESTAMP);
        String attribute2 = getAttribute(TAG_GPS_TIMESTAMP);
        if (attribute == null || attribute2 == null || (!sNonZeroTimePattern.matcher(attribute).matches() && !sNonZeroTimePattern.matcher(attribute2).matches())) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(attribute);
        sb.append(TokenParser.f498SP);
        sb.append(attribute2);
        try {
            Date parse = sFormatter.parse(sb.toString(), new ParsePosition(0));
            if (parse == null) {
                return -1;
            }
            return parse.getTime();
        } catch (IllegalArgumentException unused) {
            return -1;
        }
    }

    private static double convertRationalLatLonToDouble(String str, String str2) {
        try {
            String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER, -1);
            String[] split2 = split[0].split("/", -1);
            double parseDouble = Double.parseDouble(split2[0].trim()) / Double.parseDouble(split2[1].trim());
            String[] split3 = split[1].split("/", -1);
            double parseDouble2 = Double.parseDouble(split3[0].trim()) / Double.parseDouble(split3[1].trim());
            String[] split4 = split[2].split("/", -1);
            double parseDouble3 = parseDouble + (parseDouble2 / 60.0d) + ((Double.parseDouble(split4[0].trim()) / Double.parseDouble(split4[1].trim())) / 3600.0d);
            if (!str2.equals(LATITUDE_SOUTH)) {
                if (!str2.equals(LONGITUDE_WEST)) {
                    if (!str2.equals("N")) {
                        if (!str2.equals(LONGITUDE_EAST)) {
                            throw new IllegalArgumentException();
                        }
                    }
                    return parseDouble3;
                }
            }
            return -parseDouble3;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException unused) {
            throw new IllegalArgumentException();
        }
    }

    private String convertDecimalDegree(double d) {
        long j = (long) d;
        double d2 = d - ((double) j);
        long j2 = (long) (d2 * 60.0d);
        long round = Math.round((d2 - (((double) j2) / 60.0d)) * 3600.0d * 1.0E7d);
        StringBuilder sb = new StringBuilder();
        sb.append(j);
        sb.append("/1,");
        sb.append(j2);
        sb.append("/1,");
        sb.append(round);
        sb.append("/10000000");
        return sb.toString();
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        if (isRafFormat(bArr)) {
            return 9;
        }
        if (isOrfFormat(bArr)) {
            return 7;
        }
        return isRw2Format(bArr) ? 10 : 0;
    }

    private static boolean isJpegFormat(byte[] bArr) throws IOException {
        int i = 0;
        while (true) {
            byte[] bArr2 = JPEG_SIGNATURE;
            if (i >= bArr2.length) {
                return true;
            }
            if (bArr[i] != bArr2[i]) {
                return false;
            }
            i++;
        }
    }

    private boolean isRafFormat(byte[] bArr) throws IOException {
        byte[] bytes = RAF_SIGNATURE.getBytes(Charset.defaultCharset());
        for (int i = 0; i < bytes.length; i++) {
            if (bArr[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isOrfFormat(byte[] bArr) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == 20306 || readShort == 21330;
    }

    private boolean isRw2Format(byte[] bArr) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        short readShort = byteOrderedDataInputStream.readShort();
        byteOrderedDataInputStream.close();
        return readShort == 85;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0082 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getJpegAttributes(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r9, int r10, int r11) throws java.io.IOException {
        /*
            r8 = this;
            java.nio.ByteOrder r0 = java.nio.ByteOrder.BIG_ENDIAN
            r9.setByteOrder(r0)
            long r0 = (long) r10
            r9.seek(r0)
            byte r0 = r9.readByte()
            r1 = -1
            if (r0 != r1) goto L_0x015d
            r2 = 1
            int r10 = r10 + r2
            byte r3 = r9.readByte()
            r4 = -40
            if (r3 != r4) goto L_0x0140
            int r10 = r10 + r2
        L_0x001b:
            byte r0 = r9.readByte()
            if (r0 != r1) goto L_0x0123
            int r10 = r10 + r2
            byte r0 = r9.readByte()
            int r10 = r10 + r2
            r3 = -39
            if (r0 == r3) goto L_0x011d
            r3 = -38
            if (r0 != r3) goto L_0x0031
            goto L_0x011d
        L_0x0031:
            int r3 = r9.readUnsignedShort()
            int r3 = r3 + -2
            int r10 = r10 + 2
            if (r3 < 0) goto L_0x0115
            r4 = -31
            r5 = 0
            if (r0 == r4) goto L_0x00b8
            r4 = -2
            if (r0 == r4) goto L_0x008a
            switch(r0) {
                case -64: goto L_0x0051;
                case -63: goto L_0x0051;
                case -62: goto L_0x0051;
                case -61: goto L_0x0051;
                default: goto L_0x0046;
            }
        L_0x0046:
            switch(r0) {
                case -59: goto L_0x0051;
                case -58: goto L_0x0051;
                case -57: goto L_0x0051;
                default: goto L_0x0049;
            }
        L_0x0049:
            switch(r0) {
                case -55: goto L_0x0051;
                case -54: goto L_0x0051;
                case -53: goto L_0x0051;
                default: goto L_0x004c;
            }
        L_0x004c:
            switch(r0) {
                case -51: goto L_0x0051;
                case -50: goto L_0x0051;
                case -49: goto L_0x0051;
                default: goto L_0x004f;
            }
        L_0x004f:
            goto L_0x00e2
        L_0x0051:
            int r0 = r9.skipBytes(r2)
            if (r0 != r2) goto L_0x0082
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r0 = r8.mAttributes
            r0 = r0[r11]
            java.lang.String r4 = "ImageLength"
            int r5 = r9.readUnsignedShort()
            long r5 = (long) r5
            java.nio.ByteOrder r7 = r8.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r5 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r5, r7)
            r0.put(r4, r5)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r0 = r8.mAttributes
            r0 = r0[r11]
            java.lang.String r4 = "ImageWidth"
            int r5 = r9.readUnsignedShort()
            long r5 = (long) r5
            java.nio.ByteOrder r7 = r8.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r5 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r5, r7)
            r0.put(r4, r5)
            int r3 = r3 + -5
            goto L_0x00e2
        L_0x0082:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid SOFx"
            r9.<init>(r10)
            throw r9
        L_0x008a:
            byte[] r0 = new byte[r3]
            int r4 = r9.read(r0)
            if (r4 != r3) goto L_0x00b0
            java.lang.String r3 = "UserComment"
            java.lang.String r3 = r8.getAttribute(r3)
            if (r3 != 0) goto L_0x00ae
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r3 = r8.mAttributes
            r3 = r3[r2]
            java.lang.String r4 = "UserComment"
            java.lang.String r6 = new java.lang.String
            java.nio.charset.Charset r7 = ASCII
            r6.<init>(r0, r7)
            androidx.exifinterface.media.ExifInterface$ExifAttribute r0 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createString(r6)
            r3.put(r4, r0)
        L_0x00ae:
            r3 = 0
            goto L_0x00e2
        L_0x00b0:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid exif"
            r9.<init>(r10)
            throw r9
        L_0x00b8:
            r0 = 6
            if (r3 >= r0) goto L_0x00bc
            goto L_0x00e2
        L_0x00bc:
            byte[] r4 = new byte[r0]
            int r6 = r9.read(r4)
            if (r6 != r0) goto L_0x010d
            int r10 = r10 + 6
            int r3 = r3 + -6
            byte[] r0 = IDENTIFIER_EXIF_APP1
            boolean r0 = java.util.Arrays.equals(r4, r0)
            if (r0 != 0) goto L_0x00d1
            goto L_0x00e2
        L_0x00d1:
            if (r3 <= 0) goto L_0x0105
            r8.mExifOffset = r10
            byte[] r0 = new byte[r3]
            int r4 = r9.read(r0)
            if (r4 != r3) goto L_0x00fd
            int r10 = r10 + r3
            r8.readExifSegment(r0, r11)
            r3 = 0
        L_0x00e2:
            if (r3 < 0) goto L_0x00f5
            int r0 = r9.skipBytes(r3)
            if (r0 != r3) goto L_0x00ed
            int r10 = r10 + r3
            goto L_0x001b
        L_0x00ed:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid JPEG segment"
            r9.<init>(r10)
            throw r9
        L_0x00f5:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid length"
            r9.<init>(r10)
            throw r9
        L_0x00fd:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid exif"
            r9.<init>(r10)
            throw r9
        L_0x0105:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid exif"
            r9.<init>(r10)
            throw r9
        L_0x010d:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid exif"
            r9.<init>(r10)
            throw r9
        L_0x0115:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "Invalid length"
            r9.<init>(r10)
            throw r9
        L_0x011d:
            java.nio.ByteOrder r10 = r8.mExifByteOrder
            r9.setByteOrder(r10)
            return
        L_0x0123:
            java.io.IOException r9 = new java.io.IOException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Invalid marker:"
            r10.append(r11)
            r11 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r11 = java.lang.Integer.toHexString(r11)
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x0140:
            java.io.IOException r9 = new java.io.IOException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Invalid marker: "
            r10.append(r11)
            r11 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r11 = java.lang.Integer.toHexString(r11)
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x015d:
            java.io.IOException r9 = new java.io.IOException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Invalid marker: "
            r10.append(r11)
            r11 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r11 = java.lang.Integer.toHexString(r11)
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getJpegAttributes(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    private void getRawAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        parseTiffHeaders(byteOrderedDataInputStream, byteOrderedDataInputStream.available());
        readImageFileDirectory(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 0);
        updateImageSizeValues(byteOrderedDataInputStream, 5);
        updateImageSizeValues(byteOrderedDataInputStream, 4);
        validateImages(byteOrderedDataInputStream);
        if (this.mMimeType == 8) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
            if (exifAttribute != null) {
                ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
                byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
                byteOrderedDataInputStream2.seek(6);
                readImageFileDirectory(byteOrderedDataInputStream2, 9);
                ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[9].get(TAG_COLOR_SPACE);
                if (exifAttribute2 != null) {
                    this.mAttributes[1].put(TAG_COLOR_SPACE, exifAttribute2);
                }
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        byteOrderedDataInputStream.skipBytes(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(bArr2);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        getJpegAttributes(byteOrderedDataInputStream, i, 5);
        byteOrderedDataInputStream.seek((long) i2);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        int readInt = byteOrderedDataInputStream.readInt();
        for (int i3 = 0; i3 < readInt; i3++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                short readShort = byteOrderedDataInputStream.readShort();
                short readShort2 = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort((int) readShort, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort((int) readShort2, this.mExifByteOrder);
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, createUShort);
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, createUShort2);
                return;
            }
            byteOrderedDataInputStream.skipBytes(readUnsignedShort2);
        }
    }

    private void getOrfAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (exifAttribute != null) {
            ByteOrderedDataInputStream byteOrderedDataInputStream2 = new ByteOrderedDataInputStream(exifAttribute.bytes);
            byteOrderedDataInputStream2.setByteOrder(this.mExifByteOrder);
            byte[] bArr = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            byteOrderedDataInputStream2.readFully(bArr);
            byteOrderedDataInputStream2.seek(0);
            byte[] bArr2 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            byteOrderedDataInputStream2.readFully(bArr2);
            if (Arrays.equals(bArr, ORF_MAKER_NOTE_HEADER_1)) {
                byteOrderedDataInputStream2.seek(8);
            } else if (Arrays.equals(bArr2, ORF_MAKER_NOTE_HEADER_2)) {
                byteOrderedDataInputStream2.seek(12);
            }
            readImageFileDirectory(byteOrderedDataInputStream2, 6);
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
            ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
            if (!(exifAttribute2 == null || exifAttribute3 == null)) {
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, exifAttribute2);
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, exifAttribute3);
            }
            ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME);
            if (exifAttribute4 != null) {
                int[] iArr = (int[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 4) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid aspect frame values. frame=");
                    sb.append(Arrays.toString(iArr));
                    Log.w(str, sb.toString());
                } else if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
                    int i = (iArr[2] - iArr[0]) + 1;
                    int i2 = (iArr[3] - iArr[1]) + 1;
                    if (i < i2) {
                        int i3 = i + i2;
                        i2 = i3 - i2;
                        i = i3 - i2;
                    }
                    ExifAttribute createUShort = ExifAttribute.createUShort(i, this.mExifByteOrder);
                    ExifAttribute createUShort2 = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                    this.mAttributes[0].put(TAG_IMAGE_WIDTH, createUShort);
                    this.mAttributes[0].put(TAG_IMAGE_LENGTH, createUShort2);
                }
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        getRawAttributes(byteOrderedDataInputStream);
        if (((ExifAttribute) this.mAttributes[0].get(TAG_RW2_JPG_FROM_RAW)) != null) {
            getJpegAttributes(byteOrderedDataInputStream, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[0].get(TAG_RW2_ISO);
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[1].get(TAG_PHOTOGRAPHIC_SENSITIVITY);
        if (exifAttribute != null && exifAttribute2 == null) {
            this.mAttributes[1].put(TAG_PHOTOGRAPHIC_SENSITIVITY, exifAttribute);
        }
    }

    private void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        if (dataInputStream.readByte() == -1) {
            byteOrderedDataOutputStream.writeByte(-1);
            if (dataInputStream.readByte() == -40) {
                byteOrderedDataOutputStream.writeByte(-40);
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(-31);
                writeExifSegment(byteOrderedDataOutputStream, 6);
                byte[] bArr = new byte[4096];
                while (dataInputStream.readByte() == -1) {
                    byte readByte = dataInputStream.readByte();
                    if (readByte != -31) {
                        switch (readByte) {
                            case -39:
                            case -38:
                                byteOrderedDataOutputStream.writeByte(-1);
                                byteOrderedDataOutputStream.writeByte(readByte);
                                copy(dataInputStream, byteOrderedDataOutputStream);
                                return;
                            default:
                                byteOrderedDataOutputStream.writeByte(-1);
                                byteOrderedDataOutputStream.writeByte(readByte);
                                int readUnsignedShort = dataInputStream.readUnsignedShort();
                                byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort);
                                int i = readUnsignedShort - 2;
                                if (i >= 0) {
                                    while (i > 0) {
                                        int read = dataInputStream.read(bArr, 0, Math.min(i, bArr.length));
                                        if (read < 0) {
                                            break;
                                        } else {
                                            byteOrderedDataOutputStream.write(bArr, 0, read);
                                            i -= read;
                                        }
                                    }
                                    break;
                                } else {
                                    throw new IOException("Invalid length");
                                }
                        }
                    } else {
                        int readUnsignedShort2 = dataInputStream.readUnsignedShort() - 2;
                        if (readUnsignedShort2 >= 0) {
                            byte[] bArr2 = new byte[6];
                            if (readUnsignedShort2 >= 6) {
                                if (dataInputStream.read(bArr2) != 6) {
                                    throw new IOException("Invalid exif");
                                } else if (Arrays.equals(bArr2, IDENTIFIER_EXIF_APP1)) {
                                    int i2 = readUnsignedShort2 - 6;
                                    if (dataInputStream.skipBytes(i2) != i2) {
                                        throw new IOException("Invalid length");
                                    }
                                }
                            }
                            byteOrderedDataOutputStream.writeByte(-1);
                            byteOrderedDataOutputStream.writeByte(readByte);
                            byteOrderedDataOutputStream.writeUnsignedShort(readUnsignedShort2 + 2);
                            if (readUnsignedShort2 >= 6) {
                                readUnsignedShort2 -= 6;
                                byteOrderedDataOutputStream.write(bArr2);
                            }
                            while (readUnsignedShort2 > 0) {
                                int read2 = dataInputStream.read(bArr, 0, Math.min(readUnsignedShort2, bArr.length));
                                if (read2 >= 0) {
                                    byteOrderedDataOutputStream.write(bArr, 0, read2);
                                    readUnsignedShort2 -= read2;
                                }
                            }
                        } else {
                            throw new IOException("Invalid length");
                        }
                    }
                }
                throw new IOException("Invalid marker");
            }
            throw new IOException("Invalid marker");
        }
        throw new IOException("Invalid marker");
    }

    private void readExifSegment(byte[] bArr, int i) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bArr);
        parseTiffHeaders(byteOrderedDataInputStream, bArr.length);
        readImageFileDirectory(byteOrderedDataInputStream, i);
    }

    private void addDefaultValuesForCompatibility() {
        String attribute = getAttribute(TAG_DATETIME_ORIGINAL);
        if (attribute != null && getAttribute(TAG_DATETIME) == null) {
            this.mAttributes[0].put(TAG_DATETIME, ExifAttribute.createString(attribute));
        }
        if (getAttribute(TAG_IMAGE_WIDTH) == null) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute(TAG_IMAGE_LENGTH) == null) {
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute(TAG_ORIENTATION) == null) {
            this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute(TAG_LIGHT_SOURCE) == null) {
            this.mAttributes[1].put(TAG_LIGHT_SOURCE, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (readShort == 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid byte order: ");
        sb.append(Integer.toHexString(readShort));
        throw new IOException(sb.toString());
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        this.mExifByteOrder = readByteOrder(byteOrderedDataInputStream);
        byteOrderedDataInputStream.setByteOrder(this.mExifByteOrder);
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i2 = this.mMimeType;
        if (i2 == 7 || i2 == 10 || readUnsignedShort == 42) {
            int readInt = byteOrderedDataInputStream.readInt();
            if (readInt < 8 || readInt >= i) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid first Ifd offset: ");
                sb.append(readInt);
                throw new IOException(sb.toString());
            }
            int i3 = readInt - 8;
            if (i3 > 0 && byteOrderedDataInputStream.skipBytes(i3) != i3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Couldn't jump to first Ifd: ");
                sb2.append(i3);
                throw new IOException(sb2.toString());
            }
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Invalid start code: ");
        sb3.append(Integer.toHexString(readUnsignedShort));
        throw new IOException(sb3.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00f4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readImageFileDirectory(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r21, int r22) throws java.io.IOException {
        /*
            r20 = this;
            r0 = r20
            r1 = r21
            r2 = r22
            java.util.Set<java.lang.Integer> r3 = r0.mAttributesOffsets
            int r4 = r1.mPosition
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3.add(r4)
            int r3 = r1.mPosition
            int r3 = r3 + 2
            int r4 = r1.mLength
            if (r3 <= r4) goto L_0x001a
            return
        L_0x001a:
            short r3 = r21.readShort()
            int r4 = r1.mPosition
            int r5 = r3 * 12
            int r4 = r4 + r5
            int r5 = r1.mLength
            if (r4 > r5) goto L_0x0333
            if (r3 > 0) goto L_0x002b
            goto L_0x0333
        L_0x002b:
            r5 = 0
        L_0x002c:
            if (r5 >= r3) goto L_0x02bf
            int r9 = r21.readUnsignedShort()
            int r10 = r21.readUnsignedShort()
            int r11 = r21.readInt()
            int r12 = r21.peek()
            long r12 = (long) r12
            r14 = 4
            long r12 = r12 + r14
            java.util.HashMap<java.lang.Integer, androidx.exifinterface.media.ExifInterface$ExifTag>[] r16 = sExifTagMapsForReading
            r4 = r16[r2]
            java.lang.Integer r8 = java.lang.Integer.valueOf(r9)
            java.lang.Object r4 = r4.get(r8)
            androidx.exifinterface.media.ExifInterface$ExifTag r4 = (androidx.exifinterface.media.ExifInterface.ExifTag) r4
            r8 = 7
            if (r4 != 0) goto L_0x006b
            java.lang.String r14 = "ExifInterface"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r6 = "Skip the tag entry since tag number is not defined: "
            r15.append(r6)
            r15.append(r9)
            java.lang.String r6 = r15.toString()
            android.util.Log.w(r14, r6)
            goto L_0x00e8
        L_0x006b:
            if (r10 <= 0) goto L_0x00d2
            int[] r6 = IFD_FORMAT_BYTES_PER_FORMAT
            int r6 = r6.length
            if (r10 < r6) goto L_0x0073
            goto L_0x00d2
        L_0x0073:
            boolean r6 = r4.isFormatCompatible(r10)
            if (r6 != 0) goto L_0x009e
            java.lang.String r6 = "ExifInterface"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r14 = "Skip the tag entry since data format ("
            r7.append(r14)
            java.lang.String[] r14 = IFD_FORMAT_NAMES
            r14 = r14[r10]
            r7.append(r14)
            java.lang.String r14 = ") is unexpected for tag: "
            r7.append(r14)
            java.lang.String r14 = r4.name
            r7.append(r14)
            java.lang.String r7 = r7.toString()
            android.util.Log.w(r6, r7)
            goto L_0x00e8
        L_0x009e:
            if (r10 != r8) goto L_0x00a2
            int r10 = r4.primaryFormat
        L_0x00a2:
            long r6 = (long) r11
            int[] r14 = IFD_FORMAT_BYTES_PER_FORMAT
            r14 = r14[r10]
            long r14 = (long) r14
            long r6 = r6 * r14
            r14 = 0
            int r19 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r19 < 0) goto L_0x00ba
            r14 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r14 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r14 <= 0) goto L_0x00b8
            goto L_0x00ba
        L_0x00b8:
            r14 = 1
            goto L_0x00eb
        L_0x00ba:
            java.lang.String r14 = "ExifInterface"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r8 = "Skip the tag entry since the number of components is invalid: "
            r15.append(r8)
            r15.append(r11)
            java.lang.String r8 = r15.toString()
            android.util.Log.w(r14, r8)
            r14 = 0
            goto L_0x00eb
        L_0x00d2:
            java.lang.String r6 = "ExifInterface"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Skip the tag entry since data format is invalid: "
            r7.append(r8)
            r7.append(r10)
            java.lang.String r7 = r7.toString()
            android.util.Log.w(r6, r7)
        L_0x00e8:
            r6 = 0
            r14 = 0
        L_0x00eb:
            if (r14 != 0) goto L_0x00f4
            r1.seek(r12)
            r17 = r3
            goto L_0x02b6
        L_0x00f4:
            r14 = 4
            int r8 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r8 <= 0) goto L_0x01af
            int r8 = r21.readInt()
            int r14 = r0.mMimeType
            r15 = 7
            if (r14 != r15) goto L_0x016f
            java.lang.String r14 = "MakerNote"
            java.lang.String r15 = r4.name
            boolean r14 = r14.equals(r15)
            if (r14 == 0) goto L_0x0116
            r0.mOrfMakerNoteOffset = r8
            r17 = r3
            r18 = r10
            r15 = r11
            goto L_0x0184
        L_0x0116:
            r14 = 6
            if (r2 != r14) goto L_0x0169
            java.lang.String r15 = "ThumbnailImage"
            java.lang.String r14 = r4.name
            boolean r14 = r15.equals(r14)
            if (r14 == 0) goto L_0x0163
            r0.mOrfThumbnailOffset = r8
            r0.mOrfThumbnailLength = r11
            java.nio.ByteOrder r14 = r0.mExifByteOrder
            r15 = 6
            androidx.exifinterface.media.ExifInterface$ExifAttribute r14 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createUShort(r15, r14)
            int r15 = r0.mOrfThumbnailOffset
            r17 = r3
            long r2 = (long) r15
            java.nio.ByteOrder r15 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r2 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r2, r15)
            int r3 = r0.mOrfThumbnailLength
            r18 = r10
            r15 = r11
            long r10 = (long) r3
            java.nio.ByteOrder r3 = r0.mExifByteOrder
            androidx.exifinterface.media.ExifInterface$ExifAttribute r3 = androidx.exifinterface.media.ExifInterface.ExifAttribute.createULong(r10, r3)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r10 = r0.mAttributes
            r11 = 4
            r10 = r10[r11]
            java.lang.String r11 = "Compression"
            r10.put(r11, r14)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r10 = r0.mAttributes
            r11 = 4
            r10 = r10[r11]
            java.lang.String r14 = "JPEGInterchangeFormat"
            r10.put(r14, r2)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r2 = r2[r11]
            java.lang.String r10 = "JPEGInterchangeFormatLength"
            r2.put(r10, r3)
            goto L_0x0184
        L_0x0163:
            r17 = r3
            r18 = r10
            r15 = r11
            goto L_0x0184
        L_0x0169:
            r17 = r3
            r18 = r10
            r15 = r11
            goto L_0x0184
        L_0x016f:
            r17 = r3
            r18 = r10
            r15 = r11
            r2 = 10
            if (r14 != r2) goto L_0x0184
            java.lang.String r2 = "JpgFromRaw"
            java.lang.String r3 = r4.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0184
            r0.mRw2JpgFromRawOffset = r8
        L_0x0184:
            long r2 = (long) r8
            long r10 = r2 + r6
            int r14 = r1.mLength
            r16 = r15
            long r14 = (long) r14
            int r10 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r10 > 0) goto L_0x0194
            r1.seek(r2)
            goto L_0x01b5
        L_0x0194:
            java.lang.String r2 = "ExifInterface"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Skip the tag entry since data offset is invalid: "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.w(r2, r3)
            r1.seek(r12)
            goto L_0x02b6
        L_0x01af:
            r17 = r3
            r18 = r10
            r16 = r11
        L_0x01b5:
            java.util.HashMap<java.lang.Integer, java.lang.Integer> r2 = sExifPointerTagMap
            java.lang.Integer r3 = java.lang.Integer.valueOf(r9)
            java.lang.Object r2 = r2.get(r3)
            java.lang.Integer r2 = (java.lang.Integer) r2
            if (r2 == 0) goto L_0x024a
            r3 = -1
            switch(r18) {
                case 3: goto L_0x01e2;
                case 4: goto L_0x01db;
                case 8: goto L_0x01d3;
                case 9: goto L_0x01cb;
                case 13: goto L_0x01cb;
                default: goto L_0x01c8;
            }
        L_0x01c8:
            r6 = 0
            goto L_0x01e9
        L_0x01cb:
            int r3 = r21.readInt()
            long r3 = (long) r3
            r6 = 0
            goto L_0x01e9
        L_0x01d3:
            short r3 = r21.readShort()
            long r3 = (long) r3
            r6 = 0
            goto L_0x01e9
        L_0x01db:
            long r3 = r21.readUnsignedInt()
            r6 = 0
            goto L_0x01e9
        L_0x01e2:
            int r3 = r21.readUnsignedShort()
            long r3 = (long) r3
            r6 = 0
        L_0x01e9:
            int r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x0230
            int r6 = r1.mLength
            long r6 = (long) r6
            int r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r6 >= 0) goto L_0x0230
            java.util.Set<java.lang.Integer> r6 = r0.mAttributesOffsets
            int r7 = (int) r3
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            boolean r6 = r6.contains(r7)
            if (r6 != 0) goto L_0x020c
            r1.seek(r3)
            int r2 = r2.intValue()
            r0.readImageFileDirectory(r1, r2)
            goto L_0x0246
        L_0x020c:
            java.lang.String r6 = "ExifInterface"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Skip jump into the IFD since it has already been read: IfdType "
            r7.append(r8)
            r7.append(r2)
            java.lang.String r2 = " (at "
            r7.append(r2)
            r7.append(r3)
            java.lang.String r2 = ")"
            r7.append(r2)
            java.lang.String r2 = r7.toString()
            android.util.Log.w(r6, r2)
            goto L_0x0246
        L_0x0230:
            java.lang.String r2 = "ExifInterface"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Skip jump into the IFD since its offset is invalid: "
            r6.append(r7)
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            android.util.Log.w(r2, r3)
        L_0x0246:
            r1.seek(r12)
            goto L_0x02b6
        L_0x024a:
            int r2 = (int) r6
            byte[] r2 = new byte[r2]
            r1.readFully(r2)
            androidx.exifinterface.media.ExifInterface$ExifAttribute r3 = new androidx.exifinterface.media.ExifInterface$ExifAttribute
            r6 = r16
            r10 = r18
            r3.<init>(r10, r6, r2)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r2 = r2[r22]
            java.lang.String r6 = r4.name
            r2.put(r6, r3)
            java.lang.String r2 = "DNGVersion"
            java.lang.String r6 = r4.name
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L_0x026f
            r2 = 3
            r0.mMimeType = r2
        L_0x026f:
            java.lang.String r2 = "Make"
            java.lang.String r6 = r4.name
            boolean r2 = r2.equals(r6)
            if (r2 != 0) goto L_0x0283
            java.lang.String r2 = "Model"
            java.lang.String r6 = r4.name
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L_0x0291
        L_0x0283:
            java.nio.ByteOrder r2 = r0.mExifByteOrder
            java.lang.String r2 = r3.getStringValue(r2)
            java.lang.String r6 = "PENTAX"
            boolean r2 = r2.contains(r6)
            if (r2 != 0) goto L_0x02a6
        L_0x0291:
            java.lang.String r2 = "Compression"
            java.lang.String r4 = r4.name
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x02aa
            java.nio.ByteOrder r2 = r0.mExifByteOrder
            int r2 = r3.getIntValue(r2)
            r3 = 65535(0xffff, float:9.1834E-41)
            if (r2 != r3) goto L_0x02aa
        L_0x02a6:
            r2 = 8
            r0.mMimeType = r2
        L_0x02aa:
            int r2 = r21.peek()
            long r2 = (long) r2
            int r2 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
            if (r2 == 0) goto L_0x02b6
            r1.seek(r12)
        L_0x02b6:
            int r5 = r5 + 1
            short r5 = (short) r5
            r2 = r22
            r3 = r17
            goto L_0x002c
        L_0x02bf:
            int r2 = r21.peek()
            r3 = 4
            int r2 = r2 + r3
            int r3 = r1.mLength
            if (r2 > r3) goto L_0x0332
            int r2 = r21.readInt()
            long r3 = (long) r2
            r5 = 0
            int r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x031c
            int r5 = r1.mLength
            if (r2 >= r5) goto L_0x031c
            java.util.Set<java.lang.Integer> r5 = r0.mAttributesOffsets
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)
            boolean r5 = r5.contains(r6)
            if (r5 != 0) goto L_0x0305
            r1.seek(r3)
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r3 = 4
            r2 = r2[r3]
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x02f6
            r0.readImageFileDirectory(r1, r3)
            goto L_0x0332
        L_0x02f6:
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r0.mAttributes
            r3 = 5
            r2 = r2[r3]
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x0332
            r0.readImageFileDirectory(r1, r3)
            goto L_0x0332
        L_0x0305:
            java.lang.String r1 = "ExifInterface"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Stop reading file since re-reading an IFD may cause an infinite loop: "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            android.util.Log.w(r1, r2)
            goto L_0x0332
        L_0x031c:
            java.lang.String r1 = "ExifInterface"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Stop reading file since a wrong offset may cause an infinite loop: "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            android.util.Log.w(r1, r2)
        L_0x0332:
            return
        L_0x0333:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.readImageFileDirectory(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int):void");
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_WIDTH);
        if (((ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_LENGTH)) == null || exifAttribute == null) {
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get(TAG_JPEG_INTERCHANGE_FORMAT);
            if (exifAttribute2 != null) {
                getJpegAttributes(byteOrderedDataInputStream, exifAttribute2.getIntValue(this.mExifByteOrder), i);
            }
        }
    }

    private void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        HashMap<String, ExifAttribute> hashMap = this.mAttributes[4];
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_COMPRESSION);
        if (exifAttribute != null) {
            this.mThumbnailCompression = exifAttribute.getIntValue(this.mExifByteOrder);
            int i = this.mThumbnailCompression;
            if (i != 1) {
                switch (i) {
                    case 6:
                        handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                        return;
                    case 7:
                        break;
                    default:
                        return;
                }
            }
            if (isSupportedDataType(hashMap)) {
                handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                return;
            }
            return;
        }
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_JPEG_INTERCHANGE_FORMAT);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int min = Math.min(exifAttribute2.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.available() - intValue);
            int i = this.mMimeType;
            if (i == 4 || i == 9 || i == 10) {
                intValue += this.mExifOffset;
            } else if (i == 7) {
                intValue += this.mOrfMakerNoteOffset;
            }
            if (intValue > 0 && min > 0) {
                this.mHasThumbnail = true;
                this.mThumbnailOffset = intValue;
                this.mThumbnailLength = min;
                if (this.mFilename == null && this.mAssetInputStream == null) {
                    byte[] bArr = new byte[min];
                    byteOrderedDataInputStream.seek((long) intValue);
                    byteOrderedDataInputStream.readFully(bArr);
                    this.mThumbnailBytes = bArr;
                }
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_STRIP_OFFSETS);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_STRIP_BYTE_COUNTS);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            long[] convertToLongArray = convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            long[] convertToLongArray2 = convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            if (convertToLongArray == null) {
                Log.w(TAG, "stripOffsets should not be null.");
            } else if (convertToLongArray2 == null) {
                Log.w(TAG, "stripByteCounts should not be null.");
            } else {
                long j = 0;
                for (long j2 : convertToLongArray2) {
                    j += j2;
                }
                byte[] bArr = new byte[((int) j)];
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < convertToLongArray.length; i3++) {
                    int i4 = (int) convertToLongArray2[i3];
                    int i5 = ((int) convertToLongArray[i3]) - i;
                    if (i5 < 0) {
                        Log.d(TAG, "Invalid strip offset value");
                    }
                    byteOrderedDataInputStream.seek((long) i5);
                    int i6 = i + i5;
                    byte[] bArr2 = new byte[i4];
                    byteOrderedDataInputStream.read(bArr2);
                    i = i6 + i4;
                    System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
                    i2 += bArr2.length;
                }
                this.mHasThumbnail = true;
                this.mThumbnailBytes = bArr;
                this.mThumbnailLength = bArr.length;
            }
        }
    }

    private boolean isSupportedDataType(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_BITS_PER_SAMPLE);
        if (exifAttribute != null) {
            int[] iArr = (int[]) exifAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, iArr)) {
                return true;
            }
            if (this.mMimeType == 3) {
                ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_PHOTOMETRIC_INTERPRETATION);
                if (exifAttribute2 != null) {
                    int intValue = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if ((intValue == 1 && Arrays.equals(iArr, BITS_PER_SAMPLE_GREYSCALE_2)) || (intValue == 6 && Arrays.equals(iArr, BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isThumbnail(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get(TAG_IMAGE_LENGTH);
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get(TAG_IMAGE_WIDTH);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (intValue <= 512 && intValue2 <= 512) {
                return true;
            }
        }
        return false;
    }

    private void validateImages(InputStream inputStream) throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_X_DIMENSION);
        ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[1].get(TAG_PIXEL_Y_DIMENSION);
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, exifAttribute);
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, exifAttribute2);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap<>();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d(TAG, "No image meets the size requirements of a thumbnail image.");
        }
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream byteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i].get(TAG_DEFAULT_CROP_SIZE);
        ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_TOP_BORDER);
        ExifAttribute exifAttribute5 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_LEFT_BORDER);
        ExifAttribute exifAttribute6 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_BOTTOM_BORDER);
        ExifAttribute exifAttribute7 = (ExifAttribute) this.mAttributes[i].get(TAG_RW2_SENSOR_RIGHT_BORDER);
        if (exifAttribute3 != null) {
            if (exifAttribute3.format == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid crop size values. cropSize=");
                    sb.append(Arrays.toString(rationalArr));
                    Log.w(str, sb.toString());
                    return;
                }
                exifAttribute2 = ExifAttribute.createURational(rationalArr[0], this.mExifByteOrder);
                exifAttribute = ExifAttribute.createURational(rationalArr[1], this.mExifByteOrder);
            } else {
                int[] iArr = (int[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid crop size values. cropSize=");
                    sb2.append(Arrays.toString(iArr));
                    Log.w(str2, sb2.toString());
                    return;
                }
                exifAttribute2 = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                exifAttribute = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
            }
            this.mAttributes[i].put(TAG_IMAGE_WIDTH, exifAttribute2);
            this.mAttributes[i].put(TAG_IMAGE_LENGTH, exifAttribute);
        } else if (exifAttribute4 == null || exifAttribute5 == null || exifAttribute6 == null || exifAttribute7 == null) {
            retrieveJpegImageSize(byteOrderedDataInputStream, i);
        } else {
            int intValue = exifAttribute4.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute6.getIntValue(this.mExifByteOrder);
            int intValue3 = exifAttribute7.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute5.getIntValue(this.mExifByteOrder);
            if (intValue2 > intValue && intValue3 > intValue4) {
                int i2 = intValue3 - intValue4;
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue2 - intValue, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                this.mAttributes[i].put(TAG_IMAGE_LENGTH, createUShort);
                this.mAttributes[i].put(TAG_IMAGE_WIDTH, createUShort2);
            }
        }
    }

    private int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream, int i) throws IOException {
        ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = byteOrderedDataOutputStream;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        int[] iArr = new int[exifTagArr.length];
        int[] iArr2 = new int[exifTagArr.length];
        for (ExifTag exifTag : EXIF_POINTER_TAGS) {
            removeAttribute(exifTag.name);
        }
        removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
        removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        for (int i2 = 0; i2 < EXIF_TAGS.length; i2++) {
            for (Object obj : this.mAttributes[i2].entrySet().toArray()) {
                Entry entry = (Entry) obj;
                if (entry.getValue() == null) {
                    this.mAttributes[i2].remove(entry.getKey());
                }
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0, this.mExifByteOrder));
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong((long) this.mThumbnailLength, this.mExifByteOrder));
        }
        for (int i3 = 0; i3 < EXIF_TAGS.length; i3++) {
            int i4 = 0;
            for (Entry value : this.mAttributes[i3].entrySet()) {
                int size = ((ExifAttribute) value.getValue()).size();
                if (size > 4) {
                    i4 += size;
                }
            }
            iArr2[i3] = iArr2[i3] + i4;
        }
        int i5 = 8;
        for (int i6 = 0; i6 < EXIF_TAGS.length; i6++) {
            if (!this.mAttributes[i6].isEmpty()) {
                iArr[i6] = i5;
                i5 += (this.mAttributes[i6].size() * 12) + 2 + 4 + iArr2[i6];
            }
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong((long) i5, this.mExifByteOrder));
            this.mThumbnailOffset = i + i5;
            i5 += this.mThumbnailLength;
        }
        int i7 = i5 + 8;
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong((long) iArr[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong((long) iArr[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong((long) iArr[3], this.mExifByteOrder));
        }
        byteOrderedDataOutputStream2.writeUnsignedShort(i7);
        byteOrderedDataOutputStream2.write(IDENTIFIER_EXIF_APP1);
        byteOrderedDataOutputStream2.writeShort(this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? BYTE_ALIGN_MM : BYTE_ALIGN_II);
        byteOrderedDataOutputStream2.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream2.writeUnsignedShort(42);
        byteOrderedDataOutputStream2.writeUnsignedInt(8);
        for (int i8 = 0; i8 < EXIF_TAGS.length; i8++) {
            if (!this.mAttributes[i8].isEmpty()) {
                byteOrderedDataOutputStream2.writeUnsignedShort(this.mAttributes[i8].size());
                int size2 = iArr[i8] + 2 + (this.mAttributes[i8].size() * 12) + 4;
                for (Entry entry2 : this.mAttributes[i8].entrySet()) {
                    int i9 = ((ExifTag) sExifTagMapsForWriting[i8].get(entry2.getKey())).number;
                    ExifAttribute exifAttribute = (ExifAttribute) entry2.getValue();
                    int size3 = exifAttribute.size();
                    byteOrderedDataOutputStream2.writeUnsignedShort(i9);
                    byteOrderedDataOutputStream2.writeUnsignedShort(exifAttribute.format);
                    byteOrderedDataOutputStream2.writeInt(exifAttribute.numberOfComponents);
                    if (size3 > 4) {
                        byteOrderedDataOutputStream2.writeUnsignedInt((long) size2);
                        size2 += size3;
                    } else {
                        byteOrderedDataOutputStream2.write(exifAttribute.bytes);
                        if (size3 < 4) {
                            while (size3 < 4) {
                                byteOrderedDataOutputStream2.writeByte(0);
                                size3++;
                            }
                        }
                    }
                }
                if (i8 != 0 || this.mAttributes[4].isEmpty()) {
                    byteOrderedDataOutputStream2.writeUnsignedInt(0);
                } else {
                    byteOrderedDataOutputStream2.writeUnsignedInt((long) iArr[4]);
                }
                for (Entry value2 : this.mAttributes[i8].entrySet()) {
                    ExifAttribute exifAttribute2 = (ExifAttribute) value2.getValue();
                    if (exifAttribute2.bytes.length > 4) {
                        byteOrderedDataOutputStream2.write(exifAttribute2.bytes, 0, exifAttribute2.bytes.length);
                    }
                }
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream2.write(getThumbnailBytes());
        }
        byteOrderedDataOutputStream2.setByteOrder(ByteOrder.BIG_ENDIAN);
        return i7;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        java.lang.Double.parseDouble(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0185, code lost:
        return new android.util.Pair<>(java.lang.Integer.valueOf(12), java.lang.Integer.valueOf(-1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0193, code lost:
        return new android.util.Pair<>(java.lang.Integer.valueOf(2), java.lang.Integer.valueOf(-1));
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:68:0x0173 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.util.Pair<java.lang.Integer, java.lang.Integer> guessDataFormat(java.lang.String r10) {
        /*
            java.lang.String r0 = ","
            boolean r0 = r10.contains(r0)
            r1 = 0
            r2 = 1
            r3 = 2
            r4 = -1
            if (r0 == 0) goto L_0x00b1
            java.lang.String r0 = ","
            java.lang.String[] r10 = r10.split(r0, r4)
            r0 = r10[r1]
            android.util.Pair r0 = guessDataFormat(r0)
            java.lang.Object r1 = r0.first
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r1 != r3) goto L_0x0023
            return r0
        L_0x0023:
            int r1 = r10.length
            if (r2 >= r1) goto L_0x00b0
            r1 = r10[r2]
            android.util.Pair r1 = guessDataFormat(r1)
            java.lang.Object r5 = r1.first
            java.lang.Integer r5 = (java.lang.Integer) r5
            java.lang.Object r6 = r0.first
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x0047
            java.lang.Object r5 = r1.second
            java.lang.Integer r5 = (java.lang.Integer) r5
            java.lang.Object r6 = r0.first
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x0045
            goto L_0x0047
        L_0x0045:
            r5 = -1
            goto L_0x004f
        L_0x0047:
            java.lang.Object r5 = r0.first
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
        L_0x004f:
            java.lang.Object r6 = r0.second
            java.lang.Integer r6 = (java.lang.Integer) r6
            int r6 = r6.intValue()
            if (r6 == r4) goto L_0x007a
            java.lang.Object r6 = r1.first
            java.lang.Integer r6 = (java.lang.Integer) r6
            java.lang.Object r7 = r0.second
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0071
            java.lang.Object r1 = r1.second
            java.lang.Integer r1 = (java.lang.Integer) r1
            java.lang.Object r6 = r0.second
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x007a
        L_0x0071:
            java.lang.Object r1 = r0.second
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            goto L_0x007b
        L_0x007a:
            r1 = -1
        L_0x007b:
            if (r5 != r4) goto L_0x008d
            if (r1 != r4) goto L_0x008d
            android.util.Pair r10 = new android.util.Pair
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            r10.<init>(r0, r1)
            return r10
        L_0x008d:
            if (r5 != r4) goto L_0x009d
            android.util.Pair r0 = new android.util.Pair
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)
            r0.<init>(r1, r5)
            goto L_0x00ac
        L_0x009d:
            if (r1 != r4) goto L_0x00ac
            android.util.Pair r0 = new android.util.Pair
            java.lang.Integer r1 = java.lang.Integer.valueOf(r5)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)
            r0.<init>(r1, r5)
        L_0x00ac:
            int r2 = r2 + 1
            goto L_0x0023
        L_0x00b0:
            return r0
        L_0x00b1:
            java.lang.String r0 = "/"
            boolean r0 = r10.contains(r0)
            r5 = 0
            if (r0 == 0) goto L_0x0122
            java.lang.String r0 = "/"
            java.lang.String[] r10 = r10.split(r0, r4)
            int r0 = r10.length
            if (r0 != r3) goto L_0x0114
            r0 = r10[r1]     // Catch:{ NumberFormatException -> 0x0114 }
            double r0 = java.lang.Double.parseDouble(r0)     // Catch:{ NumberFormatException -> 0x0114 }
            long r0 = (long) r0     // Catch:{ NumberFormatException -> 0x0114 }
            r10 = r10[r2]     // Catch:{ NumberFormatException -> 0x0114 }
            double r7 = java.lang.Double.parseDouble(r10)     // Catch:{ NumberFormatException -> 0x0114 }
            long r7 = (long) r7     // Catch:{ NumberFormatException -> 0x0114 }
            r10 = 10
            int r2 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r2 < 0) goto L_0x0106
            int r2 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r2 >= 0) goto L_0x00dd
            goto L_0x0106
        L_0x00dd:
            r2 = 5
            r5 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 > 0) goto L_0x00f8
            int r0 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r0 <= 0) goto L_0x00ea
            goto L_0x00f8
        L_0x00ea:
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)     // Catch:{ NumberFormatException -> 0x0114 }
            r0.<init>(r10, r1)     // Catch:{ NumberFormatException -> 0x0114 }
            return r0
        L_0x00f8:
            android.util.Pair r10 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0114 }
            r10.<init>(r0, r1)     // Catch:{ NumberFormatException -> 0x0114 }
            return r10
        L_0x0106:
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ NumberFormatException -> 0x0114 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0114 }
            r0.<init>(r10, r1)     // Catch:{ NumberFormatException -> 0x0114 }
            return r0
        L_0x0114:
            android.util.Pair r10 = new android.util.Pair
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            r10.<init>(r0, r1)
            return r10
        L_0x0122:
            long r0 = java.lang.Long.parseLong(r10)     // Catch:{ NumberFormatException -> 0x0173 }
            java.lang.Long r0 = java.lang.Long.valueOf(r0)     // Catch:{ NumberFormatException -> 0x0173 }
            long r1 = r0.longValue()     // Catch:{ NumberFormatException -> 0x0173 }
            r7 = 4
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 < 0) goto L_0x014d
            long r1 = r0.longValue()     // Catch:{ NumberFormatException -> 0x0173 }
            r8 = 65535(0xffff, double:3.23786E-319)
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 > 0) goto L_0x014d
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0173 }
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ NumberFormatException -> 0x0173 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r7)     // Catch:{ NumberFormatException -> 0x0173 }
            r0.<init>(r1, r2)     // Catch:{ NumberFormatException -> 0x0173 }
            return r0
        L_0x014d:
            long r0 = r0.longValue()     // Catch:{ NumberFormatException -> 0x0173 }
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 >= 0) goto L_0x0165
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0173 }
            r1 = 9
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ NumberFormatException -> 0x0173 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0173 }
            r0.<init>(r1, r2)     // Catch:{ NumberFormatException -> 0x0173 }
            return r0
        L_0x0165:
            android.util.Pair r0 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0173 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r7)     // Catch:{ NumberFormatException -> 0x0173 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0173 }
            r0.<init>(r1, r2)     // Catch:{ NumberFormatException -> 0x0173 }
            return r0
        L_0x0173:
            java.lang.Double.parseDouble(r10)     // Catch:{ NumberFormatException -> 0x0186 }
            android.util.Pair r10 = new android.util.Pair     // Catch:{ NumberFormatException -> 0x0186 }
            r0 = 12
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ NumberFormatException -> 0x0186 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ NumberFormatException -> 0x0186 }
            r10.<init>(r0, r1)     // Catch:{ NumberFormatException -> 0x0186 }
            return r10
        L_0x0186:
            android.util.Pair r10 = new android.util.Pair
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            r10.<init>(r0, r1)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.guessDataFormat(java.lang.String):android.util.Pair");
    }

    private void swapBasedOnImageSize(int i, int i2) throws IOException {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            ExifAttribute exifAttribute = (ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_LENGTH);
            ExifAttribute exifAttribute2 = (ExifAttribute) this.mAttributes[i].get(TAG_IMAGE_WIDTH);
            ExifAttribute exifAttribute3 = (ExifAttribute) this.mAttributes[i2].get(TAG_IMAGE_LENGTH);
            ExifAttribute exifAttribute4 = (ExifAttribute) this.mAttributes[i2].get(TAG_IMAGE_WIDTH);
            if (!(exifAttribute == null || exifAttribute2 == null || exifAttribute3 == null || exifAttribute4 == null)) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
                    HashMap<String, ExifAttribute> hashMap = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap;
                }
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    private static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return i;
            }
            i += read;
            outputStream.write(bArr, 0, read);
        }
    }

    private static long[] convertToLongArray(Object obj) {
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            long[] jArr = new long[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                jArr[i] = (long) iArr[i];
            }
            return jArr;
        } else if (obj instanceof long[]) {
            return (long[]) obj;
        } else {
            return null;
        }
    }
}
