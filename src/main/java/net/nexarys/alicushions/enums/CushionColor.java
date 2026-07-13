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
    GRAY("6eca017cc68b949e0fdb4371b385d65d873b39e2589d6794eb13a088c46a4d60"
            ,"22e730d9bbcc2aad4af3158ba789fda7ab1cfcfafadedc33457d2c8077ac4869"
            , "b2a1c801e33507afe4bc014ec438fd05d3e336467b5bd3e0508aa9f8b234798"
            , "cacbb41088e558edd54a4e7668d933f46fa64a99be56e893883b080c58d23451"),
    LIGHT_GRAY(""
            ,""
            , ""
            , ""),
    CYAN("9fff3a0d8ebbab1ea6614d2ad45feb3598511d03a0fbec38d49c0afd9b9404d"
            ,"144dc0b9d9b2382add64eca2328704c5b0aa13d9f00900823e582b53d0add479"
            , "f37d0bcb00d0383ada62d79b1390db61d50763b41238b8a0a2863a43e057e0d1"
            , "7a6e5ac5eca00d8e2128c79fdf31ad2789be69dde503846fcc2c1ba6a3cbcb1a"),
    PURPLE(""
            ,""
            , ""
            , ""),
    BLUE("e8a0440fac1bb0046b3c308c9dbace6e4bdb14da54fac49acf7019f821ce689e"
            ,"934468b17d058a87525607b88e9fdad8ca62a0415b0cf2aa24abc60d4912a0e6"
            , "86da07a52b48d09ceb88b0d532ef85343076dea6adbec296142a01adae7f90f5"
            , "a260b5d26acb4594711d9b4b0fecfec7d2085521fc61d67b8d86e6b678b9fe46"),
    BROWN("a98f7a799ab1637ba2cb48891f6b997b28bbb421867b038c3fb7fa398f85c4ad"
            ,"2789dbfe88ef3225f66c60fb8a255a7ebb3f6ebdc12a516bf246902c23cf457b"
            , "40182c0764839c3712de95e8b0be9c7f7bd470da13b9e562fd91807dbab14126"
            , "8381023940284fdfe33810c6d7be2750f0a4ec457434d44212bcff7f06c169"),
    GREEN("7850386d5d084e823ca41154e4d83ead93b728f87dc95906405c7fc535ec1340"
            ,"3e5f251738cef67e8dba3ba70d1753085a79c2f1070a8bb1a83682cd9f6b6b43"
            , "63ea23521c394a68678eee8fd90bc4bd1882b942ba9b3afae0ac7d49ea5b50db"
            , "c859610847758ccde4b2d91138cef94792b7b6edb3e216fa5d315ab5aae01224"),
    RED(""
            ,""
            , ""
            , ""),
    BLACK("44df42431e74f0a455e17a4d6ada90fe9fa5c0af0dabadbe7c669510f785006"
            ,"b80351f4b512c8961f48cbd2a67fb16fd697876e1a38f5a0760418e188eec971"
            , "f214f24f1121c27694b7458a023cd32de76311cf1857ed5e0b9f89642ca30325"
            , "66e0d5729de35d98d30f694e7fb270408dc273c8747ab474ba9b051edca60601");

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
