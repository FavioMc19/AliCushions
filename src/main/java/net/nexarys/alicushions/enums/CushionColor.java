package net.nexarys.alicushions.enums;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.DyeColor;

@Getter
public enum CushionColor {
    WHITE(""
            ,""
            , ""
            , ""),
    ORANGE(""
            ,""
            , ""
            , ""),
    MAGENTA(""
            ,""
            , ""
            , ""),
    LIGHT_BLUE(""
            ,""
            , ""
            , ""),
    YELLOW("d84dcd0531a9513f896e5343833c0fbc91900c6d0aaf60c279972cd413686a"
            ,"b975295137548f8281b4d9f824fbd605be738526170650d408bf04d0137aa334"
            , "3ab09f13b4063ee4120184f4b05f2b59513bf499e0823265e0dc60510d869c32"
            , "e3994770db6744df30a1b2a9242dde1c47eb90b474fc34b2435cf13b9586cbb"),
    LIME(""
            ,""
            , ""
            , ""),
    PINK(""
            ,""
            , ""
            , ""),
    GRAY(""
            ,""
            , ""
            , ""),
    LIGHT_GRAY(""
            ,""
            , ""
            , ""),
    CYAN(""
            ,""
            , ""
            , ""),
    PURPLE(""
            ,""
            , ""
            , ""),
    BLUE(""
            ,""
            , ""
            , ""),
    BROWN(""
            ,""
            , ""
            , ""),
    GREEN(""
            ,""
            , ""
            , ""),
    RED(""
            ,""
            , ""
            , ""),
    BLACK(""
            ,""
            , ""
            , "");

    private final String head1;
    private final String head2;
    private final String head3;
    private final String head4;

    CushionColor(String head1, String head2, String head3, String head4) {
        this.head1 = head1;
        this.head2 = head2;
        this.head3 = head3;
        this.head4 = head4;
    }

    public DyeColor toDyeColor() {
        return DyeColor.valueOf(name());
    }

    public String getHeadById(int i) {
        return switch (i) {
          case  1 -> head1;
          case 0 -> head2;
          case 3 -> head3;
          case 2 -> head4;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
