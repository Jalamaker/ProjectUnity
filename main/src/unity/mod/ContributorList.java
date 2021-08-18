package unity.mod;

import arc.struct.*;

/** @deprecated There's got to be a better way to do this */
public final class ContributorList{
    private static final ObjectMap<ContributionType, Seq<String>> contributors = ObjectMap.of(
        // DO NOT INSERT COLORS!
        ContributionType.collaborator, Seq.with(
            "Eldoofus",
            "Gdeft",
            "GlennFolker",
            "Goobrr",
            "JerichoFletcher",
            "sk7725",
            "ThePythonGuy3",
            "ThirstyBoi",
            "Txar",
            "Xelo",
            "Xusk",
            "younggam",
            "MEEP of Faith"
        ),

        ContributionType.contributor, Seq.with(
            "Drullkus",
            "Anuke"
        ),

        ContributionType.translator, Seq.with(
            "sk7725 (Korean)",
            "Xusk (Russian)"
        )
    );

    public static Seq<String> getBy(ContributionType type){
        return contributors.get(type);
    }

    public enum ContributionType{
        collaborator,
        contributor,
        translator;

        public static ContributionType[] all = values();
    }
}
