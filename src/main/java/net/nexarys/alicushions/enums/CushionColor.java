package net.nexarys.alicushions.enums;

import lombok.Getter;
import org.bukkit.DyeColor;

@Getter
public enum CushionColor {
    WHITE("5f211885d826bd6921018b7fbc785884307f34d9600071e60a4d63ca88482b39"
            ,"d7dff9aec11bf7f3b6d10fa7f5ebb1fdfab8d87f552907ce1acfa79d12dc1138"
            , "2b267bf87d98d915d0f5ac892df72a204207fff0237e30f72ec0bb68bd5781a7"
            , "345d979d1715f57e100fbdd397bb1e31b108ed695f82115b76c7e14867b96f1b"),
    ORANGE("84e1ed3896e312f0f2115996b10bad4918dc0801b0251e8075dfde7f518421ab"
            ,"f2b5ab227ba49660707d303f5395d492f1a108e5e68e923ac54002103874d825"
            , "785d039031bb41389430bf919376b9348188961d3cbd0ad623a0b34500e88326"
            , "fd50b5951d2ec0a601a51100f74ade24f316457b2b6d7524a83dcc47b583700f"),
    MAGENTA("742992e6c3b8eef97ca281b1981092c2407bd2a36b35c89411b6ea8e41e842bc"
            ,"c3b85bc50c1812ce72aba66a238688d8c6725ebb46f5652f993f40ad79486727"
            , "ba89488456aaa07730f496ac06fea2629f4feeed474c00e72a6c0a02dd5f0135"
            , "2d5d677ec7e1c61cf1ed9773cb23802b1a04302841530f5e005f996c475df57a"),
    LIGHT_BLUE("33dd88663bf6e344c57a0c1af81fd04315b071746113225d681a2607680aa52b"
            ,"92dc4d50e00b9726d1f256a928a10030e0c74010f246dd13164caa9ea6a97b69"
            , "7197e28e7c171c792da5177c9bd30b1ad3ef9bd3c6d73de53e4604feaa044ae3"
            , "5632f605707b7fa08222d68fcff8966334f3c80fb8058818cbd39c3e83f59d2d"),
    YELLOW("d84dcd0531a9513f896e5343833c0fbc91900c6d0aaf60c279972cd413686a"
            ,"bbb87de65b4f3eb8b15d2b22d6e13899e272f21bde56d0405723d3c655782820"
            , "3ab09f13b4063ee4120184f4b05f2b59513bf499e0823265e0dc60510d869c32"
            , "1453dce1cbebbebc972d4c88b7cbfc60cae6961d21795b36b306ff12b8187548"),
    LIME("69977a81dae94a79fb537fc61cef18995b37c6c119a1fbc01ff077b5a4d7f437"
            ,"d3737f057d29bf72b8e4bc29e7dbeae119703b5daa2e7a0747553d5576e8457a"
            , "2a1c105203aabb7271eec1f487cd7002e103283a610a8e91eb188ca42b97b8a8"
            , "d5b37380fc8d0b51ac38cae846005d5fcc30f1fb6fee0626c746f67ba03a992f"),
    PINK("b0e4fa32015885a3770c7a2a57f502b09bc38012d25e5a9882f1af55821f8613"
            ,"ae2f7ba1b43c17e9cf39e75c2f9d390f318569d5b615e7939de1d2f70fce0dea"
            , "2bd66f9475a90d26d0c57185de64e4c646e177476c5f6d336ad418f8629016f5"
            , "ffc0db8482e9b46fc45bb18de84790740bafd5da8a1f0f2bb79f89a77357f24a"),
    GRAY("6eca017cc68b949e0fdb4371b385d65d873b39e2589d6794eb13a088c46a4d60"
            ,"22e730d9bbcc2aad4af3158ba789fda7ab1cfcfafadedc33457d2c8077ac4869"
            , "b2a1c801e33507afe4bc014ec438fd05d3e336467b5bd3e0508aa9f8b234798"
            , "cacbb41088e558edd54a4e7668d933f46fa64a99be56e893883b080c58d23451"),
    LIGHT_GRAY("4cd21c1ce256cb12e2b9a21af5beffa1813244733725d40ccb553a45dd3571e"
            ,"6f9b031474250f1ca083a49927d1fa529d2ff585a04d6466aaaa715b0b0fd9c8"
            , "ae2952c03d99d9da63c5f6a0fdacec0e4baba31f6152d194d3e38b40913eeca2"
            , "6a0318d49272b9498e09bd345d8a2d5f431cfbafd8080a3a5ab98d22b76360b1"),
    CYAN("9fff3a0d8ebbab1ea6614d2ad45feb3598511d03a0fbec38d49c0afd9b9404d"
            ,"144dc0b9d9b2382add64eca2328704c5b0aa13d9f00900823e582b53d0add479"
            , "f37d0bcb00d0383ada62d79b1390db61d50763b41238b8a0a2863a43e057e0d1"
            , "7a6e5ac5eca00d8e2128c79fdf31ad2789be69dde503846fcc2c1ba6a3cbcb1a"),
    PURPLE("483aa607e6d546ae97c497a08ef7fe37fe7ca1e8c7a1c567cef07dbdd807857b"
            ,"d33f5866f0c94c1a273c15da156f9bd426f95861e435bc5591eab767d2bb86cd"
            , "40a0097e3c58c371a1355007f93f477f9faf686df225772c51b6e9a6face5fe9"
            , "8a03c231e80d458f56503431814ddea96b5201944083401f60a134d4914a8d1c"),
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
    RED("f779130ef38117190be79dc439698727b4282e644970e87966350432c688ebc1"
            ,"a3488e4ca411b70986cdba3317a842ee98dc39a422aa1b1a9f36451292c7fde"
            , "fb792dfcd9e539e3eef3b5e5bd08626ad0b5f43aa5af6a36bbef49736935cab3"
            , "d0c41da1200e56812114c44833f63755ab4ef78bd323878070ce8617532ab0ca"),
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
