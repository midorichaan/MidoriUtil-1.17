package midorichan.utils;

import midorichan.Main;
import midorichan.enums.MessageType;

public class Messages {

    private static Main plugin = Main.getInstance();

    private final static String occupied = "occupied-seat";
    private final static String tooFar = "too-far-from-seat";
    private final static String noPerms = "no-permission";
    private final static String tooMany = "too-many-items";
    private final static String noSigns = "no-sign-at-ends";

    private final static String defaultOccupied = Main.getPrefix() + "他の人が座っています。";
    private final static String defaultTooFar = Main.getPrefix() + "遠すぎます。";
    private final static String defaultNoPerms = Main.getPrefix() + "権限がありません。";
    private final static String defaultTooMany = Main.getPrefix() + "アイテムを手に持っているため、座れません。";
    private final static String defaultNoSigns = Main.getPrefix() + "椅子の両端に看板がないため、座れません。";

    public static String getDefault(MessageType type) {
        switch (type) {
            default:
                return null;
            case OCCUPIED:
                return Messages.defaultOccupied;
            case TOOFAR:
                return Messages.defaultTooFar;
            case NOPERMS:
                return Messages.defaultNoPerms;
            case TOOMANYITEMS:
                return Messages.defaultTooMany;
            case NOSIGNS:
                return Messages.defaultNoSigns;
        }
    }

    public static String getMessage(MessageType type) {
        switch (type) {
            default:
                return null;
            case OCCUPIED:
                return Messages.occupied;
            case TOOFAR:
                return Messages.tooFar;
            case NOPERMS:
                return Messages.noPerms;
            case TOOMANYITEMS:
                return Messages.tooMany;
            case NOSIGNS:
                return Messages.noSigns;
        }
    }
}